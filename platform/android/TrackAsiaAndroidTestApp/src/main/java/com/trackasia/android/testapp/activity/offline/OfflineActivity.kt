package com.trackasia.android.testapp.activity.offline

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.trackasia.android.TrackAsia
import com.trackasia.android.camera.CameraPosition
import com.trackasia.android.camera.CameraUpdateFactory
import com.trackasia.android.geometry.LatLng
import com.trackasia.android.maps.MapView
import com.trackasia.android.maps.Style
import com.trackasia.android.maps.TrackAsiaMap
import com.trackasia.android.offline.OfflineManager
import com.trackasia.android.offline.OfflineManager.CreateOfflineRegionCallback
import com.trackasia.android.offline.OfflineManager.ListOfflineRegionsCallback
import com.trackasia.android.offline.OfflineRegion
import com.trackasia.android.offline.OfflineRegion.OfflineRegionObserver
import com.trackasia.android.offline.OfflineRegionError
import com.trackasia.android.offline.OfflineRegionStatus
import com.trackasia.android.offline.OfflineTilePyramidRegionDefinition
import com.trackasia.android.testapp.R
import com.trackasia.android.testapp.model.other.OfflineDownloadRegionDialog
import com.trackasia.android.testapp.model.other.OfflineDownloadRegionDialog.DownloadRegionDialogListener
import com.trackasia.android.testapp.model.other.OfflineListRegionsDialog
import com.trackasia.android.testapp.styles.TestStyles
import com.trackasia.android.testapp.utils.OfflineUtils
import timber.log.Timber
import java.util.ArrayList

/**
 * Test activity showcasing the Offline API.
 *
 *
 * Shows a map of Manhattan and allows the user to download and name a region.
 *
 */
