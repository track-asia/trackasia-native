package com.trackasia.android.testapp.activity.location

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.trackasia.android.geometry.LatLngBounds
import com.trackasia.android.location.LocationComponent
import com.trackasia.android.location.LocationComponentActivationOptions
import com.trackasia.android.location.engine.LocationEngine
import com.trackasia.android.location.engine.LocationEngineDefault
import com.trackasia.android.location.engine.LocationEngineRequest
import com.trackasia.android.location.modes.RenderMode
import com.trackasia.android.location.permissions.PermissionsListener
import com.trackasia.android.location.permissions.PermissionsManager
import com.trackasia.android.maps.MapView
import com.trackasia.android.maps.OnMapReadyCallback
import com.trackasia.android.maps.Style
import com.trackasia.android.maps.TrackAsiaMap
import com.trackasia.android.testapp.R
import com.trackasia.android.testapp.styles.TestStyles

class ManualLocationUpdatesActivity :
    AppCompatActivity(),
    OnMapReadyCallback {
    private lateinit var mapView: MapView
    private var locationComponent: LocationComponent? = null
    private var locationEngine: LocationEngine? = null
    private var permissionsManager: PermissionsManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_manual_update)
        locationEngine = LocationEngineDefault.getDefaultLocationEngine(mapView.context)
        val fabManualUpdate = findViewById<FloatingActionButton>(R.id.fabManualLocationChange)
        fabManualUpdate.setOnClickListener { v: View? ->
            if (locationComponent != null && locationComponent!!.locationEngine == null) {
                locationComponent!!.forceLocationUpdate(
                    Utils.getRandomLocation(LatLngBounds.from(60.0, 25.0, 40.0, -5.0)),
                )
            }
        }
        fabManualUpdate.isEnabled = false
        val fabToggle = findViewById<FloatingActionButton>(R.id.fabToggleManualLocation)
        fabToggle.setOnClickListener { v: View? ->
            if (locationComponent != null) {
                locationComponent!!.locationEngine =
                    if (locationComponent!!.locationEngine == null) locationEngine else null
                if (locationComponent!!.locationEngine == null) {
                    fabToggle.setImageResource(R.drawable.ic_layers_clear)
                    fabManualUpdate.isEnabled = true
                    fabManualUpdate.alpha = 1f
                    Toast
                        .makeText(
                            this@ManualLocationUpdatesActivity.applicationContext,
                            "LocationEngine disabled, use manual updates",
                            Toast.LENGTH_SHORT,
                        ).show()
                } else {
                    fabToggle.setImageResource(R.drawable.ic_layers)
                    fabManualUpdate.isEnabled = false
                    fabManualUpdate.alpha = 0.5f
                    Toast
                        .makeText(
                            this@ManualLocationUpdatesActivity.applicationContext,
                            "LocationEngine enabled",
                            Toast.LENGTH_SHORT,
                        ).show()
                }
            }
        }
        mapView = findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            mapView.getMapAsync(this)
        } else {
            permissionsManager =
                PermissionsManager(
                    object : PermissionsListener {
                        override fun onExplanationNeeded(permissionsToExplain: List<String>) {
                            Toast
                                .makeText(
                                    this@ManualLocationUpdatesActivity.applicationContext,
                                    "You need to accept location permissions.",
                                    Toast.LENGTH_SHORT,
                                ).show()
                        }

                        override fun onPermissionResult(granted: Boolean) {
                            if (granted) {
                                mapView.getMapAsync(this@ManualLocationUpdatesActivity)
                            } else {
                                finish()
                            }
                        }
                    },
                )
            permissionsManager!!.requestLocationPermissions(this)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionsManager!!.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(trackasiaMap: TrackAsiaMap) {
        trackasiaMap.setStyle(
            Style.Builder().fromUri(TestStyles.getPredefinedStyleWithFallback("Streets")),
        ) { style: Style? ->
            locationComponent = trackasiaMap.locationComponent
            locationComponent!!.activateLocationComponent(
                LocationComponentActivationOptions
                    .builder(this, style!!)
                    .locationEngine(locationEngine)
                    .locationEngineRequest(
                        LocationEngineRequest
                            .Builder(500)
                            .setFastestInterval(500)
                            .setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY)
                            .build(),
                    ).build(),
            )
            locationComponent!!.isLocationComponentEnabled = true
            locationComponent!!.renderMode = RenderMode.COMPASS
        }
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
}
