package org.trackasia.android.maps

import androidx.test.espresso.UiController
import org.trackasia.android.camera.CameraUpdateFactory
import org.trackasia.android.geometry.LatLng
import org.trackasia.android.testapp.action.trackasiaMapAction.invoke
import org.trackasia.android.testapp.activity.EspressoTest
import org.junit.Assert.assertEquals
import org.junit.Test

class TransformTest : EspressoTest() {

    companion object {
        val initialCameraUpdate = CameraUpdateFactory.newLatLngZoom(LatLng(12.0, 12.0), 12.0)!!
    }

    @Test
    fun mapboxMapScrollByWithPadding() {
        validateTestSetup()
        invoke(trackasiaMap) { _: UiController, trackasiaMap: trackasiaMap ->
            trackasiaMap.moveCamera(initialCameraUpdate)
            trackasiaMap.scrollBy(400.0f, 0.0f)
            val expectedCameraPosition = trackasiaMap.cameraPosition

            trackasiaMap.moveCamera(initialCameraUpdate)
            trackasiaMap.setPadding(250, 250, 0, 0)
            trackasiaMap.scrollBy(400.0f, 0.0f)
            val actualCameraPosition = trackasiaMap.cameraPosition

            assertEquals("Camera position should match", expectedCameraPosition, actualCameraPosition)
        }
    }
}
