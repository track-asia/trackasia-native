package org.trackasia.android.testapp.utils

import android.widget.TextView
import org.trackasia.android.maps.trackasiaMap
import org.trackasia.android.maps.trackasiaMap.OnCameraIdleListener
import org.trackasia.android.testapp.R

class IdleZoomListener(private val trackasiaMap: trackasiaMap, private val textView: TextView) :
    OnCameraIdleListener {
    override fun onCameraIdle() {
        val context = textView.context
        val position = trackasiaMap.cameraPosition
        textView.text = String.format(context.getString(R.string.debug_zoom), position.zoom)
    }
}