class OfflineActivity :
    AppCompatActivity(),
    DownloadRegionDialogListener {
    /*
     * UI elements
     */
    private lateinit var mapView: MapView
    private lateinit var trackasiaMap: TrackAsiaMap
    private var progressBar: ProgressBar? = null
    private var downloadRegion: Button? = null
    private var listRegions: Button? = null
    private var isEndNotified = false
    val STYLE_URL: String
        get() = TestStyles.getPredefinedStyleWithFallback("Streets")

    /*
     * Offline objects
     */
    private var offlineManager: OfflineManager? = null
    private var offlineRegion: OfflineRegion? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_offline)

        // You can use TrackAsia.setConnected(Boolean) to manually set the connectivity
        // state of your app. This will override any checks performed via the ConnectivityManager.
        // TrackAsia.getInstance().setConnected(false);
        val connected = TrackAsia.isConnected()
        Timber.d("TrackAsia is connected: %s", connected)

        // Set up map
        mapView = findViewById<View>(R.id.mapView) as MapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync { trackasiaMap: TrackAsiaMap ->
            Timber.d("Map is ready")
            this@OfflineActivity.trackasiaMap = trackasiaMap
            trackasiaMap.setStyle(Style.Builder().fromUri(STYLE_URL))
            // Set initial position to UNHQ in NYC
            trackasiaMap.moveCamera(
                CameraUpdateFactory.newCameraPosition(
                    CameraPosition
                        .Builder()
                        .target(LatLng(40.749851, -73.967966))
                        .zoom(14.0)
                        .bearing(0.0)
                        .tilt(0.0)
                        .build(),
                ),
            )
        }

        // The progress bar
        progressBar = findViewById<View>(R.id.progress_bar) as ProgressBar

        // Set up button listeners
        downloadRegion = findViewById<View>(R.id.button_download_region) as Button
        downloadRegion!!.setOnClickListener { view: View? -> handleDownloadRegion() }
        listRegions = findViewById<View>(R.id.button_list_regions) as Button
        listRegions!!.setOnClickListener { view: View? -> handleListRegions() }

        // Set up the OfflineManager
        offlineManager = OfflineManager.getInstance(this)
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    /*
     * Buttons logic
     */
    private fun handleDownloadRegion() {
        Timber.d("handleDownloadRegion")

        // Show dialog
        val offlineDownloadRegionDialog = OfflineDownloadRegionDialog()
        offlineDownloadRegionDialog.show(supportFragmentManager, "download")
    }

    private fun handleListRegions() {
        Timber.d("handleListRegions")

        // Query the DB asynchronously
        offlineManager!!.listOfflineRegions(
            object : ListOfflineRegionsCallback {
                override fun onList(offlineRegions: Array<OfflineRegion>?) {
                    // Check result
                    if (offlineRegions == null || offlineRegions.isEmpty()) {
                        Toast
                            .makeText(
                                this@OfflineActivity,
                                "You have no regions yet.",
                                Toast.LENGTH_SHORT,
                            ).show()
                        return
                    }

                    // Get regions info
                    val offlineRegionsNames = ArrayList<String>()
                    for (offlineRegion in offlineRegions) {
                        offlineRegionsNames.add(OfflineUtils.convertRegionName(offlineRegion.metadata))
                    }

                    // Create args
                    val args = Bundle()
                    args.putStringArrayList(OfflineListRegionsDialog.ITEMS, offlineRegionsNames)

                    // Show dialog
                    val offlineListRegionsDialog = OfflineListRegionsDialog()
                    offlineListRegionsDialog.arguments = args
                    offlineListRegionsDialog.show(supportFragmentManager, "list")
                }

                override fun onError(error: String) {
                    Timber.e("Error: %s", error)
                }
            },
        )
    }

    /*
     * Dialogs
     */
    override fun onDownloadRegionDialogPositiveClick(regionName: String?) {
        if (TextUtils.isEmpty(regionName)) {
            Toast
                .makeText(this@OfflineActivity, "Region name cannot be empty.", Toast.LENGTH_SHORT)
                .show()
            return
        }

        // Start progress bar
        Timber.d("Download started: %s", regionName)
        startProgress()

        // Definition
        val bounds = trackasiaMap.projection.visibleRegion.latLngBounds
        val minZoom = trackasiaMap.cameraPosition.zoom
        val maxZoom = trackasiaMap.maxZoomLevel
        val pixelRatio = this.resources.displayMetrics.density
        val definition =
            OfflineTilePyramidRegionDefinition(
                STYLE_URL,
                bounds,
                minZoom,
                maxZoom,
                pixelRatio,
            )

        // Sample way of encoding metadata from a JSONObject
        val metadata = OfflineUtils.convertRegionName(regionName)

        // Create region
        if (metadata != null) {
            offlineManager!!.createOfflineRegion(
                definition,
                metadata,
                object : CreateOfflineRegionCallback {
                    override fun onCreate(offlineRegion: OfflineRegion) {
                        Timber.d("Offline region created: %s", regionName)
                        this@OfflineActivity.offlineRegion = offlineRegion
                        launchDownload()
                    }

                    override fun onError(error: String) {
                        Timber.e("Error: %s", error)
                    }
                },
            )
        }
    }

    private fun launchDownload() {
        // Set an observer
        offlineRegion!!.setObserver(
            object : OfflineRegionObserver {
                override fun onStatusChanged(status: OfflineRegionStatus) {
                    // Compute a percentage
                    val percentage =
                        if (status.requiredResourceCount >= 0) 100.0 * status.completedResourceCount / status.requiredResourceCount else 0.0
                    if (status.isComplete) {
                        // Download complete
                        endProgress("Region downloaded successfully.")
                        offlineRegion!!.setDownloadState(OfflineRegion.STATE_INACTIVE)
                        offlineRegion!!.setObserver(null)
                        return
                    } else if (status.isRequiredResourceCountPrecise) {
                        // Switch to determinate state
                        setPercentage(Math.round(percentage).toInt())
                    }

                    // Debug
                    Timber.d(
                        "%s/%s resources; %s bytes downloaded.",
                        status.completedResourceCount.toString(),
                        status.requiredResourceCount.toString(),
                        status.completedResourceSize.toString(),
                    )
                }

                override fun onError(error: OfflineRegionError) {
                    Timber.e("onError: %s, %s", error.reason, error.message)
                    offlineRegion!!.setDownloadState(OfflineRegion.STATE_INACTIVE)
                }

                override fun mapboxTileCountLimitExceeded(limit: Long) {
                    Timber.e("TrackAsia tile count limit exceeded: %s", limit)
                    offlineRegion!!.setDownloadState(OfflineRegion.STATE_INACTIVE)
                }
            },
        )

        // Change the region state
        offlineRegion!!.setDownloadState(OfflineRegion.STATE_ACTIVE)
    }

    /*
     * Progress bar
     */
    private fun startProgress() {
        // Disable buttons
        downloadRegion!!.isEnabled = false
        listRegions!!.isEnabled = false

        // Start and show the progress bar
        isEndNotified = false
        progressBar!!.isIndeterminate = true
        progressBar!!.visibility = View.VISIBLE
    }

    private fun setPercentage(percentage: Int) {
        progressBar!!.isIndeterminate = false
        progressBar!!.progress = percentage
    }

    private fun endProgress(message: String) {
        // Don't notify more than once
        if (isEndNotified) {
            return
        }

        // Enable buttons
        downloadRegion!!.isEnabled = true
        listRegions!!.isEnabled = true

        // Stop and hide the progress bar
        isEndNotified = true
        progressBar!!.isIndeterminate = false
        progressBar!!.visibility = View.GONE

        // Show a toast
        Toast.makeText(this@OfflineActivity, message, Toast.LENGTH_LONG).show()
    }

    companion object {
        // JSON encoding/decoding
        val JSON_CHARSET = Charsets.UTF_8
        const val JSON_FIELD_REGION_NAME = "FIELD_REGION_NAME"
    }
}
