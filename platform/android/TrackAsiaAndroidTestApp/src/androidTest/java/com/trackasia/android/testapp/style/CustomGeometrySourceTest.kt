package com.trackasia.android.testapp.style

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import com.trackasia.android.style.sources.CustomGeometrySource.Companion.THREAD_POOL_LIMIT
import com.trackasia.android.style.sources.CustomGeometrySource.Companion.THREAD_PREFIX
import com.trackasia.android.testapp.action.OrientationAction.orientationLandscape
import com.trackasia.android.testapp.action.OrientationAction.orientationPortrait
import com.trackasia.android.testapp.action.TrackAsiaMapAction.invoke
import com.trackasia.android.testapp.action.WaitAction
import com.trackasia.android.testapp.activity.BaseTest
import com.trackasia.android.testapp.activity.style.GridSourceActivity
import com.trackasia.android.testapp.activity.style.GridSourceActivity.Companion.ID_GRID_LAYER
import com.trackasia.android.testapp.activity.style.GridSourceActivity.Companion.ID_GRID_SOURCE
import com.trackasia.android.testapp.utils.TestingAsyncUtils
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Ignore
import org.junit.Test

class CustomGeometrySourceTest : BaseTest() {
    override fun getActivityClass(): Class<*> = GridSourceActivity::class.java

    @Test
    fun sourceNotLeakingThreadsTest() {
        validateTestSetup()
        WaitAction.invoke(4000)
        onView(isRoot()).perform(orientationLandscape())
        WaitAction.invoke(2000)
        onView(isRoot()).perform(orientationPortrait())
        WaitAction.invoke(2000)
        Assert.assertFalse(
            "Threads should be shutdown when the source is destroyed.",
            Thread
                .getAllStackTraces()
                .keys
                .filter {
                    it.name.startsWith(THREAD_PREFIX)
                }.count() > THREAD_POOL_LIMIT,
        )
    }

    @Test
    fun threadsShutdownWhenSourceRemovedTest() {
        validateTestSetup()
        invoke(trackasiaMap) { uiController, trackasiaMap ->
            trackasiaMap.style!!.removeLayer(ID_GRID_LAYER)
            TestingAsyncUtils.waitForLayer(uiController, mapView)
            trackasiaMap.style!!.removeSource(ID_GRID_SOURCE)
            TestingAsyncUtils.waitForLayer(uiController, mapView)
            Assert.assertTrue(
                "There should be no threads running when the source is removed.",
                Thread
                    .getAllStackTraces()
                    .keys
                    .filter {
                        it.name.startsWith(THREAD_PREFIX)
                    }.count() == 0,
            )
        }
    }

    @Ignore("https://github.com/trackasia/trackasia-native/issues/2488")
    @Test
    fun threadsRestartedWhenSourceReAddedTest() {
        validateTestSetup()
        invoke(trackasiaMap) { uiController, trackasiaMap ->
            trackasiaMap.style!!.removeLayer((rule.activity as GridSourceActivity).layer!!)
            TestingAsyncUtils.waitForLayer(uiController, mapView)
            trackasiaMap.style!!.removeSource(ID_GRID_SOURCE)
            TestingAsyncUtils.waitForLayer(uiController, mapView)
            trackasiaMap.style!!.addSource((rule.activity as GridSourceActivity).source!!)
            trackasiaMap.style!!.addLayer((rule.activity as GridSourceActivity).layer!!)
            TestingAsyncUtils.waitForLayer(uiController, mapView)
            Assert.assertTrue(
                "Threads should be restarted when the source is re-added to the map.",
                Thread
                    .getAllStackTraces()
                    .keys
                    .filter {
                        it.name.startsWith(THREAD_PREFIX)
                    }.count() == THREAD_POOL_LIMIT,
            )
        }
    }

    @Test
    fun sourceZoomDeltaTest() {
        validateTestSetup()
        invoke(trackasiaMap) { uiController, trackasiaMap ->
            trackasiaMap.prefetchZoomDelta = 3
            trackasiaMap.style!!.getSource(ID_GRID_SOURCE)!!.let {
                assertNull(it.prefetchZoomDelta)
                it.prefetchZoomDelta = 5
                assertNotNull(it.prefetchZoomDelta)
                assertEquals(5, it.prefetchZoomDelta!!)
                it.prefetchZoomDelta = null
                assertNull(it.prefetchZoomDelta)
            }
        }
    }

    @Test
    fun isVolatileTest() {
        validateTestSetup()
        invoke(trackasiaMap) { uiController, trackasiaMap ->
            trackasiaMap.style!!.getSource(ID_GRID_SOURCE)!!.let {
                assertFalse(it.isVolatile)
                it.isVolatile = true
                assertTrue(it.isVolatile)
            }
        }
    }

    @Test
    fun minimumTileUpdateIntervalTest() {
        validateTestSetup()
        invoke(trackasiaMap) { uiController, trackasiaMap ->
            trackasiaMap.style!!.getSource(ID_GRID_SOURCE)!!.let {
                assertEquals(0, it.minimumTileUpdateInterval)
                it.minimumTileUpdateInterval = 1000
                assertEquals(1000, it.minimumTileUpdateInterval)
            }
        }
    }
}
