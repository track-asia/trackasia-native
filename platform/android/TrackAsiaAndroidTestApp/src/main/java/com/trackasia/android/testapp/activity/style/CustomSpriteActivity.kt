package com.trackasia.android.testapp.activity.style

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.trackasia.android.camera.CameraUpdateFactory
import com.trackasia.android.geometry.LatLng
import com.trackasia.android.maps.MapView
import com.trackasia.android.maps.Style
import com.trackasia.android.maps.TrackAsiaMap
import com.trackasia.android.style.layers.Layer
import com.trackasia.android.style.layers.PropertyFactory
import com.trackasia.android.style.layers.SymbolLayer
import com.trackasia.android.style.sources.GeoJsonSource
import com.trackasia.android.testapp.R
import com.trackasia.android.testapp.styles.TestStyles
import com.trackasia.geojson.Feature
import com.trackasia.geojson.FeatureCollection
import com.trackasia.geojson.Point
import timber.log.Timber

/**
 * Test activity showcasing adding a sprite image and use it in a Symbol Layer
 */
class CustomSpriteActivity : AppCompatActivity() {
    private var source: GeoJsonSource? = null
    private lateinit var trackasiaMap: TrackAsiaMap
    private lateinit var mapView: MapView
    private lateinit var layer: Layer

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_sprite)
        mapView = findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync {
            trackasiaMap = it
            it.setStyle(TestStyles.OPENFREEMAP_LIBERY) { style: Style ->
                val fab = findViewById<FloatingActionButton>(R.id.fab)
                fab.setColorFilter(
                    ContextCompat.getColor(
                        this@CustomSpriteActivity,
                        R.color.primary,
                    ),
                )
                fab.setOnClickListener(
                    object : View.OnClickListener {
                        private lateinit var point: Point

                        override fun onClick(view: View) {
                            if (!this::point.isInitialized) {
                                Timber.i("First click -> Car")
                                // --8<-- [start:addImage]
                                // Add an icon to reference later
                                style.addImage(
                                    CUSTOM_ICON,
                                    BitmapFactory.decodeResource(
                                        resources,
                                        R.drawable.ic_car_top,
                                    ),
                                )

                                // Add a source with a geojson point
                                point = Point.fromLngLat(13.400972, 52.519003)
                                source =
                                    GeoJsonSource(
                                        "point",
                                        FeatureCollection.fromFeatures(arrayOf(Feature.fromGeometry(point))),
                                    )
                                trackasiaMap.style!!.addSource(source!!)

                                // Add a symbol layer that references that point source
                                layer = SymbolLayer("layer", "point")
                                layer.setProperties( // Set the id of the sprite to use
                                    PropertyFactory.iconImage(CUSTOM_ICON),
                                    PropertyFactory.iconAllowOverlap(true),
                                    PropertyFactory.iconIgnorePlacement(true),
                                )

                                // lets add a circle below labels!
                                trackasiaMap.style!!.addLayerBelow(layer, "water-intermittent")
                                fab.setImageResource(R.drawable.ic_directions_car_black)
                                // --8<-- [end:addImage]
                            } else {
                                // Update point
                                point =
                                    Point.fromLngLat(
                                        point.longitude() + 0.001,
                                        point.latitude() + 0.001,
                                    )
                                source!!.setGeoJson(
                                    FeatureCollection.fromFeatures(
                                        arrayOf(
                                            Feature.fromGeometry(
                                                point,
                                            ),
                                        ),
                                    ),
                                )

                                // Move the camera as well
                                trackasiaMap.moveCamera(
                                    CameraUpdateFactory.newLatLng(
                                        LatLng(
                                            point.latitude(),
                                            point.longitude(),
                                        ),
                                    ),
                                )
                            }
                        }
                    },
                )
            }
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

    companion object {
        private const val CUSTOM_ICON = "custom-icon"
    }
}
