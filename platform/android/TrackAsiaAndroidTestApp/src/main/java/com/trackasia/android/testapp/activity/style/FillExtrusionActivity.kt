package com.trackasia.android.testapp.activity.style

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.trackasia.android.camera.CameraPosition
import com.trackasia.android.camera.CameraUpdateFactory
import com.trackasia.android.geometry.LatLng
import com.trackasia.android.maps.MapView
import com.trackasia.android.maps.OnMapReadyCallback
import com.trackasia.android.maps.Style
import com.trackasia.android.maps.TrackAsiaMap
import com.trackasia.android.style.layers.*
import com.trackasia.android.style.sources.GeoJsonSource
import com.trackasia.android.testapp.R
import com.trackasia.android.testapp.styles.TestStyles
import com.trackasia.geojson.Point
import com.trackasia.geojson.Polygon
import java.util.*

/**
 * Test activity showcasing fill extrusions
 */
class FillExtrusionActivity : AppCompatActivity() {
    private lateinit var mapView: MapView

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fill_extrusion_layer)
        mapView = findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(
            OnMapReadyCallback { trackasiaMap: TrackAsiaMap ->
                trackasiaMap.setStyle(TestStyles.getPredefinedStyleWithFallback("Streets")) { style: Style ->
                    val lngLats =
                        listOf(
                            Arrays.asList(
                                Point.fromLngLat(5.12112557888031, 52.09071040847704),
                                Point.fromLngLat(5.121227502822875, 52.09053901776669),
                                Point.fromLngLat(5.121484994888306, 52.090601641371805),
                                Point.fromLngLat(5.1213884353637695, 52.090766439912635),
                                Point.fromLngLat(5.12112557888031, 52.09071040847704),
                            ),
                        )
                    val domTower = Polygon.fromLngLats(lngLats)
                    val source = GeoJsonSource("extrusion-source", domTower)
                    style.addSource(source)
                    style.addLayer(
                        FillExtrusionLayer("extrusion-layer", source.id)
                            .withProperties(
                                PropertyFactory.fillExtrusionHeight(40f),
                                PropertyFactory.fillExtrusionOpacity(0.5f),
                                PropertyFactory.fillExtrusionColor(Color.RED),
                            ),
                    )
                    trackasiaMap.animateCamera(
                        CameraUpdateFactory.newCameraPosition(
                            CameraPosition
                                .Builder()
                                .target(LatLng(52.09071040847704, 5.12112557888031))
                                .tilt(45.0)
                                .zoom(18.0)
                                .build(),
                        ),
                        10000,
                    )
                }
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

    public override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    public override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }
}
