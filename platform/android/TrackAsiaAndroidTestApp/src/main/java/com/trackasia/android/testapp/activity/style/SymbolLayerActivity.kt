package com.trackasia.android.testapp.activity.style

import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import com.trackasia.android.camera.CameraPosition
import com.trackasia.android.geometry.LatLng
import com.trackasia.android.maps.MapView
import com.trackasia.android.maps.OnMapReadyCallback
import com.trackasia.android.maps.Style
import com.trackasia.android.maps.TrackAsiaMap
import com.trackasia.android.maps.TrackAsiaMap.OnMapClickListener
import com.trackasia.android.maps.TrackAsiaMapOptions
import com.trackasia.android.style.expressions.Expression
import com.trackasia.android.style.expressions.Expression.FormatEntry
import com.trackasia.android.style.expressions.Expression.FormatOption
import com.trackasia.android.style.expressions.Expression.NumberFormatOption
import com.trackasia.android.style.layers.Property
import com.trackasia.android.style.layers.PropertyFactory
import com.trackasia.android.style.layers.SymbolLayer
import com.trackasia.android.style.sources.GeoJsonSource
import com.trackasia.android.style.sources.Source
import com.trackasia.android.testapp.R
import com.trackasia.android.utils.BitmapUtils
import com.trackasia.geojson.Feature
import com.trackasia.geojson.FeatureCollection
import com.trackasia.geojson.Point
import timber.log.Timber
import java.util.Random

/**
 * Test activity showcasing runtime manipulation of symbol layers.
 *
 *
 * Showcases the ability to offline render a symbol layer by using a packaged style and fonts from the assets folder.
 *
 */
