package org.trackasia.android.testapp.activity.storage

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import org.trackasia.android.ModuleProvider
import org.trackasia.android.ModuleProviderImpl
import org.trackasia.android.maps.MapView
import org.trackasia.android.TrackAsia
import org.trackasia.android.maps.TrackAsiaMap
import org.trackasia.android.maps.OnMapReadyCallback
import org.trackasia.android.maps.Style
import org.trackasia.android.storage.FileSource
import org.trackasia.android.storage.FileSource.ResourceTransformCallback
import org.trackasia.android.storage.Resource
import org.trackasia.android.testapp.R
import org.trackasia.android.testapp.utils.ApiKeyUtils
import org.trackasia.android.testapp.utils.ExampleCustomModuleProviderImpl
import timber.log.Timber

/**
 * This example activity shows how to provide your own HTTP request implementation.
 */
class CustomHttpRequestImplActivity : AppCompatActivity() {
    private lateinit var mapView: MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_driven_style)

        // Set a custom module provider that provides our custom HTTPRequestImpl
        TrackAsia.setModuleProvider(ExampleCustomModuleProviderImpl() as ModuleProvider)

        // Initialize map with a style
        mapView = findViewById<View>(R.id.mapView) as MapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(
            OnMapReadyCallback { trackasiaMap: TrackAsiaMap ->
                trackasiaMap.setStyle(Style.Builder().fromUri("https://tiles.track-asia.com/tiles/v1/style-streets.json?key=publi"))
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()

        // Example of how to reset the module provider
        TrackAsia.setModuleProvider(ModuleProviderImpl())
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
}
