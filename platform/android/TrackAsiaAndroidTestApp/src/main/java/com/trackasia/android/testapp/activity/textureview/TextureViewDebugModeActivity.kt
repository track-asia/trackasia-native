package com.trackasia.android.testapp.activity.textureview

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import com.trackasia.android.maps.OnMapReadyCallback
import com.trackasia.android.maps.TrackAsiaMapOptions
import com.trackasia.android.testapp.activity.maplayout.DebugModeActivity
import com.trackasia.android.testapp.utils.NavUtils

/**
 * Test activity showcasing the different debug modes and allows to cycle between the default map styles.
 */
class TextureViewDebugModeActivity :
    DebugModeActivity(),
    OnMapReadyCallback {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    // activity uses singleInstance for testing purposes
                    // code below provides a default navigation when using the app
                    NavUtils.navigateHome(this@TextureViewDebugModeActivity)
                }
            },
        )
    }

    override fun setupTrackAsiaMapOptions(): TrackAsiaMapOptions {
        val trackasiaMapOptions = super.setupTrackAsiaMapOptions()
        trackasiaMapOptions.textureMode(true)
        return trackasiaMapOptions
    }
}
