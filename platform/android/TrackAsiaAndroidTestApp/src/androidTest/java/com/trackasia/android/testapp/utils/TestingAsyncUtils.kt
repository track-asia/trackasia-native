package com.trackasia.android.testapp.utils

import androidx.test.espresso.UiController
import com.trackasia.android.maps.MapView

object TestingAsyncUtils {
    private const val DEFAULT_TIMEOUT = 15_000L

    fun waitForLayer(
        uiController: UiController,
        mapView: MapView,
    ) {
        val start = System.nanoTime() / 1E6
        var isIdle = false
        mapView.addOnDidBecomeIdleListener { isIdle = true }

        while (!isIdle && System.nanoTime() / 1E6 - start < DEFAULT_TIMEOUT) {
            uiController.loopMainThreadForAtLeast(100)
        }
    }
}
