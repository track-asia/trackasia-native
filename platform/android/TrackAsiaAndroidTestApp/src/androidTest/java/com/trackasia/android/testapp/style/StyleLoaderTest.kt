package com.trackasia.android.testapp.style

import android.view.View
import androidx.test.espresso.UiController
import com.trackasia.android.maps.MapView
import com.trackasia.android.maps.TrackAsiaMap
import com.trackasia.android.maps.Style
import com.trackasia.android.testapp.R
import com.trackasia.android.testapp.action.TrackAsiaMapAction
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
        TrackAsiaMapAction.invoke(
            trackasiaMap
        ) { uiController: UiController?, trackasiaMap: TrackAsiaMap ->
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
        TrackAsiaMapAction.invoke(
            trackasiaMap
        ) { uiController: UiController?, trackasiaMap: TrackAsiaMap ->
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
