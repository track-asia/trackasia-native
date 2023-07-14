package org.trackasia.android.testapp.style

import android.view.View
import androidx.test.espresso.UiController
import org.trackasia.android.maps.MapView
import org.trackasia.android.maps.trackasiaMap
import org.trackasia.android.maps.Style
import org.trackasia.android.testapp.R
import org.trackasia.android.testapp.action.trackasiaMapAction
import org.trackasia.android.testapp.activity.EspressoTest
import org.trackasia.android.testapp.utils.ResourceUtils.readRawResource
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
        trackasiaMapAction.invoke(
            trackasiaMap
        ) { uiController: UiController?, trackasiaMap: trackasiaMap ->
            try {
                val expected =
                    readRawResource(
                        rule.activity,
                        R.raw.local_style
                    )
                trackasiaMap.setStyle(Style.Builder().fromJson(expected))
                val actual = trackasiaMap.style!!.json
                Assert.assertEquals("Style json should match", expected, actual)
            } catch (exception: IOException) {
                exception.printStackTrace()
            }
        }
    }

    @Test
    fun testDefaultStyleLoadWithActivityLifecycleChange() {
        validateTestSetup()
        trackasiaMapAction.invoke(
            trackasiaMap
        ) { uiController: UiController?, trackasiaMap: trackasiaMap ->
            try {
                val expected =
                    readRawResource(
                        rule.activity,
                        R.raw.local_style
                    )
                trackasiaMap.setStyle(Style.Builder().fromJson(expected))

                // fake activity stop/start
                val mapView =
                    rule.activity.findViewById<View>(R.id.mapView) as MapView
                mapView.onPause()
                mapView.onStop()
                mapView.onStart()
                mapView.onResume()
                val actual = trackasiaMap.style!!.json
                Assert.assertEquals(
                    "Style URL should be empty",
                    "",
                    trackasiaMap.style!!.uri
                )
                Assert.assertEquals("Style json should match", expected, actual)
            } catch (exception: IOException) {
                exception.printStackTrace()
            }
        }
    }
}
