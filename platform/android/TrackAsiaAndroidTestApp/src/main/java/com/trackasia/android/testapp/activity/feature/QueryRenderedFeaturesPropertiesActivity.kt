package com.trackasia.android.testapp.activity.feature

import android.graphics.Color
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.trackasia.android.annotations.BaseMarkerOptions
import com.trackasia.android.annotations.Marker
import com.trackasia.android.maps.MapView
import com.trackasia.android.maps.Style
import com.trackasia.android.maps.TrackAsiaMap
import com.trackasia.android.maps.TrackAsiaMap.InfoWindowAdapter
import com.trackasia.android.maps.TrackAsiaMap.OnMapClickListener
import com.trackasia.android.testapp.R
import com.trackasia.android.testapp.styles.TestStyles
import com.trackasia.geojson.Feature
import timber.log.Timber

/**
 * Test activity showcasing using the query rendered features API to query feature properties on Map click.
 */
class QueryRenderedFeaturesPropertiesActivity : AppCompatActivity() {
    lateinit var mapView: MapView
    lateinit var trackasiaMap: TrackAsiaMap
        private set
    private var marker: Marker? = null
    private val mapClickListener =
        OnMapClickListener { point ->
            val density = resources.displayMetrics.density
            val pixel = trackasiaMap.projection.toScreenLocation(point)
            Timber.i(
                "Requesting features for %sx%s (%sx%s adjusted for density)",
                pixel.x,
                pixel.y,
                pixel.x / density,
                pixel.y / density,
            )
            val features = trackasiaMap.queryRenderedFeatures(pixel)

            // Debug output
            debugOutput(features)

            // Remove any previous markers
            if (marker != null) {
                trackasiaMap.removeMarker(marker!!)
            }

            // Add a marker on the clicked point
            marker = trackasiaMap.addMarker(CustomMarkerOptions().position(point)!!.features(features))
            trackasiaMap.selectMarker(marker!!)
            true
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_query_features_point)

        // Initialize map as normal
        mapView = findViewById<View>(R.id.mapView) as MapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync { trackasiaMap: TrackAsiaMap ->
            trackasiaMap.setStyle(TestStyles.getPredefinedStyleWithFallback("Streets")) { style: Style? ->
                this@QueryRenderedFeaturesPropertiesActivity.trackasiaMap = trackasiaMap

                // Add custom window adapter
                addCustomInfoWindowAdapter(trackasiaMap)

                // Add a click listener
                trackasiaMap.addOnMapClickListener(mapClickListener)
            }
        }
    }

    private fun debugOutput(features: List<Feature>) {
        Timber.i("Got %s features", features.size)
        for (feature in features) {
            Timber.i(
                "Got feature %s with %s properties and Geometry %s",
                feature.id(),
                if (feature.properties() != null) {
                    feature
                        .properties()!!
                        .entrySet()
                        .size
                } else {
                    "<null>"
                },
                if (feature.geometry() != null) {
                    feature.geometry()!!::class.java.simpleName
                } else {
                    "<null>"
                },
            )
            if (feature.properties() != null) {
                for ((key, value) in feature.properties()!!.entrySet()) {
                    Timber.i("Prop %s - %s", key, value)
                }
            }
        }
    }

    private fun addCustomInfoWindowAdapter(trackasiaMap: TrackAsiaMap) {
        trackasiaMap.infoWindowAdapter =
            object : InfoWindowAdapter {
                private fun row(text: String): TextView {
                    val view = TextView(this@QueryRenderedFeaturesPropertiesActivity)
                    view.text = text
                    return view
                }

                override fun getInfoWindow(marker: Marker): View? {
                    val customMarker = marker as CustomMarker
                    val view = LinearLayout(this@QueryRenderedFeaturesPropertiesActivity)
                    view.orientation = LinearLayout.VERTICAL
                    view.setBackgroundColor(Color.WHITE)
                    if (customMarker.features!!.size > 0) {
                        view.addView(
                            row(
                                String.format(
                                    "Found %s features",
                                    customMarker.features.size,
                                ),
                            ),
                        )
                        val feature = customMarker.features[0]
                        for ((key, value) in feature.properties()!!.entrySet()) {
                            view.addView(row(String.format("%s: %s", key, value)))
                        }
                    } else {
                        view.addView(row("No features here"))
                    }
                    return view
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (this::trackasiaMap.isInitialized) {
            trackasiaMap.removeOnMapClickListener(mapClickListener)
        }
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    private class CustomMarker constructor(
        baseMarkerOptions: BaseMarkerOptions<*, *>?,
        val features: List<Feature>?,
    ) : Marker(baseMarkerOptions)

    private class CustomMarkerOptions : BaseMarkerOptions<CustomMarker?, CustomMarkerOptions?> {
        private var features: List<Feature>? = null

        fun features(features: List<Feature>?): CustomMarkerOptions {
            this.features = features
            return this
        }

        internal constructor() {}
        private constructor(`in`: Parcel) {
            // Should implement this
        }

        override fun getThis(): CustomMarkerOptions = this

        override fun getMarker(): CustomMarker = CustomMarker(this, features)

        override fun describeContents(): Int = 0

        override fun writeToParcel(
            out: Parcel,
            flags: Int,
        ) {
            // Should implement this
        }

        companion object {
            @JvmField
            val CREATOR: Parcelable.Creator<CustomMarkerOptions?> =
                object : Parcelable.Creator<CustomMarkerOptions?> {
                    override fun createFromParcel(`in`: Parcel): CustomMarkerOptions? = CustomMarkerOptions(`in`)

                    override fun newArray(size: Int): Array<CustomMarkerOptions?> = arrayOfNulls(size)
                }
        }
    }
}
