package com.trackasia.android.maps

import android.graphics.Color
import android.view.Gravity
import com.trackasia.android.camera.CameraPosition
import com.trackasia.android.constants.TrackAsiaConstants
import com.trackasia.android.geometry.LatLng
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import java.util.*

@RunWith(RobolectricTestRunner::class)
class TrackAsiaMapOptionsTest {
    @Test
    fun testSanity() {
        Assert.assertNotNull("should not be null",
            TrackAsiaMapOptions()
        )
    }

    @Test
    fun testDebugEnabled() {
        Assert.assertFalse(TrackAsiaMapOptions().debugActive)
        Assert.assertTrue(TrackAsiaMapOptions().debugActive(true).debugActive)
        Assert.assertFalse(TrackAsiaMapOptions().debugActive(false).debugActive)
    }

    @Test
    fun testCompassEnabled() {
        Assert.assertTrue(TrackAsiaMapOptions().compassEnabled(true).compassEnabled)
        Assert.assertFalse(TrackAsiaMapOptions().compassEnabled(false).compassEnabled)
    }

    @Test
    fun testCompassGravity() {
        Assert.assertEquals(
            Gravity.TOP or Gravity.END,
            TrackAsiaMapOptions().compassGravity
        )
        Assert.assertEquals(
            Gravity.BOTTOM,
            TrackAsiaMapOptions().compassGravity(Gravity.BOTTOM).compassGravity
        )
        Assert.assertNotEquals(
            Gravity.START.toLong(),
            TrackAsiaMapOptions().compassGravity(Gravity.BOTTOM).compassGravity.toLong()
        )
    }

    @Test
    fun testCompassMargins() {
        Assert.assertTrue(
            Arrays.equals(
                intArrayOf(0, 1, 2, 3),
                TrackAsiaMapOptions()
                    .compassMargins(intArrayOf(0, 1, 2, 3)).compassMargins
            )
        )
        Assert.assertFalse(
            Arrays.equals(
                intArrayOf(0, 1, 2, 3),
                TrackAsiaMapOptions()
                    .compassMargins(intArrayOf(0, 0, 0, 0)).compassMargins
            )
        )
    }

    @Test
    fun testLogoEnabled() {
        Assert.assertTrue(TrackAsiaMapOptions().logoEnabled(true).logoEnabled)
        Assert.assertFalse(TrackAsiaMapOptions().logoEnabled(false).logoEnabled)
    }

    @Test
    fun testLogoGravity() {
        Assert.assertEquals(
            Gravity.BOTTOM or Gravity.START,
            TrackAsiaMapOptions().logoGravity
        )
        Assert.assertEquals(
            Gravity.BOTTOM,
            TrackAsiaMapOptions().logoGravity(Gravity.BOTTOM).logoGravity
        )
        Assert.assertNotEquals(
            Gravity.START.toLong(),
            TrackAsiaMapOptions().logoGravity(Gravity.BOTTOM).logoGravity.toLong()
        )
    }

    @Test
    fun testLogoMargins() {
        Assert.assertTrue(
            Arrays.equals(
                intArrayOf(0, 1, 2, 3),
                TrackAsiaMapOptions()
                    .logoMargins(intArrayOf(0, 1, 2, 3)).logoMargins
            )
        )
        Assert.assertFalse(
            Arrays.equals(
                intArrayOf(0, 1, 2, 3),
                TrackAsiaMapOptions()
                    .logoMargins(intArrayOf(0, 0, 0, 0)).logoMargins
            )
        )
    }

    @Test
    fun testAttributionTintColor() {
        Assert.assertEquals(-1, TrackAsiaMapOptions().attributionTintColor)
        Assert.assertEquals(
            Color.RED,
            TrackAsiaMapOptions().attributionTintColor(Color.RED).attributionTintColor
        )
    }

