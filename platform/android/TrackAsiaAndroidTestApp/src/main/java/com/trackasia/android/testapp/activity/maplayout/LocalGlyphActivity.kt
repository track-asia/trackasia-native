package com.trackasia.android.testapp.activity.maplayout

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.trackasia.android.camera.CameraPosition
import com.trackasia.android.camera.CameraUpdateFactory
import com.trackasia.android.geometry.LatLng
import com.trackasia.android.maps.*
import com.trackasia.android.testapp.R
import com.trackasia.android.testapp.styles.TestStyles

/**
 * Test activity that displays the city of Suzhou with a mixture of server-generated
 * latin glyphs and CJK glyphs generated locally using "Droid Sans" as a font family.
 */
class LocalGlyphActivity : AppCompatActivity() {
    private lateinit var mapView: MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_local_glyph)
        mapView = findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(
            OnMapReadyCallback { trackasiaMap: TrackAsiaMap ->
                trackasiaMap.setStyle(TestStyles.getPredefinedStyleWithFallback("Streets"))
                // Set initial position to Suzhou
                trackasiaMap.moveCamera(
                    CameraUpdateFactory.newCameraPosition(
                        CameraPosition
                            .Builder()
                            .target(LatLng(31.3003, 120.7457))
                            .zoom(11.0)
                            .bearing(0.0)
                            .tilt(0.0)
                            .build(),
                    ),
                )
            },
        )
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

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }
}
