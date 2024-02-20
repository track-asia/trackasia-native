package com.trackasia.android.testapp.activity.location

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.trackasia.android.camera.CameraUpdateFactory
import com.trackasia.android.geometry.LatLng
import com.trackasia.android.location.LocationComponentActivationOptions
import com.trackasia.android.location.engine.LocationEngineCallback
import com.trackasia.android.location.engine.LocationEngineResult
import com.trackasia.android.location.permissions.PermissionsListener
import com.trackasia.android.location.permissions.PermissionsManager
import com.trackasia.android.maps.MapView
import com.trackasia.android.maps.TrackasiaMap
import com.trackasia.android.maps.Style
import com.trackasia.android.testapp.R
import com.trackasia.android.testapp.databinding.ActivityLocationLayerFragmentBinding

class LocationFragmentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLocationLayerFragmentBinding

    private lateinit var permissionsManager: PermissionsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocationLayerFragmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.fab.setOnClickListener {
            val fragment = supportFragmentManager.findFragmentByTag(EmptyFragment.TAG)
            if (fragment == null) {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.container, EmptyFragment.newInstance(), EmptyFragment.TAG)
                    .addToBackStack("transaction2")
                    .commit()
            } else {
                this.onBackPressed()
            }
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            if (savedInstanceState == null) {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.container, LocationFragment.newInstance(), LocationFragment.TAG)
                    .commit()
            }
        } else {
            permissionsManager = PermissionsManager(object : PermissionsListener {
                override fun onExplanationNeeded(permissionsToExplain: MutableList<String>?) {
                    Toast.makeText(
                        this@LocationFragmentActivity,
                        "You need to accept location permissions.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onPermissionResult(granted: Boolean) {
                    if (granted) {
                        if (savedInstanceState == null) {
                            supportFragmentManager
                                .beginTransaction()
                                .replace(R.id.container, LocationFragment.newInstance(), LocationFragment.TAG)
                                .commit()
                        }
                    } else {
                        finish()
                    }
                }
            })
            permissionsManager.requestLocationPermissions(this)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    class LocationFragment : androidx.fragment.app.Fragment(), LocationEngineCallback<LocationEngineResult> {
        companion object {
            const val TAG = "LFragment"
            fun newInstance(): LocationFragment {
                return LocationFragment()
            }
        }

        private lateinit var mapView: MapView
        private lateinit var mapboxMap: TrackasiaMap

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            mapView = MapView(inflater.context)
            return mapView
        }

        @SuppressLint("MissingPermission")
        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            mapView.onCreate(savedInstanceState)
            mapView.getMapAsync {
                mapboxMap = it
                it.setStyle("https://tiles.track-asia.com/tiles/v1/style-streets.json?key=public") { style ->
                    val component = mapboxMap.locationComponent

                    component.activateLocationComponent(
                        LocationComponentActivationOptions
                            .builder(requireActivity(), style)
                            .useDefaultLocationEngine(true)
                            .build()
                    )

                    component.isLocationComponentEnabled = true
                    component.locationEngine?.getLastLocation(this)
                }
            }
        }

        override fun onSuccess(result: LocationEngineResult?) {
            if (!mapView.isDestroyed) mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(result?.lastLocation!!), 12.0))
        }

        override fun onFailure(exception: Exception) {
            // noop
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

        override fun onSaveInstanceState(outState: Bundle) {
            super.onSaveInstanceState(outState)
            mapView.onSaveInstanceState(outState)
        }

        override fun onStop() {
            super.onStop()
            mapView.onStop()
        }

        override fun onLowMemory() {
            super.onLowMemory()
            mapView.onLowMemory()
        }

        override fun onDestroyView() {
            super.onDestroyView()
            mapView.onDestroy()
        }
    }

    class EmptyFragment : androidx.fragment.app.Fragment() {
        companion object {
            const val TAG = "EmptyFragment"
            fun newInstance(): EmptyFragment {
                return EmptyFragment()
            }
        }

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            val textView = TextView(inflater.context)
            textView.text = getString(R.string.this_is_an_empty_fragment)
            return textView
        }
    }
}
