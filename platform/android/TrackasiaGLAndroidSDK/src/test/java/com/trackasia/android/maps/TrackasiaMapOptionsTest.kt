package com.trackasia.android.maps

import android.graphics.Color
import android.view.Gravity
import com.trackasia.android.camera.CameraPosition
import com.trackasia.android.constants.TrackasiaConstants
import com.trackasia.android.geometry.LatLng
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import java.util.*

@RunWith(RobolectricTestRunner::class)
class TrackasiaMapOptionsTest {
    @Test
    fun testSanity() {
        Assert.assertNotNull("should not be null",
            TrackasiaMapOptions()
        )
    }

    @Test
    fun testDebugEnabled() {
        Assert.assertFalse(TrackasiaMapOptions().debugActive)
        Assert.assertTrue(TrackasiaMapOptions().debugActive(true).debugActive)
        Assert.assertFalse(TrackasiaMapOptions().debugActive(false).debugActive)
    }

    @Test
    fun testCompassEnabled() {
        Assert.assertTrue(TrackasiaMapOptions().compassEnabled(true).compassEnabled)
        Assert.assertFalse(TrackasiaMapOptions().compassEnabled(false).compassEnabled)
    }

    @Test
    fun testCompassGravity() {
        Assert.assertEquals(
            Gravity.TOP or Gravity.END,
            TrackasiaMapOptions().compassGravity
        )
        Assert.assertEquals(
            Gravity.BOTTOM,
            TrackasiaMapOptions().compassGravity(Gravity.BOTTOM).compassGravity
        )
        Assert.assertNotEquals(
            Gravity.START.toLong(),
            TrackasiaMapOptions().compassGravity(Gravity.BOTTOM).compassGravity.toLong()
        )
    }

    @Test
    fun testCompassMargins() {
        Assert.assertTrue(
            Arrays.equals(
                intArrayOf(0, 1, 2, 3),
                TrackasiaMapOptions()
                    .compassMargins(intArrayOf(0, 1, 2, 3)).compassMargins
            )
        )
        Assert.assertFalse(
            Arrays.equals(
                intArrayOf(0, 1, 2, 3),
                TrackasiaMapOptions()
                    .compassMargins(intArrayOf(0, 0, 0, 0)).compassMargins
            )
        )
    }

    @Test
    fun testLogoEnabled() {
        Assert.assertTrue(TrackasiaMapOptions().logoEnabled(true).logoEnabled)
        Assert.assertFalse(TrackasiaMapOptions().logoEnabled(false).logoEnabled)
    }

    @Test
    fun testLogoGravity() {
        Assert.assertEquals(
            Gravity.BOTTOM or Gravity.START,
            TrackasiaMapOptions().logoGravity
        )
        Assert.assertEquals(
            Gravity.BOTTOM,
            TrackasiaMapOptions().logoGravity(Gravity.BOTTOM).logoGravity
        )
        Assert.assertNotEquals(
            Gravity.START.toLong(),
            TrackasiaMapOptions().logoGravity(Gravity.BOTTOM).logoGravity.toLong()
        )
    }

    @Test
    fun testLogoMargins() {
        Assert.assertTrue(
            Arrays.equals(
                intArrayOf(0, 1, 2, 3),
                TrackasiaMapOptions()
                    .logoMargins(intArrayOf(0, 1, 2, 3)).logoMargins
            )
        )
        Assert.assertFalse(
            Arrays.equals(
                intArrayOf(0, 1, 2, 3),
                TrackasiaMapOptions()
                    .logoMargins(intArrayOf(0, 0, 0, 0)).logoMargins
            )
        )
    }

    @Test
    fun testAttributionTintColor() {
        Assert.assertEquals(-1, TrackasiaMapOptions().attributionTintColor)
        Assert.assertEquals(
            Color.RED,
            TrackasiaMapOptions().attributionTintColor(Color.RED).attributionTintColor
        )
    }

