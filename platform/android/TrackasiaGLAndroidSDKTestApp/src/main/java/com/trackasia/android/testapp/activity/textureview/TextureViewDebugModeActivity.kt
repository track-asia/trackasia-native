package com.trackasia.android.testapp.activity.textureview

import com.trackasia.android.maps.TrackasiaMapOptions
import com.trackasia.android.maps.OnMapReadyCallback
import com.trackasia.android.testapp.activity.maplayout.DebugModeActivity
import com.trackasia.android.testapp.utils.NavUtils

/**
 * Test activity showcasing the different debug modes and allows to cycle between the default map styles.
 */
class TextureViewDebugModeActivity : DebugModeActivity(), OnMapReadyCallback {
    override fun setupTrackasiaMapOptions(): TrackasiaMapOptions {
        val mapboxMapOptions = super.setupTrackasiaMapOptions()
        mapboxMapOptions.textureMode(true)
        return mapboxMapOptions
    }

    override fun onBackPressed() {
        // activity uses singleInstance for testing purposes
        // code below provides a default navigation when using the app
        NavUtils.navigateHome(this)
    }
}
