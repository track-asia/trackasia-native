package com.trackasia.android.testapp.activity

import androidx.annotation.UiThread
import com.trackasia.android.maps.Style
import com.trackasia.android.maps.TrackAsiaMap
import com.trackasia.android.testapp.activity.espresso.EspressoTestActivity
import com.trackasia.android.testapp.styles.TestStyles

/**
 * Base class for all tests using EspressoTestActivity as wrapper.
 *
 *
 * Loads "assets/streets.json" as style.
 *
 */
open class EspressoTest : BaseTest() {
    override fun getActivityClass(): Class<*> = EspressoTestActivity::class.java

    @UiThread
    override fun initMap(trackasiaMap: TrackAsiaMap) {
        trackasiaMap.setStyle(Style.Builder().fromUri(TestStyles.VERSATILES))
        super.initMap(trackasiaMap)
    }
}
