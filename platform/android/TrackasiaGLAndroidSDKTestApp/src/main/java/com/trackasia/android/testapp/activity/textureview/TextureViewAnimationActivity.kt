package com.trackasia.android.testapp.activity.textureview

import android.animation.ObjectAnimator
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.trackasia.android.camera.CameraUpdateFactory
import com.trackasia.android.geometry.LatLng
import com.trackasia.android.maps.*
import com.trackasia.android.maps.TrackasiaMap.CancelableCallback
import com.trackasia.android.testapp.R
import java.util.*

/**
 * Test animating a [android.view.TextureView] backed map.
 */
class TextureViewAnimationActivity : AppCompatActivity() {
    private lateinit var mapView: MapView
    private lateinit var mapboxMap: TrackasiaMap
    private var handler: Handler? = null
    private var delayed: Runnable? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_textureview_animate)
        handler = Handler(mainLooper)
        setupToolbar()
        setupMapView(savedInstanceState)
    }

    private fun setupToolbar() {
        val actionBar = supportActionBar
        if (actionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setHomeButtonEnabled(true)
        }
    }

    private fun setupMapView(savedInstanceState: Bundle?) {
        mapView = findViewById<View>(R.id.mapView) as MapView
        mapView.getMapAsync { mapboxMap: TrackasiaMap ->
            this@TextureViewAnimationActivity.mapboxMap = mapboxMap
            mapboxMap.setStyle(Style.getPredefinedStyle("Streets"))
            setFpsView(mapboxMap)

            // Animate the map view
            val animation = ObjectAnimator.ofFloat(mapView!!, "rotationY", 0.0f, 360f)
            animation.duration = 3600
            animation.repeatCount = ObjectAnimator.INFINITE
            animation.start()

            // Start an animation on the map as well
            flyTo(mapboxMap, 0, 14.0)
        }
    }

    private fun flyTo(mapboxMap: TrackasiaMap, place: Int, zoom: Double) {
        mapboxMap.animateCamera(
            CameraUpdateFactory.newLatLngZoom(PLACES[place], zoom),
            10000,
            object : CancelableCallback {
                override fun onCancel() {
                    delayed = Runnable {
                        delayed = null
                        flyTo(mapboxMap, place, zoom)
                    }
                    delayed?.let {
                        handler!!.postDelayed(it, 2000)
                    }
                }

                override fun onFinish() {
                    flyTo(mapboxMap, if (place == PLACES.size - 1) 0 else place + 1, zoom)
                }
            }
        )
    }

    private fun setFpsView(mapboxMap: TrackasiaMap) {
        val fpsView = findViewById<View>(R.id.fpsView) as TextView
        mapboxMap.setOnFpsChangedListener { fps: Double ->
            fpsView.text = String.format(Locale.US, "FPS: %4.2f", fps)
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
        if (handler != null && delayed != null) {
            handler!!.removeCallbacks(delayed!!)
        }
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

    companion object {
        private val PLACES = arrayOf(
            LatLng(37.7749, -122.4194), // SF
            LatLng(38.9072, -77.0369), // DC
            LatLng(52.3702, 4.8952), // AMS
            LatLng(60.1699, 24.9384), // HEL
            LatLng(-13.1639, -74.2236), // AYA
            LatLng(52.5200, 13.4050), // BER
            LatLng(12.9716, 77.5946), // BAN
            LatLng(31.2304, 121.4737) // SHA
        )
    }
}
