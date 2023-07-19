package com.trackasia.android.maps

import androidx.test.espresso.UiController
import com.trackasia.android.camera.CameraUpdateFactory
import com.trackasia.android.geometry.LatLng
import com.trackasia.android.testapp.action.TrackasiaMapAction.invoke
import com.trackasia.android.testapp.activity.EspressoTest
import org.junit.Assert.assertEquals
import org.junit.Test

class TransformTest : EspressoTest() {

    companion object {
        val initialCameraUpdate = CameraUpdateFactory.newLatLngZoom(LatLng(12.0, 12.0), 12.0)!!
    }

    @Test
    fun mapboxMapScrollByWithPadding() {
        validateTestSetup()
        invoke(trackasiaMap) { _: UiController, trackasiaMap: TrackasiaMap ->
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
