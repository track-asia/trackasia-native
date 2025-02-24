@file:Suppress("ktlint:standard:no-wildcard-imports")

package com.trackasia.android.testapp.activity.annotation

import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.trackasia.android.annotations.Polygon
import com.trackasia.android.annotations.PolygonOptions
import com.trackasia.android.camera.CameraPosition
import com.trackasia.android.geometry.LatLng
import com.trackasia.android.maps.*
import com.trackasia.android.testapp.R
import com.trackasia.android.testapp.styles.TestStyles
import java.util.ArrayList

/**
 * Test activity to showcase the Polygon annotation API & programmatically creating a MapView.
 *
 *
 * Shows how to change Polygon features as visibility, alpha, color and points.
 *
 */
class PolygonActivity :
    AppCompatActivity(),
    OnMapReadyCallback {
    private lateinit var mapView: MapView
    private lateinit var trackasiaMap: TrackAsiaMap
    private var polygon: Polygon? = null
    private var fullAlpha = true
    private var polygonIsVisible = true
    private var color = true
    private var allPoints = true
    private var holes = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // configure inital map state
        val options =
            TrackAsiaMapOptions
                .createFromAttributes(this, null)
                .attributionTintColor(Config.RED_COLOR)
                .compassFadesWhenFacingNorth(false)
                .camera(
                    CameraPosition
                        .Builder()
                        .target(LatLng(45.520486, -122.673541))
                        .zoom(12.0)
                        .tilt(40.0)
                        .build(),
                )

        // create map
        mapView = MapView(this, options)
        mapView.id = R.id.mapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
        setContentView(mapView)
    }

    override fun onMapReady(map: TrackAsiaMap) {
        trackasiaMap = map
        map.setStyle(TestStyles.getPredefinedStyleWithFallback("Streets"))
        map.setOnPolygonClickListener { polygon: Polygon ->
            Toast
                .makeText(
                    this@PolygonActivity,
                    "You clicked on polygon with id = " + polygon.id,
                    Toast.LENGTH_SHORT,
                ).show()
        }
        polygon =
            trackasiaMap.addPolygon(
                PolygonOptions()
                    .addAll(Config.STAR_SHAPE_POINTS)
                    .fillColor(Config.BLUE_COLOR),
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            R.id.action_id_alpha -> {
                fullAlpha = !fullAlpha
                polygon!!.alpha =
                    if (fullAlpha) Config.FULL_ALPHA else Config.PARTIAL_ALPHA
                true
            }
            R.id.action_id_visible -> {
                polygonIsVisible = !polygonIsVisible
                polygon!!.alpha =
                    if (polygonIsVisible) {
                        if (fullAlpha) {
                            Config.FULL_ALPHA
                        } else {
                            Config.PARTIAL_ALPHA
                        }
                    } else {
                        Config.NO_ALPHA
                    }
                true
            }
            R.id.action_id_points -> {
                allPoints = !allPoints
                polygon!!.points =
                    if (allPoints) Config.STAR_SHAPE_POINTS else Config.BROKEN_SHAPE_POINTS
                true
            }
            R.id.action_id_color -> {
                color = !color
                polygon!!.fillColor =
                    if (color) Config.BLUE_COLOR else Config.RED_COLOR
                true
            }
            R.id.action_id_holes -> {
                holes = !holes
                polygon!!.holes =
                    if (holes) Config.STAR_SHAPE_HOLES else emptyList()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_polygon, menu)
        return true
    }

    internal object Config {
        val BLUE_COLOR = Color.parseColor("#3bb2d0")
        val RED_COLOR = Color.parseColor("#AF0000")
        const val FULL_ALPHA = 1.0f
        const val PARTIAL_ALPHA = 0.5f
        const val NO_ALPHA = 0.0f
        val STAR_SHAPE_POINTS: ArrayList<LatLng?> =
            object : ArrayList<LatLng?>() {
                init {
                    add(LatLng(45.522585, -122.685699))
                    add(LatLng(45.534611, -122.708873))
                    add(LatLng(45.530883, -122.678833))
                    add(LatLng(45.547115, -122.667503))
                    add(LatLng(45.530643, -122.660121))
                    add(LatLng(45.533529, -122.636260))
                    add(LatLng(45.521743, -122.659091))
                    add(LatLng(45.510677, -122.648792))
                    add(LatLng(45.515008, -122.664070))
                    add(LatLng(45.502496, -122.669048))
                    add(LatLng(45.515369, -122.678489))
                    add(LatLng(45.506346, -122.702007))
                    add(LatLng(45.522585, -122.685699))
                }
            }
        val BROKEN_SHAPE_POINTS = STAR_SHAPE_POINTS.subList(0, STAR_SHAPE_POINTS.size - 3)
        val STAR_SHAPE_HOLES: ArrayList<List<LatLng?>?> =
            object : ArrayList<List<LatLng?>?>() {
                init {
                    add(
                        ArrayList<LatLng>(
                            object : ArrayList<LatLng?>() {
                                init {
                                    add(LatLng(45.521743, -122.669091))
                                    add(LatLng(45.530483, -122.676833))
                                    add(LatLng(45.520483, -122.676833))
                                    add(LatLng(45.521743, -122.669091))
                                }
                            },
                        ),
                    )
                    add(
                        ArrayList<LatLng>(
                            object : ArrayList<LatLng?>() {
                                init {
                                    add(LatLng(45.529743, -122.662791))
                                    add(LatLng(45.525543, -122.662791))
                                    add(LatLng(45.525543, -122.660))
                                    add(LatLng(45.527743, -122.660))
                                    add(LatLng(45.529743, -122.662791))
                                }
                            },
                        ),
                    )
                }
            }
    }
}