    @Test
    fun testAttributionEnabled() {
        Assert.assertTrue(TrackAsiaMapOptions().attributionEnabled(true).attributionEnabled)
        Assert.assertFalse(TrackAsiaMapOptions().attributionEnabled(false).attributionEnabled)
    }

    @Test
    fun testAttributionGravity() {
        Assert.assertEquals(
            Gravity.BOTTOM or Gravity.START,
            TrackAsiaMapOptions().attributionGravity
        )
        Assert.assertEquals(
            Gravity.BOTTOM,
            TrackAsiaMapOptions().attributionGravity(Gravity.BOTTOM).attributionGravity
        )
        Assert.assertNotEquals(
            Gravity.START.toLong(),
            TrackAsiaMapOptions().attributionGravity(Gravity.BOTTOM).attributionGravity.toLong()
        )
    }

    @Test
    fun testAttributionMargins() {
        Assert.assertTrue(
            Arrays.equals(
                intArrayOf(0, 1, 2, 3),
                TrackAsiaMapOptions()
                    .attributionMargins(intArrayOf(0, 1, 2, 3)).attributionMargins
            )
        )
        Assert.assertFalse(
            Arrays.equals(
                intArrayOf(0, 1, 2, 3),
                TrackAsiaMapOptions()
                    .attributionMargins(intArrayOf(0, 0, 0, 0)).attributionMargins
            )
        )
    }

    @Test
    fun testMinZoom() {
        Assert.assertEquals(
            TrackAsiaConstants.MINIMUM_ZOOM.toDouble(),
            TrackAsiaMapOptions().minZoomPreference,
            DELTA
        )
        Assert.assertEquals(
            5.0,
            TrackAsiaMapOptions().minZoomPreference(5.0).minZoomPreference,
            DELTA
        )
        Assert.assertNotEquals(
            2.0,
            TrackAsiaMapOptions().minZoomPreference(5.0).minZoomPreference,
            DELTA
        )
    }

    @Test
    fun testMaxZoom() {
        Assert.assertEquals(
            TrackAsiaConstants.MAXIMUM_ZOOM.toDouble(),
            TrackAsiaMapOptions().maxZoomPreference,
            DELTA
        )
        Assert.assertEquals(
            5.0,
            TrackAsiaMapOptions().maxZoomPreference(5.0).maxZoomPreference,
            DELTA
        )
        Assert.assertNotEquals(
            2.0,
            TrackAsiaMapOptions().maxZoomPreference(5.0).maxZoomPreference,
            DELTA
        )
    }

    @Test
    fun testMinPitch() {
        Assert.assertEquals(
            TrackAsiaConstants.MINIMUM_PITCH.toDouble(),
            TrackAsiaMapOptions().minPitchPreference,
            DELTA
        )
        Assert.assertEquals(
            5.0,
            TrackAsiaMapOptions().minPitchPreference(5.0).minPitchPreference,
            DELTA
        )
        Assert.assertNotEquals(
            2.0,
            TrackAsiaMapOptions().minPitchPreference(5.0).minPitchPreference,
            DELTA
        )
    }

    @Test
    fun testMaxPitch() {
        Assert.assertEquals(
            TrackAsiaConstants.MAXIMUM_PITCH.toDouble(),
            TrackAsiaMapOptions().maxPitchPreference,
            DELTA
        )
        Assert.assertEquals(
            5.0,
            TrackAsiaMapOptions().maxPitchPreference(5.0).maxPitchPreference,
            DELTA
        )
        Assert.assertNotEquals(
            2.0,
            TrackAsiaMapOptions().maxPitchPreference(5.0).maxPitchPreference,
            DELTA
        )
    }

    @Test
    fun testTiltGesturesEnabled() {
        Assert.assertTrue(TrackAsiaMapOptions().tiltGesturesEnabled)
        Assert.assertTrue(TrackAsiaMapOptions().tiltGesturesEnabled(true).tiltGesturesEnabled)
        Assert.assertFalse(TrackAsiaMapOptions().tiltGesturesEnabled(false).tiltGesturesEnabled)
    }

