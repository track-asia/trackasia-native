package com.trackasia.android.location.utils

import androidx.test.espresso.IdlingResource
import com.trackasia.android.maps.Style
import com.trackasia.android.maps.TrackAsiaMap

/**
 * Resource, that's idling until the provided style is loaded.
 * Remember to add any espresso action (like view assertion) after the [waitForStyle] call
 * for the test to keep running.
 */
class StyleChangeIdlingResource : IdlingResource {
    private var callback: IdlingResource.ResourceCallback? = null
    private var isIdle = true

    override fun getName(): String = javaClass.simpleName

    override fun isIdleNow(): Boolean = isIdle

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback?) {
        this.callback = callback
    }

    private fun setIdle() {
        isIdle = true
        callback?.onTransitionToIdle()
    }

    fun waitForStyle(
        trackasiaMap: TrackAsiaMap,
        styleUrl: String,
    ) {
        isIdle = false
        trackasiaMap.setStyle(Style.Builder().fromUri(styleUrl)) {
            setIdle()
        }
    }
}
