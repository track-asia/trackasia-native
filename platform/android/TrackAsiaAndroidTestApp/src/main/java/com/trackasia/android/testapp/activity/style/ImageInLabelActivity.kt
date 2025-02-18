package com.trackasia.android.testapp.activity.style

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.trackasia.android.maps.MapView
import com.trackasia.android.maps.OnMapReadyCallback
import com.trackasia.android.maps.Style
import com.trackasia.android.maps.TrackAsiaMap
import com.trackasia.android.style.expressions.Expression
import com.trackasia.android.style.expressions.Expression.FormatOption
import com.trackasia.android.style.layers.PropertyFactory
import com.trackasia.android.style.layers.SymbolLayer
import com.trackasia.android.style.sources.GeoJsonSource
import com.trackasia.android.testapp.R
import com.trackasia.android.testapp.styles.TestStyles
import com.trackasia.android.utils.BitmapUtils
import com.trackasia.geojson.Feature
import com.trackasia.geojson.FeatureCollection
import com.trackasia.geojson.Point

/**
 * Test image in label.
 */
class ImageInLabelActivity :
    AppCompatActivity(),
    OnMapReadyCallback {
    private lateinit var mapView: MapView

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stretchable_image)
        mapView = findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
    }

    override fun onMapReady(trackasiaMap: TrackAsiaMap) {
        trackasiaMap.setStyle(TestStyles.getPredefinedStyleWithFallback("Streets")) { style: Style ->
            val us =
                BitmapUtils.getBitmapFromDrawable(
                    ResourcesCompat.getDrawable(resources, R.drawable.ic_us, theme),
                )
            val android =
                BitmapUtils.getBitmapFromDrawable(
                    ResourcesCompat.getDrawable(resources, R.drawable.ic_android, theme),
                )
            style.addImage("us", us!!)
            style.addImage("android", android!!)
            val point = Point.fromLngLat(-10.0, 0.0)
            val feature = Feature.fromGeometry(point)
            val originalCollection = FeatureCollection.fromFeature(feature)
            val originalSource = GeoJsonSource(ORIGINAL_SOURCE, originalCollection)
            val originalLayer =
                SymbolLayer(ORIGINAL_LAYER, ORIGINAL_SOURCE)
                    .withProperties(
                        PropertyFactory.textAllowOverlap(true),
                        PropertyFactory.textField(
                            Expression
                                .format(
                                    Expression.formatEntry(
                                        Expression.literal("Android: "),
                                        FormatOption.formatFontScale(1.0),
                                        FormatOption.formatTextColor(Color.BLUE),
                                        FormatOption.formatTextFont(
                                            arrayOf(
                                                "Ubuntu Medium",
                                                "Arial Unicode MS Regular",
                                            ),
                                        ),
                                    ),
                                    Expression.formatEntry(Expression.image(Expression.literal("android"))),
                                    Expression.formatEntry(
                                        Expression.literal("Us: "),
                                        FormatOption.formatFontScale(1.5),
                                        FormatOption.formatTextColor(Color.YELLOW),
                                    ),
                                    Expression.formatEntry(Expression.image(Expression.literal("us"))),
                                    Expression.formatEntry(
                                        Expression.literal("suffix"),
                                        FormatOption.formatFontScale(2.0),
                                        FormatOption.formatTextColor(Color.CYAN),
                                    ),
                                ),
                        ),
                    )
            style.addSource(originalSource)
            style.addLayer(originalLayer)
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
        private const val ORIGINAL_SOURCE = "ORIGINAL_SOURCE"
        private const val ORIGINAL_LAYER = "ORIGINAL_LAYER"
    }
}