    @Test
    fun testAttributionEnabled() {
        Assert.assertTrue(TrackasiaMapOptions().attributionEnabled(true).attributionEnabled)
        Assert.assertFalse(TrackasiaMapOptions().attributionEnabled(false).attributionEnabled)
    }

    @Test
    fun testAttributionGravity() {
        Assert.assertEquals(
            Gravity.BOTTOM or Gravity.START,
            TrackasiaMapOptions().attributionGravity
        )
        Assert.assertEquals(
            Gravity.BOTTOM,
            TrackasiaMapOptions().attributionGravity(Gravity.BOTTOM).attributionGravity
        )
        Assert.assertNotEquals(
            Gravity.START.toLong(),
            TrackasiaMapOptions().attributionGravity(Gravity.BOTTOM).attributionGravity.toLong()
        )
    }

    @Test
    fun testAttributionMargins() {
        Assert.assertTrue(
            Arrays.equals(
                intArrayOf(0, 1, 2, 3),
                TrackasiaMapOptions()
                    .attributionMargins(intArrayOf(0, 1, 2, 3)).attributionMargins
            )
        )
        Assert.assertFalse(
            Arrays.equals(
                intArrayOf(0, 1, 2, 3),
                TrackasiaMapOptions()
                    .attributionMargins(intArrayOf(0, 0, 0, 0)).attributionMargins
            )
        )
    }

    @Test
    fun testMinZoom() {
        Assert.assertEquals(
            TrackasiaConstants.MINIMUM_ZOOM.toDouble(),
            TrackasiaMapOptions().minZoomPreference,
            DELTA
        )
        Assert.assertEquals(
            5.0,
            TrackasiaMapOptions().minZoomPreference(5.0).minZoomPreference,
            DELTA
        )
        Assert.assertNotEquals(
            2.0,
            TrackasiaMapOptions().minZoomPreference(5.0).minZoomPreference,
            DELTA
        )
    }

    @Test
    fun testMaxZoom() {
        Assert.assertEquals(
            TrackasiaConstants.MAXIMUM_ZOOM.toDouble(),
            TrackasiaMapOptions().maxZoomPreference,
            DELTA
        )
        Assert.assertEquals(
            5.0,
            TrackasiaMapOptions().maxZoomPreference(5.0).maxZoomPreference,
            DELTA
        )
        Assert.assertNotEquals(
            2.0,
            TrackasiaMapOptions().maxZoomPreference(5.0).maxZoomPreference,
            DELTA
        )
    }

    @Test
    fun testMinPitch() {
        Assert.assertEquals(
            TrackasiaConstants.MINIMUM_PITCH.toDouble(),
            TrackasiaMapOptions().minPitchPreference,
            DELTA
        )
        Assert.assertEquals(
            5.0,
            TrackasiaMapOptions().minPitchPreference(5.0).minPitchPreference,
            DELTA
        )
        Assert.assertNotEquals(
            2.0,
            TrackasiaMapOptions().minPitchPreference(5.0).minPitchPreference,
            DELTA
        )
    }

    @Test
    fun testMaxPitch() {
        Assert.assertEquals(
            TrackasiaConstants.MAXIMUM_PITCH.toDouble(),
            TrackasiaMapOptions().maxPitchPreference,
            DELTA
        )
        Assert.assertEquals(
            5.0,
            TrackasiaMapOptions().maxPitchPreference(5.0).maxPitchPreference,
            DELTA
        )
        Assert.assertNotEquals(
            2.0,
            TrackasiaMapOptions().maxPitchPreference(5.0).maxPitchPreference,
            DELTA
        )
    }

    @Test
    fun testTiltGesturesEnabled() {
        Assert.assertTrue(TrackasiaMapOptions().tiltGesturesEnabled)
        Assert.assertTrue(TrackasiaMapOptions().tiltGesturesEnabled(true).tiltGesturesEnabled)
        Assert.assertFalse(TrackasiaMapOptions().tiltGesturesEnabled(false).tiltGesturesEnabled)
    }

