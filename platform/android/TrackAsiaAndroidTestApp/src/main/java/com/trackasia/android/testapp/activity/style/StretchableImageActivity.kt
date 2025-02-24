package com.trackasia.android.testapp.activity.style

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.lifecycleScope
import com.trackasia.android.maps.*
import com.trackasia.android.style.expressions.Expression
import com.trackasia.android.style.layers.*
import com.trackasia.android.style.sources.GeoJsonSource
import com.trackasia.android.testapp.R
import com.trackasia.android.testapp.styles.TestStyles
import com.trackasia.android.testapp.utils.GeoParseUtil
import com.trackasia.android.utils.BitmapUtils
import com.trackasia.geojson.Feature
import com.trackasia.geojson.FeatureCollection
import com.trackasia.geojson.Point
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.IOException
import java.util.ArrayList

/**
 * Test stretchable image as a background for text..
 */
class StretchableImageActivity :
    AppCompatActivity(),
    OnMapReadyCallback {
    private lateinit var trackasiaMap: TrackAsiaMap
    private lateinit var mapView: MapView

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stretchable_image)
        mapView = findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
    }

    override fun onMapReady(trackasiaMap: TrackAsiaMap) {
        this.trackasiaMap = trackasiaMap
        trackasiaMap.setStyle(TestStyles.getPredefinedStyleWithFallback("Streets")) { style: Style ->
            val popup =
                BitmapUtils.getBitmapFromDrawable(
                    ResourcesCompat.getDrawable(resources, R.drawable.popup, theme),
                )
            val popupDebug =
                BitmapUtils.getBitmapFromDrawable(
                    ResourcesCompat.getDrawable(resources, R.drawable.popup_debug, theme),
                )

            // The two (blue) columns of pixels that can be stretched horizontally:
            //   - the pixels between x: 25 and x: 55 can be stretched
            //   - the pixels between x: 85 and x: 115 can be stretched.
            val stretchX: MutableList<ImageStretches> = ArrayList()
            stretchX.add(ImageStretches(25F, 55F))
            stretchX.add(ImageStretches(85F, 115F))

            // The one (red) row of pixels that can be stretched vertically:
            //   - the pixels between y: 25 and y: 100 can be stretched
            val stretchY: MutableList<ImageStretches> = ArrayList()
            stretchY.add(ImageStretches(25F, 100F))

            // This part of the image that can contain text ([x1, y1, x2, y2]):
            val content = ImageContent(25F, 25F, 115F, 100F)
            style.addImage(NAME_POPUP, popup!!, stretchX, stretchY, content)
            style.addImage(NAME_POPUP_DEBUG, popupDebug!!, stretchX, stretchY, content)
            lifecycleScope.launch(Dispatchers.IO) {
                val feature = loadFeatureTask(this@StretchableImageActivity)
                withContext(Dispatchers.Main) {
                    onFeatureLoaded(feature)
                }
            }
        }
    }

    private fun onFeatureLoaded(json: String?) {
        if (json == null) {
            Timber.e("json is null.")
            return
        }
        val style = trackasiaMap.style
        if (style != null) {
            val featureCollection = FeatureCollection.fromJson(json)
            val stretchSource = GeoJsonSource(STRETCH_SOURCE, featureCollection)
            val stretchLayer =
                SymbolLayer(STRETCH_LAYER, STRETCH_SOURCE)
                    .withProperties(
                        PropertyFactory.textField(Expression.get("name")),
                        PropertyFactory.iconImage(Expression.get("image-name")),
                        PropertyFactory.iconAllowOverlap(true),
                        PropertyFactory.textAllowOverlap(true),
                        PropertyFactory.iconTextFit(Property.ICON_TEXT_FIT_BOTH),
                    )

            // the original, unstretched image for comparison
            val point = Point.fromLngLat(-70.0, 0.0)
            val feature = Feature.fromGeometry(point)
            val originalCollection = FeatureCollection.fromFeature(feature)
            val originalSource = GeoJsonSource(ORIGINAL_SOURCE, originalCollection)
            val originalLayer = SymbolLayer(ORIGINAL_LAYER, ORIGINAL_SOURCE)
            style.addSource(stretchSource)
            style.addSource(originalSource)
            style.addLayer(stretchLayer)
            style.addLayer(originalLayer)
        }
    }

    private fun loadFeatureTask(activity: StretchableImageActivity): String? {
        try {
            return GeoParseUtil.loadStringFromAssets(
                activity.applicationContext,
                "stretchable_image.geojson",
            )
        } catch (exception: IOException) {
            Timber.e(exception, "Could not read feature")
        }
        return null
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
        private const val NAME_POPUP = "popup"
        private const val NAME_POPUP_DEBUG = "popup-debug"
        private const val STRETCH_SOURCE = "STRETCH_SOURCE"
        private const val STRETCH_LAYER = "STRETCH_LAYER"
        private const val ORIGINAL_SOURCE = "ORIGINAL_SOURCE"
        private const val ORIGINAL_LAYER = "ORIGINAL_LAYER"
    }
}
