package com.trackasia.android.testapp.activity.annotation

import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.Point
import okhttp3.Call
import okhttp3.Callback
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import com.trackasia.android.Trackasia
import com.trackasia.android.annotations.IconFactory
import com.trackasia.android.annotations.MarkerOptions
import com.trackasia.android.camera.CameraPosition
import com.trackasia.android.geometry.LatLng
import com.trackasia.android.geometry.LatLngBounds
import com.trackasia.android.maps.MapLibreMap
import com.trackasia.android.maps.MapView
import com.trackasia.android.testapp.R
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Locale

/* ANCHOR: top */
class JsonApiActivity : AppCompatActivity() {

    // Declare a variable for MapView
    private lateinit var mapView: MapView

    // Declare a variable for MapboxMap
    private lateinit var trackasiaMap: MapLibreMap
    /* ANCHOR_END: top */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Init Trackasia
        Trackasia.getInstance(this)

        // Init layout view
        setContentView(R.layout.activity_json_api)

        // Init the MapView
        mapView = findViewById(R.id.mapView)

        /* ANCHOR: mapAsync */
        mapView.getMapAsync { map ->
            trackasiaMap = map

            trackasiaMap.setStyle("https://demotiles.trackasia.org/style.json")

            // Fetch data from USGS
            getEarthQuakeDataFromUSGS()
        }
        /* ANCHOR_END: mapAsync */
    }

    /* ANCHOR: getEarthquakes */
    // Get Earthquake data from usgs.gov, read API doc at:
    // https://earthquake.usgs.gov/fdsnws/event/1/
    private fun getEarthQuakeDataFromUSGS() {
        val url = "https://earthquake.usgs.gov/fdsnws/event/1/query".toHttpUrl().newBuilder()
            .addQueryParameter("format", "geojson")
            .addQueryParameter("starttime", "2022-01-01")
            .addQueryParameter("endtime", "2022-12-31")
            .addQueryParameter("minmagnitude", "5.8")
            .addQueryParameter("latitude", "24")
            .addQueryParameter("longitude", "121")
            .addQueryParameter("maxradius", "1.5")
            .build()
        val request: Request = Request.Builder().url(url).build()

        OkHttpClient().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Toast.makeText(this@JsonApiActivity, "Fail to fetch data", Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onResponse(call: Call, response: Response) {
                val featureCollection = response.body?.string()
                    ?.let(FeatureCollection::fromJson)
                    ?: return
                // If FeatureCollection in response is not null
                // Then add markers to map
                runOnUiThread { addMarkersToMap(featureCollection) }
            }
        })
    }
    /* ANCHOR_END: getEarthquakes */

    /* ANCHOR: addMarkers */
    private fun addMarkersToMap(data: FeatureCollection) {
        val bounds = mutableListOf<LatLng>()

        // Get bitmaps for marker icon
        val infoIconDrawable = ResourcesCompat.getDrawable(
            this.resources,
            // Intentionally specify package name
            // This makes copy from another project easier
            com.trackasia.android.R.drawable.trackasia_info_icon_default,
            null
        )!!
        val bitmapBlue = infoIconDrawable.toBitmap()
        val bitmapRed = infoIconDrawable
            .mutate()
            .apply { setTint(Color.RED) }
            .toBitmap()

        // Add symbol for each point feature
        data.features()?.forEach { feature ->
            val geometry = feature.geometry()?.toJson() ?: return@forEach
            val point = Point.fromJson(geometry) ?: return@forEach
            val latLng = LatLng(point.latitude(), point.longitude())
            bounds.add(latLng)

            // Contents in InfoWindow of each marker
            val title = feature.getStringProperty("title")
            val epochTime = feature.getNumberProperty("time")
            val dateString = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.TAIWAN).format(epochTime)

            // If magnitude > 6.0, show marker with red icon. If not, show blue icon instead
            val mag = feature.getNumberProperty("mag")
            val icon = IconFactory.getInstance(this)
                .fromBitmap(if (mag.toFloat() > 6.0) bitmapRed else bitmapBlue)

            // Use MarkerOptions and addMarker() to add a new marker in map
            val markerOptions = MarkerOptions()
                .position(latLng)
                .title(dateString)
                .snippet(title)
                .icon(icon)
            trackasiaMap.addMarker(markerOptions)
        }

        // Move camera to newly added annotations
        trackasiaMap.getCameraForLatLngBounds(LatLngBounds.fromLatLngs(bounds))?.let {
            val newCameraPosition = CameraPosition.Builder()
                .target(it.target)
                .zoom(it.zoom - 0.5)
                .build()
            trackasiaMap.cameraPosition = newCameraPosition
        }
    }
    /* ANCHOR_END: addMarkers */

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
}