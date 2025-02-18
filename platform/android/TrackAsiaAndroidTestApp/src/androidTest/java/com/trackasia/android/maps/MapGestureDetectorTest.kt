package com.trackasia.android.maps

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.trackasia.android.camera.CameraPosition
import com.trackasia.android.camera.CameraUpdateFactory
import com.trackasia.android.constants.TrackAsiaConstants
import com.trackasia.android.geometry.LatLng
import com.trackasia.android.maps.GesturesUiTestUtils.move
import com.trackasia.android.maps.GesturesUiTestUtils.quickScale
import com.trackasia.android.testapp.R
import com.trackasia.android.testapp.activity.BaseTest
import com.trackasia.android.testapp.activity.maplayout.SimpleMapActivity
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class MapGestureDetectorTest : BaseTest() {
    override fun getActivityClass() = SimpleMapActivity::class.java

    private var maxWidth: Int = 0
    private var maxHeight: Int = 0

    @Before
    fun setup() {
        maxWidth = mapView.width
        maxHeight = mapView.height
    }

    @Test
    fun sanity_quickZoom() {
        validateTestSetup()
        var initialZoom: Double? = null
        rule.runOnUiThread {
            initialZoom = trackasiaMap.cameraPosition.zoom
        }
        onView(withId(R.id.mapView)).perform(quickScale(maxHeight / 2f, withVelocity = false))
        rule.runOnUiThread {
            Assert.assertTrue(trackasiaMap.cameraPosition.zoom > initialZoom!!)
        }
    }

    @Test
    fun quickZoomDisabled_phantomQuickZoom_moveStillEnabled_15091() {
        // regression test for https://github.com/mapbox/mapbox-gl-native/issues/15091
        validateTestSetup()
        var initialCameraPosition: CameraPosition? = null
        rule.runOnUiThread {
            // zoom in so we can move vertically
            trackasiaMap.moveCamera(CameraUpdateFactory.zoomTo(4.0))
            initialCameraPosition = trackasiaMap.cameraPosition
            trackasiaMap.uiSettings.isQuickZoomGesturesEnabled = false
        }

        onView(withId(R.id.mapView)).perform(quickScale(maxHeight / 2f))
        rule.runOnUiThread {
            // camera did not move
            Assert.assertEquals(initialCameraPosition!!, trackasiaMap.cameraPosition)
        }

        // move to expected target
        onView(withId(R.id.mapView)).perform(move(-maxWidth / 2f, -maxHeight / 2f, withVelocity = false))
        rule.runOnUiThread {
            Assert.assertNotEquals(initialCameraPosition!!.target!!.latitude, trackasiaMap.cameraPosition.target!!.latitude, 1.0)
            Assert.assertNotEquals(initialCameraPosition!!.target!!.longitude, trackasiaMap.cameraPosition.target!!.longitude, 1.0)
        }
    }

    @Test
    fun quickZoom_doNotMove_14227() {
        // test for https://github.com/mapbox/mapbox-gl-native/issues/14227
        validateTestSetup()
        var initialTarget: LatLng? = null
        rule.runOnUiThread {
            initialTarget = trackasiaMap.cameraPosition.target!!
        }

        onView(withId(R.id.mapView)).perform(quickScale(maxHeight / 2f))
        rule.runOnUiThread {
            // camera did not move
            Assert.assertEquals(initialTarget!!.latitude, trackasiaMap.cameraPosition.target!!.latitude, 1.0)
            Assert.assertEquals(initialTarget!!.longitude, trackasiaMap.cameraPosition.target!!.longitude, 1.0)
        }
    }

    @Test
    fun quickZoom_interrupted_moveStillEnabled_14598() {
        // test for https://github.com/mapbox/mapbox-gl-native/issues/14598
        validateTestSetup()
        onView(withId(R.id.mapView)).perform(quickScale(maxHeight / 2f, interrupt = true))

        var initialCameraPosition: CameraPosition? = null
        rule.runOnUiThread {
            // zoom in so we can move vertically
            trackasiaMap.moveCamera(CameraUpdateFactory.zoomTo(4.0))
            initialCameraPosition = trackasiaMap.cameraPosition
            trackasiaMap.uiSettings.isQuickZoomGesturesEnabled = false
        }

        // move to expected target
        onView(withId(R.id.mapView)).perform(move(-maxWidth / 2f, -maxHeight / 2f, withVelocity = false))
        rule.runOnUiThread {
            Assert.assertNotEquals(initialCameraPosition!!.target!!.latitude, trackasiaMap.cameraPosition.target!!.latitude, 1.0)
            Assert.assertNotEquals(initialCameraPosition!!.target!!.longitude, trackasiaMap.cameraPosition.target!!.longitude, 1.0)
        }
    }

    @Test
    fun quickZoom_ignoreDoubleTap() {
        // test for https://github.com/mapbox/mapbox-gl-native/issues/14013
        validateTestSetup()
        var initialZoom: Double? = null
        rule.runOnUiThread {
            trackasiaMap.moveCamera(CameraUpdateFactory.zoomTo(2.0))
            initialZoom = trackasiaMap.cameraPosition.zoom
        }
        onView(withId(R.id.mapView)).perform(
            quickScale(
                -(
                    trackasiaMap.gesturesManager.standardScaleGestureDetector.spanSinceStartThreshold *
                        2
                ),
                withVelocity = false,
                duration = 1000L,
            ),
        )
        R.id.mapView.loopFor(TrackAsiaConstants.ANIMATION_DURATION.toLong())
        rule.runOnUiThread {
            Assert.assertTrue(trackasiaMap.cameraPosition.zoom < initialZoom!!)
        }
    }

    @Test
    fun doubleTap_minimalMovement() {
        validateTestSetup()
        var initialZoom: Double? = null
        rule.runOnUiThread {
            initialZoom = trackasiaMap.cameraPosition.zoom
        }
        onView(withId(R.id.mapView)).perform(
            quickScale(
                trackasiaMap.gesturesManager.standardScaleGestureDetector.spanSinceStartThreshold / 2,
                withVelocity = false,
                duration = 50L,
            ),
        )
        R.id.mapView.loopFor(TrackAsiaConstants.ANIMATION_DURATION.toLong())
        rule.runOnUiThread {
            Assert.assertEquals(initialZoom!! + 1, trackasiaMap.cameraPosition.zoom, 0.1)
        }
    }

    @Test
    fun doubleTap_overMaxThreshold_ignore_14013() {
        // test for https://github.com/mapbox/mapbox-gl-native/issues/14013
        validateTestSetup()
        var initialZoom: Double? = null
        rule.runOnUiThread {
            initialZoom = trackasiaMap.cameraPosition.zoom
            trackasiaMap.uiSettings.isQuickZoomGesturesEnabled = false
        }
        onView(withId(R.id.mapView)).perform(
            quickScale(
                trackasiaMap.gesturesManager.standardScaleGestureDetector.spanSinceStartThreshold * 2,
                withVelocity = false,
                duration = 50L,
            ),
        )
        R.id.mapView.loopFor(TrackAsiaConstants.ANIMATION_DURATION.toLong())
        rule.runOnUiThread {
            Assert.assertEquals(initialZoom!!, trackasiaMap.cameraPosition.zoom, 0.01)
        }
    }

    @Test
    fun doubleTap_interrupted_moveStillEnabled() {
        validateTestSetup()

        rule.runOnUiThread {
            trackasiaMap.moveCamera(CameraUpdateFactory.zoomTo(4.0))
        }

        onView(withId(R.id.mapView)).perform(
            quickScale(
                trackasiaMap.gesturesManager.standardScaleGestureDetector.spanSinceStartThreshold / 2,
                withVelocity = false,
                duration = 50L,
                interrupt = true,
            ),
        )

        var initialCameraPosition: CameraPosition? = null
        rule.runOnUiThread {
            // zoom in so we can move vertically
            trackasiaMap.moveCamera(CameraUpdateFactory.zoomTo(4.0))
            initialCameraPosition = trackasiaMap.cameraPosition
            trackasiaMap.uiSettings.isQuickZoomGesturesEnabled = false
        }

        // move to expected target
        onView(withId(R.id.mapView)).perform(move(-maxWidth / 2f, -maxHeight / 2f, withVelocity = false))
        rule.runOnUiThread {
            Assert.assertNotEquals(initialCameraPosition!!.target!!.latitude, trackasiaMap.cameraPosition.target!!.latitude, 1.0)
            Assert.assertNotEquals(initialCameraPosition!!.target!!.longitude, trackasiaMap.cameraPosition.target!!.longitude, 1.0)
        }
    }

    @Test
    fun quickZoom_roundTripping() {
        validateTestSetup()
        rule.runOnUiThread {
            trackasiaMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(51.0, 16.0), 3.0))
        }
        onView(withId(R.id.mapView)).perform(quickScale(300f, withVelocity = false, duration = 750L))
        onView(withId(R.id.mapView)).perform(quickScale(-300f, withVelocity = false, duration = 750L))

        rule.runOnUiThread {
            Assert.assertEquals(3.0, trackasiaMap.cameraPosition.zoom, 0.01)
        }
    }
}