    @Test
    fun testScrollGesturesEnabled() {
        Assert.assertTrue(TrackAsiaMapOptions().scrollGesturesEnabled)
        Assert.assertTrue(TrackAsiaMapOptions().scrollGesturesEnabled(true).scrollGesturesEnabled)
        Assert.assertFalse(TrackAsiaMapOptions().scrollGesturesEnabled(false).scrollGesturesEnabled)
    }

    @Test
    fun testHorizontalScrollGesturesEnabled() {
        Assert.assertTrue(TrackAsiaMapOptions().horizontalScrollGesturesEnabled)
        Assert.assertTrue(TrackAsiaMapOptions().horizontalScrollGesturesEnabled(true).horizontalScrollGesturesEnabled)
        Assert.assertFalse(TrackAsiaMapOptions().horizontalScrollGesturesEnabled(false).horizontalScrollGesturesEnabled)
    }

    @Test
    fun testZoomGesturesEnabled() {
        Assert.assertTrue(TrackAsiaMapOptions().zoomGesturesEnabled)
        Assert.assertTrue(TrackAsiaMapOptions().zoomGesturesEnabled(true).zoomGesturesEnabled)
        Assert.assertFalse(TrackAsiaMapOptions().zoomGesturesEnabled(false).zoomGesturesEnabled)
    }

    @Test
    fun testRotateGesturesEnabled() {
        Assert.assertTrue(TrackAsiaMapOptions().rotateGesturesEnabled)
        Assert.assertTrue(TrackAsiaMapOptions().rotateGesturesEnabled(true).rotateGesturesEnabled)
        Assert.assertFalse(TrackAsiaMapOptions().rotateGesturesEnabled(false).rotateGesturesEnabled)
    }

    @Test
    fun testCamera() {
        val position = CameraPosition.Builder().build()
        Assert.assertEquals(
            CameraPosition.Builder(position).build(),
            TrackAsiaMapOptions().camera(position).camera
        )
        Assert.assertNotEquals(
            CameraPosition.Builder().target(LatLng(1.0, 1.0)),
            TrackAsiaMapOptions().camera(position)
        )
        Assert.assertNull(TrackAsiaMapOptions().camera)
    }

    @Test
    fun testPrefetchesTiles() {
        // Default value
        Assert.assertTrue(TrackAsiaMapOptions().prefetchesTiles)

        // Check mutations
        Assert.assertTrue(TrackAsiaMapOptions().setPrefetchesTiles(true).prefetchesTiles)
        Assert.assertFalse(TrackAsiaMapOptions().setPrefetchesTiles(false).prefetchesTiles)
    }

    @Test
    fun testPrefetchZoomDelta() {
        // Default value
        Assert.assertEquals(4, TrackAsiaMapOptions().prefetchZoomDelta)

        // Check mutations
        Assert.assertEquals(
            5,
            TrackAsiaMapOptions().setPrefetchZoomDelta(5).prefetchZoomDelta
        )
    }

    @Test
    fun testCrossSourceCollisions() {
        // Default value
        Assert.assertTrue(TrackAsiaMapOptions().crossSourceCollisions)

        // check mutations
        Assert.assertTrue(TrackAsiaMapOptions().crossSourceCollisions(true).crossSourceCollisions)
        Assert.assertFalse(TrackAsiaMapOptions().crossSourceCollisions(false).crossSourceCollisions)
    }

    @Test
    fun testLocalIdeographFontFamily_enabledByDefault() {
        val options = TrackAsiaMapOptions.createFromAttributes(RuntimeEnvironment.application, null)
        Assert.assertEquals(
            TrackAsiaConstants.DEFAULT_FONT,
            options.localIdeographFontFamily
        )
    }

    companion object {
        private const val DELTA = 1e-15
    }
}
