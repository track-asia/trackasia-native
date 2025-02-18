package com.trackasia.android.testapp.activity.style

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.trackasia.android.TrackAsia
import com.trackasia.android.geometry.LatLng
import com.trackasia.android.geometry.LatLngQuad
import com.trackasia.android.maps.MapView
import com.trackasia.android.maps.OnMapReadyCallback
import com.trackasia.android.maps.Style
import com.trackasia.android.maps.TrackAsiaMap
import com.trackasia.android.style.layers.RasterLayer
import com.trackasia.android.style.sources.ImageSource
import com.trackasia.android.testapp.R
import com.trackasia.android.testapp.styles.TestStyles
import com.trackasia.android.utils.BitmapUtils

/**
 * Test activity showing how to use a series of images to create an animation
 * with an ImageSource
 *
 *
 * TrackAsia Native equivalent of https://trackasia.com/trackasia-gl-js/docs/examples/animate-images/
 *
 */
class AnimatedImageSourceActivity :
    AppCompatActivity(),
    OnMapReadyCallback {
    private lateinit var mapView: MapView
    private val handler = Handler(Looper.getMainLooper())
    private var runnable: Runnable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animated_image_source)
        mapView = findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
    }

    override fun onMapReady(map: TrackAsiaMap) {
        // --8<-- [start:onMapReady]
        val quad =
            LatLngQuad(
                LatLng(46.437, -80.425),
                LatLng(46.437, -71.516),
                LatLng(37.936, -71.516),
                LatLng(37.936, -80.425),
            )
        val imageSource = ImageSource(ID_IMAGE_SOURCE, quad, R.drawable.southeast_radar_0)
        val layer = RasterLayer(ID_IMAGE_LAYER, ID_IMAGE_SOURCE)
        map.setStyle(
            Style
                .Builder()
                .fromUri(TestStyles.AMERICANA)
                .withSource(imageSource)
                .withLayer(layer),
        ) { style: Style? ->
            runnable = RefreshImageRunnable(imageSource, handler)
            runnable?.let {
                handler.postDelayed(it, 100)
            }
        }
        // --8<-- [end:onMapReady]
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    public override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    public override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
        runnable?.let {
            handler.removeCallbacks(it)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    private class RefreshImageRunnable internal constructor(
        private val imageSource: ImageSource,
        private val handler: Handler,
    ) : Runnable {
        private val drawables: Array<Bitmap?>
        private var drawableIndex: Int

        fun getBitmap(resourceId: Int): Bitmap? {
            val context = TrackAsia.getApplicationContext()
            val drawable = BitmapUtils.getDrawableFromRes(context, resourceId)
            if (drawable is BitmapDrawable) {
                return drawable.bitmap
            }
            return null
        }

        override fun run() {
            // --8<-- [start:setImage]
            imageSource.setImage(drawables[drawableIndex++]!!)
            if (drawableIndex > 3) {
                drawableIndex = 0
            }
            handler.postDelayed(this, 1000)
            // --8<-- [end:setImage]
        }

        init {
            drawables = arrayOfNulls(4)
            drawables[0] = getBitmap(R.drawable.southeast_radar_0)
            drawables[1] = getBitmap(R.drawable.southeast_radar_1)
            drawables[2] = getBitmap(R.drawable.southeast_radar_2)
            drawables[3] = getBitmap(R.drawable.southeast_radar_3)
            drawableIndex = 1
        }
    }

    companion object {
        private const val ID_IMAGE_SOURCE = "animated_image_source"
        private const val ID_IMAGE_LAYER = "animated_image_layer"
    }
}
