package com.trackasia.android.testapp.activity.camera

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.trackasia.android.geometry.LatLng
import com.trackasia.android.geometry.LatLngBounds
import com.trackasia.android.maps.Style
import com.trackasia.android.maps.TrackAsiaMap
import com.trackasia.android.style.layers.Property.ICON_ANCHOR_CENTER
import com.trackasia.android.style.layers.PropertyFactory.*
import com.trackasia.android.style.layers.SymbolLayer
import com.trackasia.android.style.sources.GeoJsonSource
import com.trackasia.android.testapp.R
import com.trackasia.android.testapp.databinding.ActivityLatlngboundsBinding
import com.trackasia.android.testapp.styles.TestStyles
import com.trackasia.android.testapp.utils.GeoParseUtil
import com.trackasia.android.utils.BitmapUtils
import com.trackasia.geojson.FeatureCollection
import com.trackasia.geojson.FeatureCollection.fromJson
import com.trackasia.geojson.Point
import java.net.URISyntaxException

/** Test activity showcasing using the LatLngBounds camera API. */
class LatLngBoundsActivity : AppCompatActivity() {
    private lateinit var trackasiaMap: TrackAsiaMap
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<*>
    private lateinit var bounds: LatLngBounds
    private lateinit var binding: ActivityLatlngboundsBinding

    private val peekHeight by lazy {
        375.toPx(this) // 375dp
    }

    private val additionalPadding by lazy {
        32.toPx(this) // 32dp
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLatlngboundsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        initMapView(savedInstanceState)
    }

    private fun initMapView(savedInstanceState: Bundle?) {
        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.getMapAsync { map ->
            trackasiaMap = map

            // # --8<-- [start:featureCollection]
            val featureCollection: FeatureCollection =
                fromJson(GeoParseUtil.loadStringFromAssets(this, "points-sf.geojson"))
            bounds = createBounds(featureCollection)

            map.getCameraForLatLngBounds(bounds, createPadding(peekHeight))?.let {
                map.cameraPosition = it
            }
            // # --8<-- [end:featureCollection]

            try {
                loadStyle(featureCollection)
            } catch (e: URISyntaxException) {
                e.printStackTrace()
            }
        }
    }

    private fun loadStyle(featureCollection: FeatureCollection) {
        trackasiaMap.setStyle(
            Style
                .Builder()
                .fromUri(TestStyles.VERSATILES)
                .withLayer(
                    SymbolLayer("symbol", "symbol")
                        .withProperties(
                            iconAllowOverlap(true),
                            iconIgnorePlacement(true),
                            iconImage("icon"),
                            iconAnchor(ICON_ANCHOR_CENTER),
                        ),
                ).withSource(GeoJsonSource("symbol", featureCollection))
                .withImage(
                    "icon",
                    BitmapUtils.getDrawableFromRes(
                        this@LatLngBoundsActivity,
                        R.drawable.ic_android,
                    )!!,
                ),
        ) {
            initBottomSheet()
            binding.fab.setOnClickListener {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
    }

    private fun initBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet)
        bottomSheetBehavior.addBottomSheetCallback(
            object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onSlide(
                    bottomSheet: View,
                    slideOffset: Float,
                ) {
                    val offset = convertSlideOffset(slideOffset)
                    val bottomPadding = (peekHeight * offset).toInt()

                    trackasiaMap
                        .getCameraForLatLngBounds(bounds, createPadding(bottomPadding))
                        ?.let { trackasiaMap.cameraPosition = it }
                }

                override fun onStateChanged(
                    bottomSheet: View,
                    newState: Int,
                ) {
                    // no-op
                }
            },
        )
    }

    // slideOffset ranges from NaN to -1.0, range from 1.0 to 0 instead
    fun convertSlideOffset(slideOffset: Float): Float =
        if (slideOffset.equals(Float.NaN)) {
            1.0f
        } else {
            1 + slideOffset
        }

    fun createPadding(bottomPadding: Int): IntArray = intArrayOf(additionalPadding, additionalPadding, additionalPadding, bottomPadding)

    // # --8<-- [start:createBounds]
    private fun createBounds(featureCollection: FeatureCollection): LatLngBounds {
        val boundsBuilder = LatLngBounds.Builder()
        featureCollection.features()?.let {
            for (feature in it) {
                val point = feature.geometry() as Point
                boundsBuilder.include(LatLng(point.latitude(), point.longitude()))
            }
        }
        return boundsBuilder.build()
    }
    // # --8<-- [end:createBounds]

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
    }
}

fun Int.toPx(context: Context): Int = (this * context.resources.displayMetrics.density).toInt()
