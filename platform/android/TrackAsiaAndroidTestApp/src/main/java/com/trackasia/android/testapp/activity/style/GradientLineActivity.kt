package com.trackasia.android.testapp.activity.style

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.trackasia.android.maps.MapView
import com.trackasia.android.maps.OnMapReadyCallback
import com.trackasia.android.maps.Style
import com.trackasia.android.maps.TrackAsiaMap
import com.trackasia.android.style.expressions.Expression
import com.trackasia.android.style.layers.*
import com.trackasia.android.style.sources.GeoJsonOptions
import com.trackasia.android.style.sources.GeoJsonSource
import com.trackasia.android.testapp.R
import com.trackasia.android.testapp.utils.ResourceUtils
import timber.log.Timber
import java.io.IOException

/**
 * Activity showcasing applying a gradient coloring to a line layer.
 */
class GradientLineActivity :
    AppCompatActivity(),
    OnMapReadyCallback {
    private lateinit var mapView: MapView

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gradient_line)
        mapView = findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
    }

    override fun onMapReady(trackasiaMap: TrackAsiaMap) {
        try {
            val geoJson =
                ResourceUtils.readRawResource(
                    this@GradientLineActivity,
                    R.raw.test_line_gradient_feature,
                )
            trackasiaMap.setStyle(
                Style
                    .Builder()
                    .withSource(
                        GeoJsonSource(
                            LINE_SOURCE,
                            geoJson,
                            GeoJsonOptions().withLineMetrics(true),
                        ),
                    ).withLayer(
                        LineLayer("gradient", LINE_SOURCE)
                            .withProperties(
                                PropertyFactory.lineGradient(
                                    Expression.interpolate(
                                        Expression.linear(),
                                        Expression.lineProgress(),
                                        Expression.stop(0f, Expression.rgb(0, 0, 255)),
                                        Expression.stop(0.5f, Expression.rgb(0, 255, 0)),
                                        Expression.stop(1f, Expression.rgb(255, 0, 0)),
                                    ),
                                ),
                                PropertyFactory.lineColor(Color.RED),
                                PropertyFactory.lineWidth(10.0f),
                                PropertyFactory.lineCap(Property.LINE_CAP_ROUND),
                                PropertyFactory.lineJoin(Property.LINE_JOIN_ROUND),
                            ),
                    ),
            )
        } catch (exception: IOException) {
            Timber.e(exception)
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
        const val LINE_SOURCE = "gradient"
    }
}
