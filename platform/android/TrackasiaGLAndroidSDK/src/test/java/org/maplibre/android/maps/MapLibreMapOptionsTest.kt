package org.trackasia.android.maps

import android.graphics.Color
import android.view.Gravity
import org.trackasia.android.camera.CameraPosition
import org.trackasia.android.constants.trackasiaConstants
import org.trackasia.android.geometry.LatLng
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import java.util.*

@RunWith(RobolectricTestRunner::class)
class trackasiaMapOptionsTest {
    @Test
    fun testSanity() {
        Assert.assertNotNull("should not be null",
            trackasiaMapOptions()
        )
    }

    @Test
    fun testDebugEnabled() {
        Assert.assertFalse(trackasiaMapOptions().debugActive)
        Assert.assertTrue(trackasiaMapOptions().debugActive(true).debugActive)
        Assert.assertFalse(trackasiaMapOptions().debugActive(false).debugActive)
    }

    @Test
    fun testCompassEnabled() {
        Assert.assertTrue(trackasiaMapOptions().compassEnabled(true).compassEnabled)
        Assert.assertFalse(trackasiaMapOptions().compassEnabled(false).compassEnabled)
    }

    @Test
    fun testCompassGravity() {
        Assert.assertEquals(
            Gravity.TOP or Gravity.END,
            trackasiaMapOptions().compassGravity
        )
        Assert.assertEquals(
            Gravity.BOTTOM,
            trackasiaMapOptions().compassGravity(Gravity.BOTTOM).compassGravity
        )
        Assert.assertNotEquals(
            Gravity.START.toLong(),
            trackasiaMapOptions().compassGravity(Gravity.BOTTOM).compassGravity.toLong()
        )
    }

    @Test
    fun testCompassMargins() {
        Assert.assertTrue(
            Arrays.equals(
                intArrayOf(0, 1, 2, 3),
                trackasiaMapOptions()
                    .compassMargins(intArrayOf(0, 1, 2, 3)).compassMargins
            )
        )
        Assert.assertFalse(
            Arrays.equals(
                intArrayOf(0, 1, 2, 3),
                trackasiaMapOptions()
                    .compassMargins(intArrayOf(0, 0, 0, 0)).compassMargins
            )
        )
    }

    @Test
    fun testLogoEnabled() {
        Assert.assertTrue(trackasiaMapOptions().logoEnabled(true).logoEnabled)
        Assert.assertFalse(trackasiaMapOptions().logoEnabled(false).logoEnabled)
    }

    @Test
    fun testLogoGravity() {
        Assert.assertEquals(
            Gravity.BOTTOM or Gravity.START,
            trackasiaMapOptions().logoGravity
        )
        Assert.assertEquals(
            Gravity.BOTTOM,
            trackasiaMapOptions().logoGravity(Gravity.BOTTOM).logoGravity
        )
        Assert.assertNotEquals(
            Gravity.START.toLong(),
            trackasiaMapOptions().logoGravity(Gravity.BOTTOM).logoGravity.toLong()
        )
    }

    @Test
    fun testLogoMargins() {
        Assert.assertTrue(
            Arrays.equals(
                intArrayOf(0, 1, 2, 3),
                trackasiaMapOptions()
                    .logoMargins(intArrayOf(0, 1, 2, 3)).logoMargins
            )
        )
        Assert.assertFalse(
            Arrays.equals(
                intArrayOf(0, 1, 2, 3),
                trackasiaMapOptions()
                    .logoMargins(intArrayOf(0, 0, 0, 0)).logoMargins
            )
        )
    }

    @Test
    fun testAttributionTintColor() {
        Assert.assertEquals(-1, trackasiaMapOptions().attributionTintColor)
        Assert.assertEquals(
            Color.RED,
            trackasiaMapOptions().attributionTintColor(Color.RED).attributionTintColor
        )
    }

    @Test
    fun testAttributionEnabled() {
        Assert.assertTrue(trackasiaMapOptions().attributionEnabled(true).attributionEnabled)
        Assert.assertFalse(trackasiaMapOptions().attributionEnabled(false).attributionEnabled)
    }

    @Test
    fun testAttributionGravity() {
        Assert.assertEquals(
            Gravity.BOTTOM or Gravity.START,
            trackasiaMapOptions().attributionGravity
        )
        Assert.assertEquals(
            Gravity.BOTTOM,
            trackasiaMapOptions().attributionGravity(Gravity.BOTTOM).attributionGravity
        )
        Assert.assertNotEquals(
            Gravity.START.toLong(),
            trackasiaMapOptions().attributionGravity(Gravity.BOTTOM).attributionGravity.toLong()
        )
    }

    @Test
    fun testAttributionMargins() {
        Assert.assertTrue(
            Arrays.equals(
                intArrayOf(0, 1, 2, 3),
                trackasiaMapOptions()
                    .attributionMargins(intArrayOf(0, 1, 2, 3)).attributionMargins
            )
        )
        Assert.assertFalse(
            Arrays.equals(
                intArrayOf(0, 1, 2, 3),
                trackasiaMapOptions()
                    .attributionMargins(intArrayOf(0, 0, 0, 0)).attributionMargins
            )
        )
    }

    @Test
    fun testMinZoom() {
        Assert.assertEquals(
            trackasiaConstants.MINIMUM_ZOOM.toDouble(),
            trackasiaMapOptions().minZoomPreference,
            DELTA
        )
        Assert.assertEquals(
            5.0,
            trackasiaMapOptions().minZoomPreference(5.0).minZoomPreference,
            DELTA
        )
        Assert.assertNotEquals(
            2.0,
            trackasiaMapOptions().minZoomPreference(5.0).minZoomPreference,
            DELTA
        )
    }

