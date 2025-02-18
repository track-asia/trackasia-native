package com.trackasia.android.testapp.activity.maplayout

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.trackasia.android.camera.CameraPosition
import com.trackasia.android.camera.CameraUpdateFactory
import com.trackasia.android.geometry.LatLng
import com.trackasia.android.maps.*
import com.trackasia.android.testapp.R
import com.trackasia.android.testapp.styles.TestStyles
import com.trackasia.android.utils.MapFragmentUtils

/**
 * Test activity showcasing having 2 maps on top of each other.
 *
 *
 * The small map is using the `trackasia_enableZMediaOverlay="true"` configuration
 *
 */
class DoubleMapActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_fragment)
        if (savedInstanceState == null) {
            val options = TrackAsiaMapOptions.createFromAttributes(this, null)
            options.camera(
                CameraPosition
                    .Builder()
                    .target(MACHU_PICCHU)
                    .zoom(ZOOM_IN)
                    .build(),
            )
            val doubleMapFragment = DoubleMapFragment()
            doubleMapFragment.arguments = MapFragmentUtils.createFragmentArgs(options)
            val transaction = supportFragmentManager.beginTransaction()
            transaction.add(R.id.fragment_container, doubleMapFragment, TAG_FRAGMENT)
            transaction.commit()
        }
    }

    /**
     * Custom fragment containing 2 MapViews.
     */
    class DoubleMapFragment : Fragment() {
        private lateinit var mapView: MapView
        private lateinit var mapViewMini: MapView

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?,
        ): View? = inflater.inflate(R.layout.fragment_double_map, container, false)

        override fun onViewCreated(
            view: View,
            savedInstanceState: Bundle?,
        ) {
            super.onViewCreated(view, savedInstanceState)

            // MapView large
            mapView = MapView(view.context, MapFragmentUtils.resolveArgs(view.context, arguments))
            mapView.onCreate(savedInstanceState)
            mapView.getMapAsync { trackasiaMap: TrackAsiaMap ->
                trackasiaMap.setStyle(
                    TestStyles.getPredefinedStyleWithFallback(
                        "Streets",
                    ),
                )
            }
            (view.findViewById<View>(R.id.container) as ViewGroup).addView(mapView, 0)

            // MapView mini
            mapViewMini = view.findViewById(R.id.mini_map)
            mapViewMini.onCreate(savedInstanceState)
            mapViewMini.getMapAsync(
                OnMapReadyCallback { trackasiaMap: TrackAsiaMap ->
                    trackasiaMap.moveCamera(
                        CameraUpdateFactory.newCameraPosition(
                            CameraPosition
                                .Builder()
                                .target(MACHU_PICCHU)
                                .zoom(ZOOM_OUT)
                                .build(),
                        ),
                    )
                    trackasiaMap.setStyle(Style.Builder().fromUri(TestStyles.getPredefinedStyleWithFallback("Bright")))
                    val uiSettings = trackasiaMap.uiSettings
                    uiSettings.setAllGesturesEnabled(false)
                    uiSettings.isCompassEnabled = false
                    uiSettings.isAttributionEnabled = false
                    uiSettings.isLogoEnabled = false
                    trackasiaMap.addOnMapClickListener { point: LatLng? ->
                        // test if we can open 2 activities after each other
                        Toast
                            .makeText(
                                mapViewMini.getContext(),
                                "Creating a new Activity instance",
                                Toast.LENGTH_SHORT,
                            ).show()
                        startActivity(Intent(mapViewMini.getContext(), DoubleMapActivity::class.java))
                        false
                    }
                },
            )
        }

        override fun onResume() {
            super.onResume()
            mapView.onResume()
            mapViewMini!!.onResume()
        }

        override fun onStart() {
            super.onStart()
            mapView.onStart()
            mapViewMini!!.onStart()
        }

        override fun onPause() {
            super.onPause()
            mapView.onPause()
            mapViewMini!!.onPause()
        }

        override fun onStop() {
            super.onStop()
            mapView.onStop()
            mapViewMini!!.onStop()
        }

        override fun onDestroyView() {
            super.onDestroyView()
            mapView.onDestroy()
            mapViewMini!!.onDestroy()
        }

        override fun onLowMemory() {
            super.onLowMemory()
            mapView.onLowMemory()
            mapViewMini!!.onLowMemory()
        }

        override fun onSaveInstanceState(outState: Bundle) {
            super.onSaveInstanceState(outState)
            mapView.onSaveInstanceState(outState)
            // Mini map view is not interactive in this case, so we shouldn't save the instance.
            // If we'd like to support state saving for both maps, they'd have to be kept in separate fragments.
            // mapViewMini.onSaveInstanceState(outState);
        }
    }

    companion object {
        private const val TAG_FRAGMENT = "map"
        private val MACHU_PICCHU = LatLng(-13.1650709, -72.5447154)
        private const val ZOOM_IN = 12.0
        private const val ZOOM_OUT = 4.0
    }
}
