package com.trackasia.android.testapp.maps

import androidx.test.espresso.UiController
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.trackasia.android.maps.MapLibreMap
import com.trackasia.android.maps.Style
import com.trackasia.android.style.layers.SymbolLayer
import com.trackasia.android.style.sources.GeoJsonSource
import com.trackasia.android.testapp.action.MapLibreMapAction
import com.trackasia.android.testapp.activity.EspressoTest
import com.trackasia.android.testapp.utils.TestingAsyncUtils
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class StyleLoadTest : EspressoTest() {

    @Test
    fun updateSourceAfterStyleLoad() {
        validateTestSetup()
        MapLibreMapAction.invoke(trackasiaMap) { uiController: UiController, trackasiaMap: MapLibreMap ->
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
