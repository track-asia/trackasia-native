package com.trackasia.android.location.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.location.Location
import android.os.Handler
import android.os.Looper
import com.trackasia.android.geometry.LatLng
import com.trackasia.android.maps.TrackAsiaMap
import com.trackasia.android.style.layers.Property
import com.trackasia.android.style.sources.GeoJsonSource
import com.trackasia.geojson.Feature

fun TrackAsiaMap.querySourceFeatures(sourceId: String): List<Feature> =
    this.style!!.getSourceAs<GeoJsonSource>(sourceId)?.querySourceFeatures(null) ?: emptyList()

fun TrackAsiaMap.queryRenderedFeatures(
    location: Location,
    layerId: String,
): List<Feature> {
    val latLng = LatLng(location.latitude, location.longitude)
    val point = this.projection.toScreenLocation(latLng)
    return this.queryRenderedFeatures(point, layerId)
}

fun TrackAsiaMap.isLayerVisible(layerId: String): Boolean =
    this.style!!
        .getLayer(layerId)
        ?.visibility
        ?.value
        ?.equals(Property.VISIBLE)!!

class TrackAsiaTestingUtils {
    companion object {
        /**
         * Used to increase style load time for stress testing.
         */
        const val MAPBOX_HEAVY_STYLE = "asset://heavy_style.json"

        private const val DATA_PUSH_INTERVAL = 1L

        /**
         * Pushes data updates every [DATA_PUSH_INTERVAL] milliseconds until the style has been loaded,
         * checked with [StyleChangeIdlingResource].
         */
        fun pushSourceUpdates(
            styleChangeIdlingResource: StyleChangeIdlingResource,
            update: () -> Unit,
        ) {
            val mainHandler = Handler(Looper.getMainLooper())
            val runnable =
                object : Runnable {
                    override fun run() {
                        update.invoke()
                        if (!styleChangeIdlingResource.isIdleNow) {
                            mainHandler.postDelayed(this, DATA_PUSH_INTERVAL)
                        }
                    }
                }

            if (!styleChangeIdlingResource.isIdleNow) {
                if (Looper.myLooper() == Looper.getMainLooper()) {
                    runnable.run()
                } else {
                    mainHandler.post(runnable)
                }
            }
        }
    }
}

fun TrackAsiaMap.addImageFromDrawable(
    string: String,
    drawable: Drawable,
) {
    val bitmapFromDrawable = getBitmapFromDrawable(drawable)
    this.style!!.addImage(string, bitmapFromDrawable)
}

private fun getBitmapFromDrawable(drawable: Drawable): Bitmap {
    if (drawable is BitmapDrawable) return drawable.bitmap
    val bitmap =
        Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888,
        )
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)
    return bitmap
}
