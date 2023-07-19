package com.trackasia.android.testapp.utils

import android.widget.TextView
import com.trackasia.android.maps.TrackasiaMap
import com.trackasia.android.maps.TrackasiaMap.OnCameraIdleListener
import com.trackasia.android.testapp.R

class IdleZoomListener(private val trackasiaMap: TrackasiaMap, private val textView: TextView) :
    OnCameraIdleListener {
    override fun onCameraIdle() {
        val context = textView.context
        val position = trackasiaMap.cameraPosition
        textView.text = String.format(context.getString(R.string.debug_zoom), position.zoom)
    }
}
