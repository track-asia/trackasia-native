package org.trackasia.android.testapp.activity.textureview

import org.trackasia.android.maps.OnMapReadyCallback
import org.trackasia.android.maps.TrackAsiaMapOptions
import org.trackasia.android.testapp.activity.maplayout.DebugModeActivity
import org.trackasia.android.testapp.utils.NavUtils

/**
 * Test activity showcasing the different debug modes and allows to cycle between the default map styles.
 */
class TextureViewDebugModeActivity : DebugModeActivity(), OnMapReadyCallback {
    override fun setupTrackAsiaMapOptions(): TrackAsiaMapOptions {
        val trackasiaMapOptions = super.setupTrackAsiaMapOptions()
        trackasiaMapOptions.textureMode(true)
        return trackasiaMapOptions
    }

    override fun onBackPressed() {
        // activity uses singleInstance for testing purposes
        // code below provides a default navigation when using the app
        NavUtils.navigateHome(this)
    }
}