    @Test
    fun testScrollGesturesEnabled() {
        Assert.assertTrue(TrackasiaMapOptions().scrollGesturesEnabled)
        Assert.assertTrue(TrackasiaMapOptions().scrollGesturesEnabled(true).scrollGesturesEnabled)
        Assert.assertFalse(TrackasiaMapOptions().scrollGesturesEnabled(false).scrollGesturesEnabled)
    }

    @Test
    fun testHorizontalScrollGesturesEnabled() {
        Assert.assertTrue(TrackasiaMapOptions().horizontalScrollGesturesEnabled)
        Assert.assertTrue(TrackasiaMapOptions().horizontalScrollGesturesEnabled(true).horizontalScrollGesturesEnabled)
        Assert.assertFalse(TrackasiaMapOptions().horizontalScrollGesturesEnabled(false).horizontalScrollGesturesEnabled)
    }

    @Test
    fun testZoomGesturesEnabled() {
        Assert.assertTrue(TrackasiaMapOptions().zoomGesturesEnabled)
        Assert.assertTrue(TrackasiaMapOptions().zoomGesturesEnabled(true).zoomGesturesEnabled)
        Assert.assertFalse(TrackasiaMapOptions().zoomGesturesEnabled(false).zoomGesturesEnabled)
    }

    @Test
    fun testRotateGesturesEnabled() {
        Assert.assertTrue(TrackasiaMapOptions().rotateGesturesEnabled)
        Assert.assertTrue(TrackasiaMapOptions().rotateGesturesEnabled(true).rotateGesturesEnabled)
        Assert.assertFalse(TrackasiaMapOptions().rotateGesturesEnabled(false).rotateGesturesEnabled)
    }

    @Test
    fun testCamera() {
        val position = CameraPosition.Builder().build()
        Assert.assertEquals(
            CameraPosition.Builder(position).build(),
            TrackasiaMapOptions().camera(position).camera
        )
        Assert.assertNotEquals(
            CameraPosition.Builder().target(LatLng(1.0, 1.0)),
            TrackasiaMapOptions().camera(position)
        )
        Assert.assertNull(TrackasiaMapOptions().camera)
    }

    @Test
    fun testPrefetchesTiles() {
        // Default value
        Assert.assertTrue(TrackasiaMapOptions().prefetchesTiles)

        // Check mutations
        Assert.assertTrue(TrackasiaMapOptions().setPrefetchesTiles(true).prefetchesTiles)
        Assert.assertFalse(TrackasiaMapOptions().setPrefetchesTiles(false).prefetchesTiles)
    }

    @Test
    fun testPrefetchZoomDelta() {
        // Default value
        Assert.assertEquals(4, TrackasiaMapOptions().prefetchZoomDelta)

        // Check mutations
        Assert.assertEquals(
            5,
            TrackasiaMapOptions().setPrefetchZoomDelta(5).prefetchZoomDelta
        )
    }

    @Test
    fun testCrossSourceCollisions() {
        // Default value
        Assert.assertTrue(TrackasiaMapOptions().crossSourceCollisions)

        // check mutations
        Assert.assertTrue(TrackasiaMapOptions().crossSourceCollisions(true).crossSourceCollisions)
        Assert.assertFalse(TrackasiaMapOptions().crossSourceCollisions(false).crossSourceCollisions)
    }

    @Test
    fun testLocalIdeographFontFamily_enabledByDefault() {
        val options = TrackasiaMapOptions.createFromAttributes(RuntimeEnvironment.application, null)
        Assert.assertEquals(
            TrackasiaConstants.DEFAULT_FONT,
            options.localIdeographFontFamily
        )
    }

    companion object {
        private const val DELTA = 1e-15
    }
}
