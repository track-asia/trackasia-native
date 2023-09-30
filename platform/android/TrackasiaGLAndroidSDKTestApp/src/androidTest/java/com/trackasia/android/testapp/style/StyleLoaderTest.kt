package com.trackasia.android.testapp.style

import android.view.View
import androidx.test.espresso.UiController
import com.trackasia.android.maps.MapView
import com.trackasia.android.maps.TrackasiaMap
import com.trackasia.android.maps.Style
import com.trackasia.android.testapp.R
import com.trackasia.android.testapp.action.TrackasiaMapAction
import com.trackasia.android.testapp.activity.EspressoTest
import com.trackasia.android.testapp.utils.ResourceUtils.readRawResource
import org.junit.Assert
import org.junit.Test
import java.io.IOException

/**
 * Tests around style loading
 */
class StyleLoaderTest : EspressoTest() {
    @Test
    fun testSetGetStyleJsonString() {
        validateTestSetup()
        TrackasiaMapAction.invoke(
            mapboxMap
        ) { uiController: UiController?, mapboxMap: TrackasiaMap ->
            try {
                val expected =
                    readRawResource(
                        rule.activity,
                        R.raw.local_style
                    )
                mapboxMap.setStyle(Style.Builder().fromJson(expected))
                val actual = mapboxMap.style!!.json
                Assert.assertEquals("Style json should match", expected, actual)
            } catch (exception: IOException) {
                exception.printStackTrace()
            }
        }
    }

    @Test
    fun testDefaultStyleLoadWithActivityLifecycleChange() {
        validateTestSetup()
        TrackasiaMapAction.invoke(
            mapboxMap
        ) { uiController: UiController?, mapboxMap: TrackasiaMap ->
            try {
                val expected =
                    readRawResource(
                        rule.activity,
                        R.raw.local_style
                    )
                mapboxMap.setStyle(Style.Builder().fromJson(expected))

                // fake activity stop/start
                val mapView =
                    rule.activity.findViewById<View>(R.id.mapView) as MapView
                mapView.onPause()
                mapView.onStop()
                mapView.onStart()
                mapView.onResume()
                val actual = mapboxMap.style!!.json
                Assert.assertEquals(
                    "Style URL should be empty",
                    "",
                    mapboxMap.style!!.uri
                )
                Assert.assertEquals("Style json should match", expected, actual)
            } catch (exception: IOException) {
                exception.printStackTrace()
            }
        }
    }
}
