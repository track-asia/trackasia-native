package com.trackasia.android.testapp.activity.style

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.trackasia.android.geometry.LatLng
import com.trackasia.android.maps.MapView
import com.trackasia.android.maps.OnMapReadyCallback
import com.trackasia.android.maps.Style
import com.trackasia.android.maps.TrackAsiaMap
import com.trackasia.android.style.expressions.Expression
import com.trackasia.android.style.layers.Property
import com.trackasia.android.style.layers.PropertyFactory
import com.trackasia.android.style.layers.SymbolLayer
import com.trackasia.android.style.sources.GeoJsonSource
import com.trackasia.android.style.sources.Source
import com.trackasia.android.testapp.R
import com.trackasia.android.testapp.styles.TestStyles
import com.trackasia.android.testapp.utils.ResourceUtils.readRawResource
import com.trackasia.geojson.FeatureCollection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

/**
 * Test activity showcasing using a symbol generator that generates Bitmaps from Android SDK Views.
 */
class SymbolGeneratorActivity :
    AppCompatActivity(),
    OnMapReadyCallback {
    private lateinit var mapView: MapView
    private lateinit var trackasiaMap: TrackAsiaMap

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_symbol_generator)
        mapView = findViewById<View>(R.id.mapView) as MapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onMapReady(map: TrackAsiaMap) {
        trackasiaMap = map
        map.setStyle(TestStyles.getPredefinedStyleWithFallback("Outdoor")) { style: Style? ->
            addSymbolClickListener()
            lifecycleScope.launch(Dispatchers.IO) {
                val featureCollection = loadDataTask(this@SymbolGeneratorActivity)
                withContext(Dispatchers.Main) {
                    featureCollection?.let { onDataLoaded(it) }
                }
            }
        }
    }

    private fun addSymbolClickListener() {
        trackasiaMap.addOnMapClickListener { point: LatLng? ->
            val screenPoint =
                trackasiaMap.projection.toScreenLocation(
                    point!!,
                )
            val features = trackasiaMap.queryRenderedFeatures(screenPoint, LAYER_ID)
            if (!features.isEmpty()) {
                val feature = features[0]
                // validate symbol flicker regression for #13407
                val layer = trackasiaMap.style!!.getLayerAs<SymbolLayer>(LAYER_ID)
                layer!!.setProperties(
                    PropertyFactory.iconOpacity(
                        Expression.match(
                            Expression.get(FEATURE_ID),
                            Expression.literal(1.0f),
                            Expression.stop(feature.getStringProperty(FEATURE_ID), 0.3f),
                        ),
                    ),
                )
                Timber.v("Feature was clicked with data: %s", feature.toJson())
                Toast
                    .makeText(
                        this@SymbolGeneratorActivity,
                        "hello from: " + feature.getStringProperty(FEATURE_NAME),
                        Toast.LENGTH_LONG,
                    ).show()
            }
            false
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_generator_symbol, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_action_icon_overlap) {
            val layer = trackasiaMap.style!!.getLayerAs<SymbolLayer>(LAYER_ID)
            layer!!.setProperties(PropertyFactory.iconAllowOverlap(!layer.iconAllowOverlap.getValue()!!))
            return true
        } else if (item.itemId == R.id.menu_action_filter) {
            val layer = trackasiaMap.style!!.getLayerAs<SymbolLayer>(LAYER_ID)
            layer!!.setFilter(Expression.eq(Expression.get(FEATURE_RANK), Expression.literal(1)))
            Timber.e("Filter that was set: %s", layer.filter)
            return true
        }
        return super.onOptionsItemSelected(item)
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

    /**
     * Utility class to generate Bitmaps for Symbol.
     *
     *
     * Bitmaps can be added to the map with [com.trackasia.android.maps.Style.addImage]
     *
     */
    private object SymbolGenerator {
        /**
         * Generate a Bitmap from an Android SDK View.
         *
         * @param view the View to be drawn to a Bitmap
         * @return the generated bitmap
         */
        fun generate(view: View): Bitmap {
            val measureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            view.measure(measureSpec, measureSpec)
            val measuredWidth = view.measuredWidth
            val measuredHeight = view.measuredHeight
            view.layout(0, 0, measuredWidth, measuredHeight)
            val bitmap = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888)
            bitmap.eraseColor(Color.TRANSPARENT)
            val canvas = Canvas(bitmap)
            view.draw(canvas)
            return bitmap
        }
    }

    private fun loadDataTask(context: SymbolGeneratorActivity): FeatureCollection? {
        // read local geojson from raw folder
        val tinyCountriesJson = readRawResource(context, R.raw.tiny_countries)
        return FeatureCollection.fromJson(tinyCountriesJson)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun onDataLoaded(featureCollection: FeatureCollection) {
        if (mapView.isDestroyed) {
            return
        }

        // create expressions
        val iconImageExpression = Expression.string(Expression.get(Expression.literal(FEATURE_ID)))
        val iconSizeExpression =
            Expression.division(
                Expression.number(
                    Expression.get(
                        Expression.literal(
                            FEATURE_RANK,
                        ),
                    ),
                ),
                Expression.literal(2.0f),
            )
        val textSizeExpression =
            Expression.product(
                Expression.get(
                    Expression.literal(
                        FEATURE_RANK,
                    ),
                ),
                Expression.pi(),
            )
        val textFieldExpression =
            Expression.concat(
                Expression.upcase(Expression.literal("a ")),
                Expression.upcase(
                    Expression.string(
                        Expression.get(Expression.literal(FEATURE_TYPE)),
                    ),
                ),
                Expression.downcase(Expression.literal(" IN ")),
                Expression.string(
                    Expression.get(
                        Expression.literal(
                            FEATURE_REGION,
                        ),
                    ),
                ),
            )
        val textColorExpression =
            Expression.match(
                Expression.get(Expression.literal(FEATURE_RANK)),
                Expression.literal(1),
                Expression.rgba(255, 0, 0, 1.0f),
                Expression.literal(2),
                Expression.rgba(0, 0, 255.0f, 1.0f),
                Expression.rgba(0.0f, 255.0f, 0.0f, 1.0f),
            )
        Expression.rgba(
            Expression.division(Expression.literal(255), Expression.get(FEATURE_RANK)),
            Expression.literal(0.0f),
            Expression.literal(0.0f),
            Expression.literal(1.0f),
        )

        // create symbol layer
        val symbolLayer =
            SymbolLayer(LAYER_ID, SOURCE_ID)
                .withProperties( // icon configuration
                    PropertyFactory.iconImage(iconImageExpression),
                    PropertyFactory.iconAllowOverlap(false),
                    PropertyFactory.iconSize(iconSizeExpression),
                    PropertyFactory.iconAnchor(Property.ICON_ANCHOR_BOTTOM),
                    PropertyFactory.iconOffset(
                        Expression.step(
                            Expression.zoom(),
                            Expression.literal(floatArrayOf(0f, 0f)),
                            Expression.stop(1, arrayOf(0f, 0f)),
                            Expression.stop(10, arrayOf(0f, -35f)),
                        ),
                    ), // text field configuration
                    PropertyFactory.textField(textFieldExpression),
                    PropertyFactory.textSize(textSizeExpression),
                    PropertyFactory.textAnchor(Property.TEXT_ANCHOR_TOP),
                    PropertyFactory.textColor(textColorExpression),
                )

        // add a geojson source to the map
        val source: Source = GeoJsonSource(SOURCE_ID, featureCollection)
        trackasiaMap.style!!.addSource(source)

        // add symbol layer
        trackasiaMap.style!!.addLayer(symbolLayer)

        // get expressions
        val iconImageExpressionResult = symbolLayer.iconImage.expression
        val iconSizeExpressionResult = symbolLayer.iconSize.expression
        val textSizeExpressionResult = symbolLayer.textSize.expression
        val textFieldExpressionResult = symbolLayer.textField.expression
        val textColorExpressionResult = symbolLayer.textColor.expression

        // log expressions
        Timber.e(iconImageExpressionResult.toString())
        Timber.e(iconSizeExpressionResult.toString())
        Timber.e(textSizeExpressionResult.toString())
        Timber.e(textFieldExpressionResult.toString())
        Timber.e(textColorExpressionResult.toString())

        // reset expressions
        symbolLayer.setProperties(
            PropertyFactory.iconImage(iconImageExpressionResult),
            PropertyFactory.iconSize(iconSizeExpressionResult),
            PropertyFactory.textSize(textSizeExpressionResult),
            PropertyFactory.textField(textFieldExpressionResult),
            PropertyFactory.textColor(textColorExpressionResult),
        )
        lifecycleScope.launch(Dispatchers.IO) {
            val bitmapHashMap = generateSymbolTask(this@SymbolGeneratorActivity, featureCollection)
            withContext(Dispatchers.Main) {
                trackasiaMap.getStyle { style -> style.addImagesAsync(bitmapHashMap) }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun generateSymbolTask(
        context: Context,
        featureCollection: FeatureCollection?,
    ): HashMap<String, Bitmap> {
        val imagesMap = HashMap<String, Bitmap>()
        featureCollection?.features()?.forEach {
            val countryName = it.getStringProperty(FEATURE_ID)
            val textView = TextView(context)
            textView.setBackgroundColor(context.resources.getColor(R.color.blueAccent, null))
            textView.setPadding(10, 5, 10, 5)
            textView.setTextColor(Color.WHITE)
            textView.text = countryName
            imagesMap[countryName] = SymbolGenerator.generate(textView)
        }
        return imagesMap
    }

    companion object {
        private const val SOURCE_ID = "com.mapbox.mapboxsdk.style.layers.symbol.source.id"
        private const val LAYER_ID = "com.mapbox.mapboxsdk.style.layers.symbol.layer.id"
        private const val FEATURE_ID = "brk_name"
        private const val FEATURE_RANK = "scalerank"
        private const val FEATURE_NAME = "name_sort"
        private const val FEATURE_TYPE = "type"
        private const val FEATURE_REGION = "continent"
    }
}
