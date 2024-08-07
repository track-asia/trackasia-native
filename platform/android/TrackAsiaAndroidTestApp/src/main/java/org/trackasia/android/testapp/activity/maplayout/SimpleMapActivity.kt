package org.trackasia.android.testapp.activity.maplayout

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import org.trackasia.android.maps.*
import org.trackasia.android.testapp.R
import org.trackasia.android.testapp.utils.ApiKeyUtils
import org.trackasia.android.testapp.utils.NavUtils

/**
 * Test activity showcasing a simple MapView without any TrackAsiaMap interaction.
 */
class SimpleMapActivity : AppCompatActivity() {
    private lateinit var mapView: MapView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_simple)
        mapView = findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(
            OnMapReadyCallback { trackasiaMap: TrackAsiaMap ->
                var key = ApiKeyUtils.getApiKey(applicationContext)
                if (key == null || key == "YOUR_API_KEY_GOES_HERE") {
                    trackasiaMap.setStyle(Style.Builder().fromUri("https://maps.track-asia.com/styles/v1/streets.json?key=public_key"))
                } else {
                    val styles = Style.getPredefinedStyles()
                    if (styles != null && styles.size > 0) {
                        val styleUrl = styles[0].url
                        trackasiaMap.setStyle(Style.Builder().fromUri(styleUrl))
                    }
                }
            }
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

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                // activity uses singleInstance for testing purposes
                // code below provides a default navigation when using the app
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        // activity uses singleInstance for testing purposes
        // code below provides a default navigation when using the app
        NavUtils.navigateHome(this)
    }
}
