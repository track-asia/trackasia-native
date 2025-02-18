package com.trackasia.android.testapp.activity.offline

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.trackasia.android.TrackAsia
import com.trackasia.android.log.Logger
import com.trackasia.android.maps.Style
import com.trackasia.android.offline.OfflineManager
import com.trackasia.android.offline.OfflineRegion
import com.trackasia.android.storage.FileSource
import com.trackasia.android.testapp.databinding.ActivityMergeOfflineRegionsBinding
import com.trackasia.android.testapp.styles.TestStyles
import com.trackasia.android.testapp.utils.FileUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

class MergeOfflineRegionsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMergeOfflineRegionsBinding

    companion object {
        private const val LOG_TAG = "Mbgl-MergeOfflineRegionsActivity"
        private const val TEST_DB_FILE_NAME = "offline_test.db"
        private var TEST_STYLE = TestStyles.getPredefinedStyleWithFallback("Satellite Hybrid")
    }

    private val onRegionMergedListener =
        object : OfflineManager.MergeOfflineRegionsCallback {
            override fun onMerge(offlineRegions: Array<OfflineRegion>?) {
                binding.mapView.getMapAsync {
                    it.setStyle(Style.Builder().fromUri(TEST_STYLE))
                }
                Toast
                    .makeText(
                        this@MergeOfflineRegionsActivity,
                        String.format(Locale.ENGLISH, "Merged %d regions.", offlineRegions?.size ?: 0),
                        Toast.LENGTH_LONG,
                    ).show()
            }

            override fun onError(error: String) {
                Toast
                    .makeText(
                        this@MergeOfflineRegionsActivity,
                        String.format("Offline DB merge error."),
                        Toast.LENGTH_LONG,
                    ).show()
                Logger.e(LOG_TAG, error)
            }
        }

    /**
     * Since we expect from the results of the offline merge callback to interact with the hosting activity,
     * we need to ensure that we are not interacting with a destroyed activity.
     */
    private class MergeCallback(
        private var activityCallback: OfflineManager.MergeOfflineRegionsCallback?,
    ) : OfflineManager.MergeOfflineRegionsCallback {
        override fun onMerge(offlineRegions: Array<OfflineRegion>?) {
            activityCallback?.onMerge(offlineRegions)
        }

        override fun onError(error: String) {
            activityCallback?.onError(error)
        }

        fun onActivityDestroy() {
            activityCallback = null
        }
    }

    private val mergeCallback = MergeCallback(onRegionMergedListener)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMergeOfflineRegionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // forcing offline state
        TrackAsia.setConnected(false)

        binding.mapView.onCreate(savedInstanceState)
        binding.loadRegionBtn.setOnClickListener {
            copyAsset()
        }
        binding.mapView.getMapAsync {
            it.isDebugActive = true
            it.setStyle(Style.Builder().fromUri(TEST_STYLE))
        }
    }

    private fun copyAsset() {
        // copy db asset to internal memory
        lifecycleScope.launch(Dispatchers.IO) {
            val copied =
                FileUtils.copyFileFromAssetsTask(
                    this@MergeOfflineRegionsActivity,
                    TEST_DB_FILE_NAME,
                    FileSource.getResourcesCachePath(this@MergeOfflineRegionsActivity),
                )
            withContext(Dispatchers.Main) {
                if (copied) {
                    Toast
                        .makeText(
                            this@MergeOfflineRegionsActivity,
                            String.format("OnFileCopied."),
                            Toast.LENGTH_LONG,
                        ).show()
                    mergeDb()
                } else {
                    Toast
                        .makeText(
                            this@MergeOfflineRegionsActivity,
                            String.format("Error copying DB file."),
                            Toast.LENGTH_LONG,
                        ).show()
                }
            }
        }
    }

    private fun mergeDb() {
        OfflineManager.getInstance(this).mergeOfflineRegions(
            FileSource.getResourcesCachePath(this) + "/" + TEST_DB_FILE_NAME,
            mergeCallback,
        )
    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mergeCallback.onActivityDestroy()
        binding.mapView.onDestroy()

        // restoring connectivity state
        TrackAsia.setConnected(null)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
    }
}
