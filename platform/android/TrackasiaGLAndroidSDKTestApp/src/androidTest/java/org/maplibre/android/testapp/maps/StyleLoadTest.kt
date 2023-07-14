package org.trackasia.android.testapp.maps

import androidx.test.espresso.UiController
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.trackasia.android.maps.trackasiaMap
import org.trackasia.android.maps.Style
import org.trackasia.android.style.layers.SymbolLayer
import org.trackasia.android.style.sources.GeoJsonSource
import org.trackasia.android.testapp.action.trackasiaMapAction
import org.trackasia.android.testapp.activity.EspressoTest
import org.trackasia.android.testapp.utils.TestingAsyncUtils
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class StyleLoadTest : EspressoTest() {

    @Test
    fun updateSourceAfterStyleLoad() {
        validateTestSetup()
        trackasiaMapAction.invoke(trackasiaMap) { uiController: UiController, trackasiaMap: trackasiaMap ->
            val source = GeoJsonSource("id")
            val layer = SymbolLayer("id", "id")
            trackasiaMap.setStyle(Style.Builder().withSource(source).withLayer(layer))
            TestingAsyncUtils.waitForLayer(uiController, mapView)
            trackasiaMap.setStyle(Style.Builder().fromUrl(Style.getPredefinedStyle("Streets")))
            TestingAsyncUtils.waitForLayer(uiController, mapView)
            source.setGeoJson("{}")
        }
    }
}