    @Test
    fun testMaxZoom() {
        Assert.assertEquals(
            trackasiaConstants.MAXIMUM_ZOOM.toDouble(),
            trackasiaMapOptions().maxZoomPreference,
            DELTA
        )
        Assert.assertEquals(
            5.0,
            trackasiaMapOptions().maxZoomPreference(5.0).maxZoomPreference,
            DELTA
        )
        Assert.assertNotEquals(
            2.0,
            trackasiaMapOptions().maxZoomPreference(5.0).maxZoomPreference,
            DELTA
        )
    }

    @Test
    fun testMinPitch() {
        Assert.assertEquals(
            trackasiaConstants.MINIMUM_PITCH.toDouble(),
            trackasiaMapOptions().minPitchPreference,
            DELTA
        )
        Assert.assertEquals(
            5.0,
            trackasiaMapOptions().minPitchPreference(5.0).minPitchPreference,
            DELTA
        )
        Assert.assertNotEquals(
            2.0,
            trackasiaMapOptions().minPitchPreference(5.0).minPitchPreference,
            DELTA
        )
    }

    @Test
    fun testMaxPitch() {
        Assert.assertEquals(
            trackasiaConstants.MAXIMUM_PITCH.toDouble(),
            trackasiaMapOptions().maxPitchPreference,
            DELTA
        )
        Assert.assertEquals(
            5.0,
            trackasiaMapOptions().maxPitchPreference(5.0).maxPitchPreference,
            DELTA
        )
        Assert.assertNotEquals(
            2.0,
            trackasiaMapOptions().maxPitchPreference(5.0).maxPitchPreference,
            DELTA
        )
    }

    @Test
    fun testTiltGesturesEnabled() {
        Assert.assertTrue(trackasiaMapOptions().tiltGesturesEnabled)
        Assert.assertTrue(trackasiaMapOptions().tiltGesturesEnabled(true).tiltGesturesEnabled)
        Assert.assertFalse(trackasiaMapOptions().tiltGesturesEnabled(false).tiltGesturesEnabled)
    }

    @Test
    fun testScrollGesturesEnabled() {
        Assert.assertTrue(trackasiaMapOptions().scrollGesturesEnabled)
        Assert.assertTrue(trackasiaMapOptions().scrollGesturesEnabled(true).scrollGesturesEnabled)
        Assert.assertFalse(trackasiaMapOptions().scrollGesturesEnabled(false).scrollGesturesEnabled)
    }

    @Test
    fun testHorizontalScrollGesturesEnabled() {
        Assert.assertTrue(trackasiaMapOptions().horizontalScrollGesturesEnabled)
        Assert.assertTrue(trackasiaMapOptions().horizontalScrollGesturesEnabled(true).horizontalScrollGesturesEnabled)
        Assert.assertFalse(trackasiaMapOptions().horizontalScrollGesturesEnabled(false).horizontalScrollGesturesEnabled)
    }

    @Test
    fun testZoomGesturesEnabled() {
        Assert.assertTrue(trackasiaMapOptions().zoomGesturesEnabled)
        Assert.assertTrue(trackasiaMapOptions().zoomGesturesEnabled(true).zoomGesturesEnabled)
        Assert.assertFalse(trackasiaMapOptions().zoomGesturesEnabled(false).zoomGesturesEnabled)
    }

    @Test
    fun testRotateGesturesEnabled() {
        Assert.assertTrue(trackasiaMapOptions().rotateGesturesEnabled)
        Assert.assertTrue(trackasiaMapOptions().rotateGesturesEnabled(true).rotateGesturesEnabled)
        Assert.assertFalse(trackasiaMapOptions().rotateGesturesEnabled(false).rotateGesturesEnabled)
    }

    @Test
    fun testCamera() {
        val position = CameraPosition.Builder().build()
        Assert.assertEquals(
            CameraPosition.Builder(position).build(),
            trackasiaMapOptions().camera(position).camera
        )
        Assert.assertNotEquals(
            CameraPosition.Builder().target(LatLng(1.0, 1.0)),
            trackasiaMapOptions().camera(position)
        )
        Assert.assertNull(trackasiaMapOptions().camera)
    }

    @Test
    fun testPrefetchesTiles() {
        // Default value
        Assert.assertTrue(trackasiaMapOptions().prefetchesTiles)

        // Check mutations
        Assert.assertTrue(trackasiaMapOptions().setPrefetchesTiles(true).prefetchesTiles)
        Assert.assertFalse(trackasiaMapOptions().setPrefetchesTiles(false).prefetchesTiles)
    }

    @Test
    fun testPrefetchZoomDelta() {
        // Default value
        Assert.assertEquals(4, trackasiaMapOptions().prefetchZoomDelta)

        // Check mutations
        Assert.assertEquals(
            5,
            trackasiaMapOptions().setPrefetchZoomDelta(5).prefetchZoomDelta
        )
    }

    @Test
    fun testCrossSourceCollisions() {
        // Default value
        Assert.assertTrue(trackasiaMapOptions().crossSourceCollisions)

        // check mutations
        Assert.assertTrue(trackasiaMapOptions().crossSourceCollisions(true).crossSourceCollisions)
        Assert.assertFalse(trackasiaMapOptions().crossSourceCollisions(false).crossSourceCollisions)
    }

    @Test
    fun testLocalIdeographFontFamily_enabledByDefault() {
        val options = trackasiaMapOptions.createFromAttributes(RuntimeEnvironment.application, null)
        Assert.assertEquals(
            trackasiaConstants.DEFAULT_FONT,
            options.localIdeographFontFamily
        )
    }

    companion object {
        private const val DELTA = 1e-15
    }
}