class SymbolLayerActivity :
    AppCompatActivity(),
    OnMapClickListener,
    OnMapReadyCallback {
    private val random = Random()
    private var markerSource: GeoJsonSource? = null
    private var markerCollection: FeatureCollection? = null
    private var markerSymbolLayer: SymbolLayer? = null
    private var mapboxSignSymbolLayer: SymbolLayer? = null
    private var numberFormatSymbolLayer: SymbolLayer? = null
    private lateinit var trackasiaMap: TrackAsiaMap
    private lateinit var mapView: MapView

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_symbollayer)

        // Create map configuration
        val trackasiaMapOptions = TrackAsiaMapOptions.createFromAttributes(this)
        trackasiaMapOptions.camera(
            CameraPosition
                .Builder()
                .target(
                    LatLng(52.35273, 4.91638),
                ).zoom(13.0)
                .build(),
        )

        // Create map programmatically, add to view hierarchy
        mapView = MapView(this, trackasiaMapOptions)
        mapView.getMapAsync(this)
        mapView.onCreate(savedInstanceState)
        (findViewById<View>(R.id.container) as ViewGroup).addView(mapView)

        // Use OnStyleImageMissing API to lazily load an icon
        mapView.addOnStyleImageMissingListener { id: String? ->
            val style = trackasiaMap.style
            if (style != null) {
                Timber.e("Adding image with id: %s", id)
                val androidIcon =
                    BitmapUtils.getBitmapFromDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_android_2, null))
                style.addImage(id!!, androidIcon!!)
            }
        }
    }

    override fun onMapReady(trackasiaMap: TrackAsiaMap) {
        this.trackasiaMap = trackasiaMap
        val carBitmap =
            BitmapUtils.getBitmapFromDrawable(
                ResourcesCompat.getDrawable(resources, R.drawable.ic_directions_car_black, null),
            )

        // marker source
        markerCollection =
            FeatureCollection.fromFeatures(
                arrayOf(
                    Feature.fromGeometry(
                        Point.fromLngLat(4.91638, 52.35673),
                        featureProperties("1", "Android"),
                    ),
                    Feature.fromGeometry(
                        Point.fromLngLat(4.91638, 52.34673),
                        featureProperties("2", "Car"),
                    ),
                ),
            )
        markerSource = GeoJsonSource(MARKER_SOURCE, markerCollection)

        // marker layer
        markerSymbolLayer =
            SymbolLayer(MARKER_LAYER, MARKER_SOURCE)
                .withProperties(
                    PropertyFactory.iconImage(Expression.get(TITLE_FEATURE_PROPERTY)),
                    PropertyFactory.iconIgnorePlacement(true),
                    PropertyFactory.iconAllowOverlap(true),
                    PropertyFactory.iconSize(
                        Expression.switchCase(
                            Expression.toBool(
                                Expression.get(
                                    SELECTED_FEATURE_PROPERTY,
                                ),
                            ),
                            Expression.literal(1.5f),
                            Expression.literal(1.0f),
                        ),
                    ),
                    PropertyFactory.iconAnchor(Property.ICON_ANCHOR_BOTTOM),
                    PropertyFactory.iconColor(Color.BLUE),
                    PropertyFactory.textField(TEXT_FIELD_EXPRESSION),
                    PropertyFactory.textFont(NORMAL_FONT_STACK),
                    PropertyFactory.textColor(Color.BLUE),
                    PropertyFactory.textAllowOverlap(true),
                    PropertyFactory.textIgnorePlacement(true),
                    PropertyFactory.textAnchor(Property.TEXT_ANCHOR_TOP),
                    PropertyFactory.textSize(10f),
                )

        // mapbox sign layer
        val mapboxSignSource: Source =
            GeoJsonSource(MAPBOX_SIGN_SOURCE, Point.fromLngLat(4.91638, 52.3510))
        mapboxSignSymbolLayer = SymbolLayer(MAPBOX_SIGN_LAYER, MAPBOX_SIGN_SOURCE)
        shuffleMapboxSign()

        // number format layer
        val numberFormatSource: Source =
            GeoJsonSource(NUMBER_FORMAT_SOURCE, Point.fromLngLat(4.92756, 52.3516))
        numberFormatSymbolLayer = SymbolLayer(NUMBER_FORMAT_LAYER, NUMBER_FORMAT_SOURCE)
        numberFormatSymbolLayer!!.setProperties(
            PropertyFactory.textField(
                Expression.numberFormat(
                    123.456789,
                    NumberFormatOption.locale("nl-NL"),
                    NumberFormatOption.currency("EUR"),
                ),
            ),
        )
        trackasiaMap.setStyle(
            Style
                .Builder()
                .fromUri("asset://streets.json")
                .withImage("Car", carBitmap!!, false)
                .withSources(markerSource, mapboxSignSource, numberFormatSource)
                .withLayers(markerSymbolLayer, mapboxSignSymbolLayer, numberFormatSymbolLayer),
        )

        // Set a click-listener so we can manipulate the map
        trackasiaMap.addOnMapClickListener(this@SymbolLayerActivity)
    }

    override fun onMapClick(point: LatLng): Boolean {
        // Query which features are clicked
        val screenLoc = trackasiaMap.projection.toScreenLocation(point)
        val markerFeatures = trackasiaMap.queryRenderedFeatures(screenLoc, MARKER_LAYER)
        if (!markerFeatures.isEmpty()) {
            for (feature in markerCollection!!.features()!!) {
                if (feature.getStringProperty(ID_FEATURE_PROPERTY)
                    == markerFeatures[0].getStringProperty(ID_FEATURE_PROPERTY)
                ) {
                    // use DDS
                    val selected = feature.getBooleanProperty(SELECTED_FEATURE_PROPERTY)
                    feature.addBooleanProperty(SELECTED_FEATURE_PROPERTY, !selected)

                    // validate symbol flicker regression for #13407
                    markerSymbolLayer!!.setProperties(
                        PropertyFactory.iconOpacity(
                            Expression.match(
                                Expression.get(ID_FEATURE_PROPERTY),
                                Expression.literal(1.0f),
                                Expression.stop(
                                    feature.getStringProperty("id"),
                                    if (selected) 0.3f else 1.0f,
                                ),
                            ),
                        ),
                    )
                }
            }
            markerSource!!.setGeoJson(markerCollection)
        } else {
            val mapboxSignFeatures = trackasiaMap.queryRenderedFeatures(screenLoc, MAPBOX_SIGN_LAYER)
            if (!mapboxSignFeatures.isEmpty()) {
                shuffleMapboxSign()
            }
        }
        return false
    }

    private fun toggleTextSize() {
        if (markerSymbolLayer != null) {
            val size: Number? = markerSymbolLayer!!.textSize.getValue()
            if (size != null) {
                markerSymbolLayer!!.setProperties(
                    if (size as Float > 10) {
                        PropertyFactory.textSize(10f)
                    } else {
                        PropertyFactory.textSize(20f)
                    },
                )
            }
        }
    }

    private fun toggleTextField() {
        if (markerSymbolLayer != null) {
            if (TEXT_FIELD_EXPRESSION == markerSymbolLayer!!.textField.expression) {
                markerSymbolLayer!!.setProperties(PropertyFactory.textField("āA"))
            } else {
                markerSymbolLayer!!.setProperties(PropertyFactory.textField(TEXT_FIELD_EXPRESSION))
            }
        }
    }

    private fun toggleTextFont() {
        if (markerSymbolLayer != null) {
            if (markerSymbolLayer!!.textFont.getValue().contentEquals(NORMAL_FONT_STACK)) {
                markerSymbolLayer!!.setProperties(PropertyFactory.textFont(BOLD_FONT_STACK))
            } else {
                markerSymbolLayer!!.setProperties(PropertyFactory.textFont(NORMAL_FONT_STACK))
            }
        }
    }

    private fun shuffleMapboxSign() {
        if (mapboxSignSymbolLayer != null) {
            mapboxSignSymbolLayer!!.setProperties(
                PropertyFactory.textField(
                    Expression.format(
                        Expression.formatEntry("M", FormatOption.formatFontScale(2.0)),
                        getRandomColorEntryForString("a"),
                        getRandomColorEntryForString("p"),
                        getRandomColorEntryForString("b"),
                        getRandomColorEntryForString("o"),
                        getRandomColorEntryForString("x"),
                    ),
                ),
                PropertyFactory.textColor(Color.BLACK),
                PropertyFactory.textFont(BOLD_FONT_STACK),
                PropertyFactory.textSize(25f),
                PropertyFactory.textRotationAlignment(Property.TEXT_ROTATION_ALIGNMENT_MAP),
            )
        }
    }

    private fun getRandomColorEntryForString(string: String): FormatEntry =
        Expression.formatEntry(
            string,
            FormatOption.formatTextColor(
                Expression.rgb(
                    random.nextInt(256),
                    random.nextInt(256),
                    random.nextInt(256),
                ),
            ),
        )

    private fun featureProperties(
        id: String,
        title: String,
    ): JsonObject {
        val `object` = JsonObject()
        `object`.add(ID_FEATURE_PROPERTY, JsonPrimitive(id))
        `object`.add(TITLE_FEATURE_PROPERTY, JsonPrimitive(title))
        `object`.add(SELECTED_FEATURE_PROPERTY, JsonPrimitive(false))
        return `object`
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
        if (this::trackasiaMap.isInitialized) {
            trackasiaMap.removeOnMapClickListener(this)
        }
        mapView.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_symbol_layer, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            R.id.action_toggle_text_size -> {
                toggleTextSize()
                true
            }

            R.id.action_toggle_text_field -> {
                toggleTextField()
                true
            }

            R.id.action_toggle_text_font -> {
                toggleTextFont()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }

    companion object {
        private const val ID_FEATURE_PROPERTY = "id"
        private const val SELECTED_FEATURE_PROPERTY = "selected"
        private const val TITLE_FEATURE_PROPERTY = "title"
        private val NORMAL_FONT_STACK = arrayOf("DIN Offc Pro Regular", "Arial Unicode MS Regular")
        private val BOLD_FONT_STACK = arrayOf("DIN Offc Pro Bold", "Arial Unicode MS Regular")

        // layer & source constants
        private const val MARKER_SOURCE = "marker-source"
        private const val MARKER_LAYER = "marker-layer"
        private const val MAPBOX_SIGN_SOURCE = "mapbox-sign-source"
        private const val MAPBOX_SIGN_LAYER = "mapbox-sign-layer"
        private const val NUMBER_FORMAT_SOURCE = "mapbox-number-source"
        private const val NUMBER_FORMAT_LAYER = "mapbox-number-layer"
        private val TEXT_FIELD_EXPRESSION =
            Expression.switchCase(
                Expression.toBool(Expression.get(SELECTED_FEATURE_PROPERTY)),
                Expression.format(
                    Expression.formatEntry(
                        Expression.get(TITLE_FEATURE_PROPERTY),
                        FormatOption.formatTextFont(BOLD_FONT_STACK),
                    ),
                    Expression.formatEntry("\nis fun!", FormatOption.formatFontScale(0.75)),
                ),
                Expression.format(
                    Expression.formatEntry("This is", FormatOption.formatFontScale(0.75)),
                    Expression.formatEntry(
                        Expression.concat(
                            Expression.literal("\n"),
                            Expression.get(TITLE_FEATURE_PROPERTY),
                        ),
                        FormatOption.formatFontScale(1.25),
                        FormatOption.formatTextFont(BOLD_FONT_STACK),
                    ),
                ),
            )
    }
}
