package com.trackasia.android.maps

import androidx.test.espresso.UiController
import com.trackasia.android.camera.CameraPosition
import com.trackasia.android.camera.CameraUpdateFactory
import com.trackasia.android.geometry.LatLng
import com.trackasia.android.testapp.action.TrackAsiaMapAction.invoke
import com.trackasia.android.testapp.activity.EspressoTest
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.math.abs

fun almostEquals(
    first: Double,
    second: Double,
    delta: Double,
): Boolean = abs(first - second) < delta

fun cameraPositionAlmostEquals(
    first: CameraPosition,
    second: CameraPosition,
    delta: Double = 0.00001,
): Boolean {
    if (delta == 0.0) return first == second
    if (first == second) return true

    if (!almostEquals(first.zoom, second.zoom, delta) ||
        !almostEquals(first.tilt, second.tilt, delta) ||
        !almostEquals(first.bearing, second.bearing, delta)
    ) {
        return false
    }

    if (first.padding != null && second.padding != null) {
        // !! needed here https://discuss.kotlinlang.org/t/what-is-the-reason-behind-smart-cast-being-impossible-to-perform-when-referenced-class-is-in-another-module/2201
        if (first.padding!!.size != second.padding!!.size) return false
        for (index in first.padding!!.indices) {
            if (!almostEquals(first.padding!![index], second.padding!![index], delta)) return false
        }
    }

    if (first.target != null && second.target != null) {
        if (!almostEquals(first.target!!.altitude, second.target!!.altitude, delta) ||
            !almostEquals(first.target!!.latitude, second.target!!.latitude, delta) ||
            !almostEquals(first.target!!.longitude, second.target!!.longitude, delta)
        ) {
            return false
        }
    }

    return true
}

class TransformTest : EspressoTest() {
    companion object {
        val initialCameraUpdate = CameraUpdateFactory.newLatLngZoom(LatLng(12.0, 12.0), 12.0)
    }

    @Test
    fun trackasiaMapScrollByWithPadding() {
        validateTestSetup()
        invoke(trackasiaMap) { _: UiController, trackasiaMap: TrackAsiaMap ->
            trackasiaMap.moveCamera(initialCameraUpdate)
            trackasiaMap.scrollBy(400.0f, 0.0f)
            val expectedCameraPosition = trackasiaMap.cameraPosition

            trackasiaMap.moveCamera(initialCameraUpdate)
            trackasiaMap.setPadding(250, 250, 0, 0)
            trackasiaMap.scrollBy(400.0f, 0.0f)
            val actualCameraPosition = trackasiaMap.cameraPosition

            assertTrue(
                "Camera position should match $expectedCameraPosition $actualCameraPosition",
                cameraPositionAlmostEquals(expectedCameraPosition, actualCameraPosition),
            )
        }
    }
}
