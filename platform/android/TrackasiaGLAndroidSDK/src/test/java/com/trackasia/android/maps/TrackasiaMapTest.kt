package com.trackasia.android.maps

import android.content.Context
import com.trackasia.android.TrackasiaInjector
import com.trackasia.android.camera.CameraPosition
import com.trackasia.android.camera.CameraUpdateFactory
import com.trackasia.android.constants.TrackasiaConstants
import com.trackasia.android.geometry.LatLng
import com.trackasia.android.geometry.LatLngBounds
import com.trackasia.android.style.layers.TransitionOptions
import com.trackasia.android.utils.ConfigUtils
import io.mockk.*
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class TrackasiaMapTest {

    private lateinit var trackasiaMap: TrackasiaMap

    private lateinit var nativeMapView: NativeMap

    private lateinit var transform: Transform

    private lateinit var cameraChangeDispatcher: CameraChangeDispatcher

    private lateinit var developerAnimationListener: TrackasiaMap.OnDeveloperAnimationListener

    @Mock
    private lateinit var context: Context

    @Mock
    private lateinit var appContext: Context

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        TrackasiaInjector.inject(context, "abcdef", ConfigUtils.getMockedOptions())
        cameraChangeDispatcher = spyk()
        developerAnimationListener = mockk(relaxed = true)
        nativeMapView = mockk(relaxed = true)
        transform = mockk(relaxed = true)
        trackasiaMap = TrackasiaMap(
            nativeMapView,
            transform,
            mockk(relaxed = true),
            null,
            null,
            cameraChangeDispatcher,
            listOf(developerAnimationListener)
        )
        every { nativeMapView.isDestroyed } returns false
        every { nativeMapView.nativePtr } returns 5
        trackasiaMap.injectLocationComponent(spyk())
        trackasiaMap.setStyle(Style.getPredefinedStyle("Streets"))
        trackasiaMap.onFinishLoadingStyle()
    }

    @Test
    fun testTransitionOptions() {
        val expected = TransitionOptions(100, 200)
        trackasiaMap.style?.transition = expected
        verify { nativeMapView.transitionOptions = expected }
    }

    @Test
    fun testMoveCamera() {
        val callback = mockk<TrackasiaMap.CancelableCallback>()
        val target = LatLng(1.0, 2.0)
        val expected = CameraPosition.Builder().target(target).build()
        val update = CameraUpdateFactory.newCameraPosition(expected)
        trackasiaMap.moveCamera(update, callback)
        verify { transform.moveCamera(trackasiaMap, update, callback) }
        verify { developerAnimationListener.onDeveloperAnimationStarted() }
    }

    @Test
    fun testEaseCamera() {
        val callback = mockk<TrackasiaMap.CancelableCallback>()
        val target = LatLng(1.0, 2.0)
        val expected = CameraPosition.Builder().target(target).build()
        val update = CameraUpdateFactory.newCameraPosition(expected)
        trackasiaMap.easeCamera(update, callback)
        verify { transform.easeCamera(trackasiaMap, update, TrackasiaConstants.ANIMATION_DURATION, true, callback) }
        verify { developerAnimationListener.onDeveloperAnimationStarted() }
    }

    @Test
    fun testAnimateCamera() {
        val callback = mockk<TrackasiaMap.CancelableCallback>()
        val target = LatLng(1.0, 2.0)
        val expected = CameraPosition.Builder().target(target).build()
        val update = CameraUpdateFactory.newCameraPosition(expected)
        trackasiaMap.animateCamera(update, callback)
        verify { transform.animateCamera(trackasiaMap, update, TrackasiaConstants.ANIMATION_DURATION, callback) }
        verify { developerAnimationListener.onDeveloperAnimationStarted() }
    }

    @Test
    fun testScrollBy() {
        trackasiaMap.scrollBy(100f, 200f)
        verify { nativeMapView.moveBy(100.0, 200.0, 0) }
        verify { developerAnimationListener.onDeveloperAnimationStarted() }
    }

    @Test
    fun testResetNorth() {
        trackasiaMap.resetNorth()
        verify { transform.resetNorth() }
        verify { developerAnimationListener.onDeveloperAnimationStarted() }
    }

    @Test
    fun testFocalBearing() {
        trackasiaMap.setFocalBearing(35.0, 100f, 200f, 1000)
        verify { transform.setBearing(35.0, 100f, 200f, 1000) }
        verify { developerAnimationListener.onDeveloperAnimationStarted() }
    }

    @Test
    fun testMinZoom() {
        trackasiaMap.setMinZoomPreference(10.0)
        verify { transform.minZoom = 10.0 }
    }

    @Test
    fun testMaxZoom() {
        trackasiaMap.setMaxZoomPreference(10.0)
        verify { transform.maxZoom = 10.0 }
    }

    @Test
    fun testMinPitch() {
        trackasiaMap.setMinPitchPreference(10.0)
        verify { transform.minPitch = 10.0 }
    }

    @Test
    fun testMaxPitch() {
        trackasiaMap.setMaxPitchPreference(10.0)
        verify { transform.maxPitch = 10.0 }
    }

    @Test
    fun testFpsListener() {
        val fpsChangedListener = mockk<TrackasiaMap.OnFpsChangedListener>()
        trackasiaMap.onFpsChangedListener = fpsChangedListener
        assertEquals("Listener should match", fpsChangedListener, trackasiaMap.onFpsChangedListener)
    }

    @Test
    fun testTilePrefetch() {
        trackasiaMap.prefetchesTiles = true
        verify { nativeMapView.prefetchTiles = true }
    }

    @Test
    fun testGetPrefetchZoomDelta() {
        every { nativeMapView.prefetchZoomDelta } answers { 3 }
        assertEquals(3, trackasiaMap.prefetchZoomDelta)
    }

    @Test
    fun testSetPrefetchZoomDelta() {
        trackasiaMap.prefetchZoomDelta = 2
        verify { nativeMapView.prefetchZoomDelta = 2 }
    }

    @Test
    fun testCameraForLatLngBounds() {
        val bounds = LatLngBounds.Builder().include(LatLng()).include(LatLng(1.0, 1.0)).build()
        trackasiaMap.setLatLngBoundsForCameraTarget(bounds)
        verify { nativeMapView.setLatLngBounds(bounds) }
    }

    @Test(expected = IllegalArgumentException::class)
    fun testAnimateCameraChecksDurationPositive() {
        trackasiaMap.animateCamera(CameraUpdateFactory.newLatLng(LatLng(30.0, 30.0)), 0, null)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testEaseCameraChecksDurationPositive() {
        trackasiaMap.easeCamera(CameraUpdateFactory.newLatLng(LatLng(30.0, 30.0)), 0, null)
    }

    @Test
    fun testGetNativeMapPtr() {
        assertEquals(5, trackasiaMap.nativeMapPtr)
    }

    @Test
    fun testNativeMapIsNotCalledOnStateSave() {
        clearMocks(nativeMapView)
        trackasiaMap.onSaveInstanceState(mockk(relaxed = true))
        verify { nativeMapView wasNot Called }
    }

    @Test
    fun testCameraChangeDispatcherCleared() {
        trackasiaMap.onDestroy()
        verify { cameraChangeDispatcher.onDestroy() }
    }

    @Test
    fun testStyleClearedOnDestroy() {
        val style = mockk<Style>(relaxed = true)
        val builder = mockk<Style.Builder>(relaxed = true)
        every { builder.build(nativeMapView) } returns style
        trackasiaMap.setStyle(builder)

        trackasiaMap.onDestroy()
        verify(exactly = 1) { style.clear() }
    }

    @Test
    fun testStyleCallbackNotCalledWhenPreviousFailed() {
        val style = mockk<Style>(relaxed = true)
        val builder = mockk<Style.Builder>(relaxed = true)
        every { builder.build(nativeMapView) } returns style
        val onStyleLoadedListener = mockk<Style.OnStyleLoaded>(relaxed = true)

        trackasiaMap.setStyle(builder, onStyleLoadedListener)
        trackasiaMap.onFailLoadingStyle()
        trackasiaMap.setStyle(builder, onStyleLoadedListener)
        trackasiaMap.onFinishLoadingStyle()
        verify(exactly = 1) { onStyleLoadedListener.onStyleLoaded(style) }
    }

    @Test
    fun testStyleCallbackNotCalledWhenPreviousNotFinished() {
        // regression test for #14337
        val style = mockk<Style>(relaxed = true)
        val builder = mockk<Style.Builder>(relaxed = true)
        every { builder.build(nativeMapView) } returns style
        val onStyleLoadedListener = mockk<Style.OnStyleLoaded>(relaxed = true)

        trackasiaMap.setStyle(builder, onStyleLoadedListener)
        trackasiaMap.setStyle(builder, onStyleLoadedListener)
        trackasiaMap.onFinishLoadingStyle()
        verify(exactly = 1) { onStyleLoadedListener.onStyleLoaded(style) }
    }
}
