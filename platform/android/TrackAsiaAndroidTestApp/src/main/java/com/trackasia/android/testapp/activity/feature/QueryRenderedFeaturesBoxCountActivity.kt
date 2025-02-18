package com.trackasia.android.testapp.activity.feature

import android.graphics.PointF
import android.graphics.RectF
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.trackasia.android.maps.MapView
import com.trackasia.android.maps.Style
import com.trackasia.android.maps.TrackAsiaMap
import com.trackasia.android.testapp.R
import com.trackasia.android.testapp.styles.TestStyles
import com.trackasia.android.testapp.utils.NavUtils
import com.trackasia.geojson.Feature
import timber.log.Timber

/**
 * Test activity showcasing using the query rendered features API to count features in a rectangle.
 */
class QueryRenderedFeaturesBoxCountActivity : AppCompatActivity() {
    lateinit var mapView: MapView
    lateinit var trackasiaMap: TrackAsiaMap
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    // activity uses singleInstance for testing purposes
                    // code below provides a default navigation when using the app
                    NavUtils.navigateHome(this@QueryRenderedFeaturesBoxCountActivity)
                }
            },
        )
        setContentView(R.layout.activity_query_features_box)
        val selectionBox = findViewById<View>(R.id.selection_box)

        // Initialize map as normal
        mapView = findViewById<View>(R.id.mapView) as MapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync { trackasiaMap: TrackAsiaMap ->
            this@QueryRenderedFeaturesBoxCountActivity.trackasiaMap = trackasiaMap
            trackasiaMap.setStyle(Style.Builder().fromUri(TestStyles.AMERICANA))
            selectionBox.setOnClickListener { _: View? ->
                // Query
                val top = selectionBox.top - mapView.top
                val left = selectionBox.left - mapView.left
                val box =
                    RectF(
                        left.toFloat(),
                        top.toFloat(),
                        (left + selectionBox.width).toFloat(),
                        (top + selectionBox.height).toFloat(),
                    )
                Timber.i("Querying box %s", box)
                val features = trackasiaMap.queryRenderedFeatures(box)

                // Show count
                Toast
                    .makeText(
                        this@QueryRenderedFeaturesBoxCountActivity,
                        String.format("%s features in box", features.size),
                        Toast.LENGTH_SHORT,
                    ).show()

                // Debug output
                debugOutput(features)
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

    override fun onStart() {
        super.onStart()
        mapView.onStart()

        if (::trackasiaMap.isInitialized) {
            // Regression test for #14394
            trackasiaMap.queryRenderedFeatures(PointF(0F, 0F))
        }
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                // activity uses singleInstance for testing purposes
                // code below provides a default navigation when using the app
                NavUtils.navigateHome(this)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
