package com.trackasia.android.location

import android.graphics.PointF
import android.graphics.RectF
import android.location.Location
import com.trackasia.android.camera.CameraPosition
import com.trackasia.android.camera.CameraUpdate
import com.trackasia.android.camera.CameraUpdateFactory.newCameraPosition
import com.trackasia.android.geometry.LatLng
import com.trackasia.android.gestures.AndroidGesturesManager
import com.trackasia.android.gestures.MoveGestureDetector
import com.trackasia.android.location.TrackAsiaAnimator.AnimationsValueChangeListener
import com.trackasia.android.location.modes.CameraMode
import com.trackasia.android.maps.Projection
import com.trackasia.android.maps.TrackAsiaMap
import com.trackasia.android.maps.TrackAsiaMap.CancelableCallback
import com.trackasia.android.maps.TrackAsiaMap.OnCameraMoveListener
import com.trackasia.android.maps.Transform
import com.trackasia.android.maps.UiSettings
import org.junit.Assert
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers
import org.mockito.Mockito

class LocationCameraControllerTest {
    @Test
    fun setCameraMode_mapTransitionsAreCancelled() {
        val trackasiaMap = Mockito.mock(TrackAsiaMap::class.java)
        val camera = buildCamera(trackasiaMap)
        camera.initializeOptions(
            Mockito.mock(
                LocationComponentOptions::class.java,
            ),
        )
        camera.setEnabled(true)
        camera.cameraMode = CameraMode.TRACKING_GPS
        Mockito.verify(trackasiaMap).cancelTransitions()
    }

    @Test
    fun setCameraMode_gestureThresholdIsAdjusted() {
        val moveGestureDetector =
            Mockito.mock(
                MoveGestureDetector::class.java,
            )
        val camera = buildCamera(moveGestureDetector)
        val options =
            Mockito.mock(
                LocationComponentOptions::class.java,
            )
        val moveThreshold = 5f
        Mockito.`when`(options.trackingInitialMoveThreshold()).thenReturn(moveThreshold)
        Mockito.`when`(options.trackingGesturesManagement()).thenReturn(true)
        camera.initializeOptions(options)
        camera.cameraMode = CameraMode.TRACKING_GPS
        Mockito.verify(moveGestureDetector).moveThreshold = moveThreshold
        Mockito.verify(moveGestureDetector, Mockito.times(0)).moveThresholdRect =
            ArgumentMatchers.any(
                RectF::class.java,
            )
    }

    @Test
    fun setCameraMode_gestureThresholdNotAdjustedWhenDisabled() {
        val moveGestureDetector =
            Mockito.mock(
                MoveGestureDetector::class.java,
            )
        val camera = buildCamera(moveGestureDetector)
        val options =
            Mockito.mock(
                LocationComponentOptions::class.java,
            )
        val moveThreshold = 5f
        Mockito.`when`(options.trackingInitialMoveThreshold()).thenReturn(moveThreshold)
        Mockito.`when`(options.trackingGesturesManagement()).thenReturn(false)
        camera.initializeOptions(options)
        camera.cameraMode = CameraMode.TRACKING_GPS
        Mockito.verify(moveGestureDetector, Mockito.times(0)).moveThreshold = moveThreshold
        Mockito.verify(moveGestureDetector, Mockito.times(0)).moveThreshold = 0f
        Mockito.verify(moveGestureDetector, Mockito.times(0)).moveThresholdRect =
            ArgumentMatchers.any(
                RectF::class.java,
            )
    }

    @Test
    fun setCameraMode_gestureThresholdIsResetWhenNotTracking() {
        val moveGestureDetector =
            Mockito.mock(
                MoveGestureDetector::class.java,
            )
        val camera = buildCamera(moveGestureDetector)
        val options =
            Mockito.mock(
                LocationComponentOptions::class.java,
            )
        Mockito.`when`(options.trackingGesturesManagement()).thenReturn(true)
        camera.initializeOptions(options)
        camera.cameraMode = CameraMode.NONE
        Mockito.verify(moveGestureDetector, Mockito.times(2)).moveThreshold =
            0f // one for initialization
        Mockito.verify(moveGestureDetector, Mockito.times(2)).moveThresholdRect =
            null // one for initialization
    }

    @Test
    fun setCameraMode_notTrackingAdjustsFocalPoint() {
        val trackasiaMap = Mockito.mock(TrackAsiaMap::class.java)
        Mockito.`when`(trackasiaMap.uiSettings).thenReturn(
            Mockito.mock(
                UiSettings::class.java,
            ),
        )
        val camera = buildCamera(trackasiaMap)
        camera.initializeOptions(
            Mockito.mock(
                LocationComponentOptions::class.java,
            ),
        )
        camera.cameraMode = CameraMode.TRACKING_GPS
        camera.cameraMode = CameraMode.NONE
        Mockito.verify(trackasiaMap.uiSettings).focalPoint = null
    }

    @Test
    fun setCameraMode_trackingChangeListenerCameraDismissedIsCalled() {
        val internalTrackingChangedListener =
            Mockito.mock(
                OnCameraTrackingChangedListener::class.java,
            )
        val camera = buildCamera(internalTrackingChangedListener)
        camera.initializeOptions(
            Mockito.mock(
                LocationComponentOptions::class.java,
            ),
        )
        camera.cameraMode = CameraMode.TRACKING_GPS
        camera.cameraMode = CameraMode.NONE
        Mockito.verify(internalTrackingChangedListener).onCameraTrackingDismissed()
    }

    @Test
    fun setCameraMode_internalCameraTrackingChangeListenerIsCalled() {
        val internalTrackingChangedListener =
            Mockito.mock(
                OnCameraTrackingChangedListener::class.java,
            )
        val camera = buildCamera(internalTrackingChangedListener)
        camera.initializeOptions(
            Mockito.mock(
                LocationComponentOptions::class.java,
            ),
        )
        val cameraMode = CameraMode.NONE
        camera.cameraMode = cameraMode
        Mockito.verify(internalTrackingChangedListener).onCameraTrackingChanged(cameraMode)
    }

    @Test
    fun setCameraMode_doNotNotifyAboutDuplicates_NONE() {
        val internalTrackingChangedListener =
            Mockito.mock(
                OnCameraTrackingChangedListener::class.java,
            )
        val camera = buildCamera(internalTrackingChangedListener)
        camera.initializeOptions(
            Mockito.mock(
                LocationComponentOptions::class.java,
            ),
        )
        val cameraMode = CameraMode.NONE
        camera.cameraMode = cameraMode
        camera.cameraMode = cameraMode
        Mockito
            .verify(internalTrackingChangedListener, Mockito.times(1))
            .onCameraTrackingChanged(cameraMode)
    }

    @Test
    fun setCameraMode_doNotNotifyAboutDuplicates_TRACKING_GPS() {
        val internalTrackingChangedListener =
            Mockito.mock(
                OnCameraTrackingChangedListener::class.java,
            )
        val camera = buildCamera(internalTrackingChangedListener)
        camera.initializeOptions(
            Mockito.mock(
                LocationComponentOptions::class.java,
            ),
        )
        val cameraMode = CameraMode.TRACKING_GPS
        camera.cameraMode = cameraMode
        camera.cameraMode = cameraMode
        Mockito
            .verify(internalTrackingChangedListener, Mockito.times(1))
            .onCameraTrackingChanged(cameraMode)
    }

    @Test
    fun setCameraMode_cancelTransitionsWhenSet() {
        val trackasiaMap = Mockito.mock(TrackAsiaMap::class.java)
        Mockito.`when`(trackasiaMap.uiSettings).thenReturn(
            Mockito.mock(
                UiSettings::class.java,
            ),
        )
        Mockito.`when`(trackasiaMap.projection).thenReturn(
            Mockito.mock(
                Projection::class.java,
            ),
        )
        val camera = buildCamera(trackasiaMap)
        camera.initializeOptions(
            Mockito.mock(
                LocationComponentOptions::class.java,
            ),
        )
        camera.cameraMode = CameraMode.NONE_COMPASS
        Mockito.verify(trackasiaMap, Mockito.times(1)).cancelTransitions()
        camera.cameraMode = CameraMode.NONE_GPS
        Mockito.verify(trackasiaMap, Mockito.times(2)).cancelTransitions()
        camera.cameraMode = CameraMode.TRACKING
        Mockito.verify(trackasiaMap, Mockito.times(3)).cancelTransitions()
        camera.cameraMode = CameraMode.TRACKING_COMPASS
        Mockito.verify(trackasiaMap, Mockito.times(4)).cancelTransitions()
        camera.cameraMode = CameraMode.TRACKING_GPS
        Mockito.verify(trackasiaMap, Mockito.times(5)).cancelTransitions()
        camera.cameraMode = CameraMode.TRACKING_GPS_NORTH
        Mockito.verify(trackasiaMap, Mockito.times(6)).cancelTransitions()
    }

    @Test
    fun setCameraMode_dontCancelTransitionsWhenNoneSet() {
        val trackasiaMap = Mockito.mock(TrackAsiaMap::class.java)
        Mockito.`when`(trackasiaMap.uiSettings).thenReturn(
            Mockito.mock(
                UiSettings::class.java,
            ),
        )
        Mockito.`when`(trackasiaMap.projection).thenReturn(
            Mockito.mock(
                Projection::class.java,
            ),
        )
        val camera = buildCamera(trackasiaMap)
        camera.initializeOptions(
            Mockito.mock(
                LocationComponentOptions::class.java,
            ),
        )
        camera.cameraMode = CameraMode.NONE
        Mockito.verify(trackasiaMap, Mockito.never()).cancelTransitions()
    }

    @Test
    fun onNewLatLngValue_cameraModeTrackingUpdatesLatLng() {
        val transform =
            Mockito.mock(
                Transform::class.java,
            )
        val camera = buildCamera(transform)
        camera.initializeOptions(
            Mockito.mock(
                LocationComponentOptions::class.java,
            ),
        )
        camera.cameraMode = CameraMode.TRACKING
        val latLng = Mockito.mock(LatLng::class.java)
        getAnimationListener<Any>(
            TrackAsiaAnimator.ANIMATOR_CAMERA_LATLNG,
            camera.animationListeners,
        )!!.onNewAnimationValue(latLng)
        Mockito.verify(transform).moveCamera(
            ArgumentMatchers.any(
                TrackAsiaMap::class.java,
            ),
            ArgumentMatchers.any(CameraUpdate::class.java),
            ArgumentMatchers.nullable(CancelableCallback::class.java),
        )
    }

    @Test
    fun onNewLatLngValue_cameraModeTrackingGpsNorthUpdatesLatLng() {
        val transform =
            Mockito.mock(
                Transform::class.java,
            )
        val camera = buildCamera(transform)
        camera.initializeOptions(
            Mockito.mock(
                LocationComponentOptions::class.java,
            ),
        )
        camera.cameraMode = CameraMode.TRACKING_GPS_NORTH
        val latLng = Mockito.mock(LatLng::class.java)
        getAnimationListener<Any>(
            TrackAsiaAnimator.ANIMATOR_CAMERA_LATLNG,
            camera.animationListeners,
        )!!.onNewAnimationValue(latLng)
        Mockito.verify(transform).moveCamera(
            ArgumentMatchers.any(
                TrackAsiaMap::class.java,
            ),
            ArgumentMatchers.any(CameraUpdate::class.java),
            ArgumentMatchers.nullable(CancelableCallback::class.java),
        )
    }

    @Test
    fun onNewLatLngValue_cameraModeTrackingGpsUpdatesLatLng() {
        val transform =
            Mockito.mock(
                Transform::class.java,
            )
        val camera = buildCamera(transform)
        camera.initializeOptions(
            Mockito.mock(
                LocationComponentOptions::class.java,
            ),
        )
        camera.cameraMode = CameraMode.TRACKING_GPS
        val latLng = Mockito.mock(LatLng::class.java)
        getAnimationListener<Any>(
            TrackAsiaAnimator.ANIMATOR_CAMERA_LATLNG,
            camera.animationListeners,
        )!!.onNewAnimationValue(latLng)
        Mockito.verify(transform).moveCamera(
            ArgumentMatchers.any(
                TrackAsiaMap::class.java,
            ),
            ArgumentMatchers.any(CameraUpdate::class.java),
            ArgumentMatchers.nullable(CancelableCallback::class.java),
        )
    }

    @Test
    fun onNewLatLngValue_cameraModeTrackingCompassUpdatesLatLng() {
        val transform =
            Mockito.mock(
                Transform::class.java,
            )
        val camera = buildCamera(transform)
        camera.initializeOptions(
            Mockito.mock(
                LocationComponentOptions::class.java,
            ),
        )
        camera.cameraMode = CameraMode.TRACKING_COMPASS
        val latLng = Mockito.mock(LatLng::class.java)
        getAnimationListener<Any>(
            TrackAsiaAnimator.ANIMATOR_CAMERA_LATLNG,
            camera.animationListeners,
        )!!.onNewAnimationValue(latLng)
        Mockito.verify(transform).moveCamera(
            ArgumentMatchers.any(
                TrackAsiaMap::class.java,
            ),
            ArgumentMatchers.any(CameraUpdate::class.java),
            ArgumentMatchers.nullable(CancelableCallback::class.java),
        )
    }

    @Test
    fun onNewLatLngValue_cameraModeNoneIgnored() {
        val transform =
            Mockito.mock(
                Transform::class.java,
            )
        val camera = buildCamera(transform)
        camera.initializeOptions(
            Mockito.mock(
                LocationComponentOptions::class.java,
            ),
        )
        camera.cameraMode = CameraMode.NONE
        Assert.assertNull(
            getAnimationListener<Any>(
                TrackAsiaAnimator.ANIMATOR_CAMERA_LATLNG,
                camera.animationListeners,
            ),
        )
        Mockito.verify(transform, Mockito.times(0)).moveCamera(
            ArgumentMatchers.any(
                TrackAsiaMap::class.java,
            ),
            ArgumentMatchers.any(CameraUpdate::class.java),
            ArgumentMatchers.nullable(CancelableCallback::class.java),
        )
    }

    @Test
    fun onNewLatLngValue_focalPointIsAdjusted() {
        val trackasiaMap = Mockito.mock(TrackAsiaMap::class.java)
        val transform =
            Mockito.mock(
                Transform::class.java,
            )
        val listener = arrayOf<OnCameraMoveListener?>(null)
        Mockito
            .doAnswer { invocation ->
                listener[0] = invocation.arguments[0] as OnCameraMoveListener
                null
            }.`when`(trackasiaMap)
            .addOnCameraMoveListener(
                ArgumentMatchers.any(
                    OnCameraMoveListener::class.java,
                ),
            )
        val projection =
            Mockito.mock(
                Projection::class.java,
            )
        val pointF = Mockito.mock(PointF::class.java)
        Mockito
            .`when`(
                projection.toScreenLocation(
                    ArgumentMatchers.any(
                        LatLng::class.java,
                    ),
                ),
            ).thenReturn(pointF)
        Mockito.`when`(trackasiaMap.projection).thenReturn(projection)
        val camera = buildCamera(trackasiaMap, transform)
        val options =
            Mockito.mock(
                LocationComponentOptions::class.java,
            )
        Mockito.`when`(options.trackingGesturesManagement()).thenReturn(true)
        camera.initializeOptions(options)
        camera.cameraMode = CameraMode.TRACKING
        val latLng = Mockito.mock(LatLng::class.java)
        getAnimationListener<Any>(
            TrackAsiaAnimator.ANIMATOR_CAMERA_LATLNG,
            camera.animationListeners,
        )!!.onNewAnimationValue(latLng)
        Mockito.verify(transform).moveCamera(
            ArgumentMatchers.any(
                TrackAsiaMap::class.java,
            ),
            ArgumentMatchers.any(CameraUpdate::class.java),
            ArgumentMatchers.nullable(CancelableCallback::class.java),
        )
        val uiSettings = Mockito.mock(UiSettings::class.java)
        Mockito.`when`(trackasiaMap.uiSettings).thenReturn(uiSettings)
        listener[0]!!.onCameraMove()
        Mockito.verify(uiSettings).focalPoint = pointF
    }

    @Test
    fun onNewGpsBearingValue_cameraModeTrackingGpsUpdatesBearing() {
        val transform =
            Mockito.mock(
                Transform::class.java,
            )
        val camera = buildCamera(transform)
        camera.initializeOptions(
            Mockito.mock(
                LocationComponentOptions::class.java,
            ),
        )
        camera.cameraMode = CameraMode.TRACKING_GPS
        val gpsBearing = 5f
        getAnimationListener<Any>(
            TrackAsiaAnimator.ANIMATOR_CAMERA_GPS_BEARING,
            camera.animationListeners,
        )!!.onNewAnimationValue(gpsBearing)
        Mockito.verify(transform).moveCamera(
            ArgumentMatchers.any(
                TrackAsiaMap::class.java,
            ),
            ArgumentMatchers.any(CameraUpdate::class.java),
            ArgumentMatchers.nullable(CancelableCallback::class.java),
        )
    }

    @Test
    fun onNewGpsBearingValue_cameraModeNoneGpsUpdatesBearing() {
        val transform =
            Mockito.mock(
                Transform::class.java,
            )
        val camera = buildCamera(transform)
        camera.initializeOptions(
            Mockito.mock(
                LocationComponentOptions::class.java,
            ),
        )
        camera.cameraMode = CameraMode.NONE_GPS
        val gpsBearing = 5f
        getAnimationListener<Any>(
            TrackAsiaAnimator.ANIMATOR_CAMERA_GPS_BEARING,
            camera.animationListeners,
        )!!.onNewAnimationValue(gpsBearing)
        Mockito.verify(transform).moveCamera(
            ArgumentMatchers.any(
                TrackAsiaMap::class.java,
            ),
            ArgumentMatchers.any(CameraUpdate::class.java),
            ArgumentMatchers.nullable(CancelableCallback::class.java),
        )
    }

    @Test
    fun onNewGpsBearingValue_cameraModeTrackingNorthUpdatesBearing() {
        val trackasiaMap = Mockito.mock(TrackAsiaMap::class.java)
        val transform =
            Mockito.mock(
                Transform::class.java,
            )
        val camera = buildCamera(trackasiaMap, transform)
        val cameraPosition = CameraPosition.Builder().bearing(7.0).build()
        Mockito.`when`(trackasiaMap.cameraPosition).thenReturn(cameraPosition)
        camera.initializeOptions(
            Mockito.mock(
                LocationComponentOptions::class.java,
            ),
        )
        camera.cameraMode = CameraMode.TRACKING_GPS_NORTH
        val gpsBearing = 5f
        getAnimationListener<Any>(
            TrackAsiaAnimator.ANIMATOR_CAMERA_GPS_BEARING,
            camera.animationListeners,
        )!!.onNewAnimationValue(gpsBearing)
        Mockito.verify(transform).moveCamera(
            ArgumentMatchers.eq(trackasiaMap),
            ArgumentMatchers.any(
                CameraUpdate::class.java,
            ),
            ArgumentMatchers.nullable(CancelableCallback::class.java),
        )
    }

    @Test
    fun onNewGpsBearingValue_cameraModeTrackingNorthBearingZeroIgnored() {
        val trackasiaMap = Mockito.mock(TrackAsiaMap::class.java)
        val transform =
            Mockito.mock(
                Transform::class.java,
            )
        val camera = buildCamera(trackasiaMap, transform)
        val cameraPosition = CameraPosition.Builder().bearing(0.0).build()
        Mockito.`when`(trackasiaMap.cameraPosition).thenReturn(cameraPosition)
        camera.initializeOptions(
            Mockito.mock(
                LocationComponentOptions::class.java,
            ),
        )
        camera.cameraMode = CameraMode.TRACKING_GPS_NORTH
        val gpsBearing = 5f
        getAnimationListener<Any>(
            TrackAsiaAnimator.ANIMATOR_CAMERA_GPS_BEARING,
            camera.animationListeners,
        )!!.onNewAnimationValue(gpsBearing)
        Mockito.verify(transform, Mockito.times(0)).moveCamera(
            ArgumentMatchers.eq(trackasiaMap),
            ArgumentMatchers.any(
                CameraUpdate::class.java,
            ),
            ArgumentMatchers.nullable(CancelableCallback::class.java),
        )
    }

    @Test
    fun onNewGpsBearingValue_cameraModeNoneIgnored() {
        val transform =
            Mockito.mock(
                Transform::class.java,
            )
        val camera = buildCamera(transform)
        camera.initializeOptions(
            Mockito.mock(
                LocationComponentOptions::class.java,
            ),
        )
        camera.cameraMode = CameraMode.NONE
        Assert.assertNull(
            getAnimationListener<Any>(
                TrackAsiaAnimator.ANIMATOR_CAMERA_GPS_BEARING,
                camera.animationListeners,
            ),
        )
        Mockito.verify(transform, Mockito.times(0)).moveCamera(
            ArgumentMatchers.any(
                TrackAsiaMap::class.java,
            ),
            ArgumentMatchers.any(CameraUpdate::class.java),
            ArgumentMatchers.nullable(CancelableCallback::class.java),
        )
    }

    @Test
    fun onNewCompassBearingValue_cameraModeTrackingCompassUpdatesBearing() {
        val transform =
            Mockito.mock(
                Transform::class.java,
            )
        val camera = buildCamera(transform)
        camera.initializeOptions(
            Mockito.mock(
                LocationComponentOptions::class.java,
            ),
        )
        camera.cameraMode = CameraMode.TRACKING_COMPASS
        val compassBearing = 5f
        getAnimationListener<Any>(
            TrackAsiaAnimator.ANIMATOR_CAMERA_COMPASS_BEARING,
            camera.animationListeners,
        )?.onNewAnimationValue(compassBearing)
        Mockito.verify(transform).moveCamera(
            ArgumentMatchers.any(
                TrackAsiaMap::class.java,
            ),
            ArgumentMatchers.any(CameraUpdate::class.java),
            ArgumentMatchers.nullable(CancelableCallback::class.java),
        )
    }

    @Test
    fun onNewCompassBearingValue_cameraModeNoneCompassUpdatesBearing() {
        val transform =
            Mockito.mock(
                Transform::class.java,
            )
        val camera = buildCamera(transform)
        camera.initializeOptions(
            Mockito.mock(
                LocationComponentOptions::class.java,
            ),
        )
        camera.cameraMode = CameraMode.NONE_COMPASS
        val compassBearing = 5f
        getAnimationListener<Any>(
            TrackAsiaAnimator.ANIMATOR_CAMERA_COMPASS_BEARING,
            camera.animationListeners,
        )?.onNewAnimationValue(compassBearing)
        Mockito.verify(transform).moveCamera(
            ArgumentMatchers.any(
                TrackAsiaMap::class.java,
            ),
            ArgumentMatchers.any(CameraUpdate::class.java),
            ArgumentMatchers.nullable(CancelableCallback::class.java),
        )
    }

    @Test
    fun onNewCompassBearingValue_cameraModeNoneIgnored() {
        val transform =
            Mockito.mock(
                Transform::class.java,
            )
        val camera = buildCamera(transform)
        camera.initializeOptions(
            Mockito.mock(
                LocationComponentOptions::class.java,
            ),
        )
        camera.cameraMode = CameraMode.NONE
        Assert.assertNull(
            getAnimationListener<Any>(
                TrackAsiaAnimator.ANIMATOR_CAMERA_COMPASS_BEARING,
                camera.animationListeners,
            ),
        )
        Mockito.verify(transform, Mockito.times(0)).moveCamera(
            ArgumentMatchers.any(
                TrackAsiaMap::class.java,
            ),
            ArgumentMatchers.any(CameraUpdate::class.java),
            ArgumentMatchers.nullable(CancelableCallback::class.java),
        )
    }

    @Test
    fun onNewZoomValue_cameraIsUpdated() {
        val transform =
            Mockito.mock(
                Transform::class.java,
            )
        val camera = buildCamera(transform)
        camera.initializeOptions(
            Mockito.mock(
                LocationComponentOptions::class.java,
            ),
        )
        camera.cameraMode = CameraMode.TRACKING
        val zoom = 5f
        getAnimationListener<Any>(
            TrackAsiaAnimator.ANIMATOR_ZOOM,
            camera.animationListeners,
        )!!.onNewAnimationValue(zoom)
        Mockito.verify(transform).moveCamera(
            ArgumentMatchers.any(
                TrackAsiaMap::class.java,
            ),
            ArgumentMatchers.any(CameraUpdate::class.java),
            ArgumentMatchers.nullable(CancelableCallback::class.java),
        )
    }

    @Test
    fun onNeTiltValue_cameraIsUpdated() {
        val transform =
            Mockito.mock(
                Transform::class.java,
            )
        val camera = buildCamera(transform)
        camera.initializeOptions(
            Mockito.mock(
                LocationComponentOptions::class.java,
            ),
        )
        camera.cameraMode = CameraMode.TRACKING
        val tilt = 5f
        getAnimationListener<Any>(
            TrackAsiaAnimator.ANIMATOR_TILT,
            camera.animationListeners,
        )!!.onNewAnimationValue(tilt)
        Mockito.verify(transform).moveCamera(
            ArgumentMatchers.any(
                TrackAsiaMap::class.java,
            ),
            ArgumentMatchers.any(CameraUpdate::class.java),
            ArgumentMatchers.nullable(CancelableCallback::class.java),
        )
    }

    @Test
    fun gesturesManagement_enabled() {
        val trackasiaMap = Mockito.mock(TrackAsiaMap::class.java)
        val initialGesturesManager =
            Mockito.mock(
                AndroidGesturesManager::class.java,
            )
        val internalGesturesManager =
            Mockito.mock(
                AndroidGesturesManager::class.java,
            )
        Mockito.`when`(trackasiaMap.gesturesManager).thenReturn(initialGesturesManager)
        val camera = buildCamera(trackasiaMap, initialGesturesManager, internalGesturesManager)
        val options =
            Mockito.mock(
                LocationComponentOptions::class.java,
            )
        Mockito.`when`(options.trackingGesturesManagement()).thenReturn(true)
        camera.initializeOptions(options)
        Mockito.verify(trackasiaMap).setGesturesManager(internalGesturesManager, true, true)
    }

    @Test
    fun gesturesManagement_disabled() {
        val trackasiaMap = Mockito.mock(TrackAsiaMap::class.java)
        val initialGesturesManager =
            Mockito.mock(
                AndroidGesturesManager::class.java,
            )
        val internalGesturesManager =
            Mockito.mock(
                AndroidGesturesManager::class.java,
            )
        Mockito.`when`(trackasiaMap.gesturesManager).thenReturn(internalGesturesManager)
        val camera = buildCamera(trackasiaMap, initialGesturesManager, internalGesturesManager)
        val options =
            Mockito.mock(
                LocationComponentOptions::class.java,
            )
        Mockito.`when`(options.trackingGesturesManagement()).thenReturn(false)
        camera.initializeOptions(options)
        Mockito.verify(trackasiaMap).setGesturesManager(initialGesturesManager, true, true)
    }

    @Test
    fun gesturesManagement_optionNotChangedInitial() {
        val trackasiaMap = Mockito.mock(TrackAsiaMap::class.java)
        val initialGesturesManager =
            Mockito.mock(
                AndroidGesturesManager::class.java,
            )
        val internalGesturesManager =
            Mockito.mock(
                AndroidGesturesManager::class.java,
            )
        Mockito.`when`(trackasiaMap.gesturesManager).thenReturn(initialGesturesManager)
        val camera = buildCamera(trackasiaMap, initialGesturesManager, internalGesturesManager)
        val options =
            Mockito.mock(
                LocationComponentOptions::class.java,
            )
        Mockito.`when`(options.trackingGesturesManagement()).thenReturn(false)
        camera.initializeOptions(options)
        Mockito
            .verify(trackasiaMap, Mockito.times(0))
            .setGesturesManager(initialGesturesManager, true, true)
    }

    @Test
    fun gesturesManagement_optionNotChangedInternal() {
        val trackasiaMap = Mockito.mock(TrackAsiaMap::class.java)
        val initialGesturesManager =
            Mockito.mock(
                AndroidGesturesManager::class.java,
            )
        val internalGesturesManager =
            Mockito.mock(
                AndroidGesturesManager::class.java,
            )
        Mockito.`when`(trackasiaMap.gesturesManager).thenReturn(internalGesturesManager)
        val camera = buildCamera(trackasiaMap, initialGesturesManager, internalGesturesManager)
        val options =
            Mockito.mock(
                LocationComponentOptions::class.java,
            )
        Mockito.`when`(options.trackingGesturesManagement()).thenReturn(true)
        camera.initializeOptions(options)
        Mockito
            .verify(trackasiaMap, Mockito.times(0))
            .setGesturesManager(internalGesturesManager, true, true)
    }

    @Test
    fun gesturesManagement_moveGesture_notTracking() {
        val moveGestureDetector =
            Mockito.mock(
                MoveGestureDetector::class.java,
            )
        Mockito.`when`(moveGestureDetector.pointersCount).thenReturn(1)
        val camera = buildCamera(moveGestureDetector)
        val options =
            Mockito.mock(
                LocationComponentOptions::class.java,
            )
        Mockito.`when`(options.trackingGesturesManagement()).thenReturn(true)
        val initial = 100f
        val multiFinger = 200f
        val multiFingerArea = Mockito.mock(RectF::class.java)
        Mockito.`when`(options.trackingInitialMoveThreshold()).thenReturn(initial)
        Mockito.`when`(options.trackingMultiFingerMoveThreshold()).thenReturn(multiFinger)
        Mockito.`when`(options.trackingMultiFingerProtectedMoveArea()).thenReturn(multiFingerArea)
        camera.initializeOptions(options)
        camera.onMoveListener.onMoveBegin(moveGestureDetector)
        Mockito.verify(moveGestureDetector, Mockito.times(2)).moveThreshold = 0f
        Mockito.verify(moveGestureDetector, Mockito.times(2)).moveThresholdRect = null
    }

    @Test
    fun gesturesManagement_moveGesture_singlePointer_tracking() {
        val moveGestureDetector =
            Mockito.mock(
                MoveGestureDetector::class.java,
            )
        Mockito.`when`(moveGestureDetector.pointersCount).thenReturn(1)
        val camera = buildCamera(moveGestureDetector)
        val options =
            Mockito.mock(
                LocationComponentOptions::class.java,
            )
        Mockito.`when`(options.trackingGesturesManagement()).thenReturn(true)
        val initial = 100f
        Mockito.`when`(options.trackingInitialMoveThreshold()).thenReturn(initial)
        camera.initializeOptions(options)
        camera.cameraMode = CameraMode.TRACKING
        Mockito.`when`(moveGestureDetector.moveThreshold).thenReturn(initial)
        camera.onMoveListener.onMoveBegin(moveGestureDetector)
        Mockito.verify(moveGestureDetector, Mockito.atMost(1)).moveThreshold = initial
        Mockito.verify(moveGestureDetector, Mockito.times(0)).moveThresholdRect =
            ArgumentMatchers.any(
                RectF::class.java,
            )
    }

    @Test
    fun gesturesManagement_moveGesture_singlePointer_tracking_duplicateCall() {
        val moveGestureDetector =
            Mockito.mock(
                MoveGestureDetector::class.java,
            )
        Mockito.`when`(moveGestureDetector.pointersCount).thenReturn(1)
        val camera = buildCamera(moveGestureDetector)
        val options =
            Mockito.mock(
                LocationComponentOptions::class.java,
            )
        Mockito.`when`(options.trackingGesturesManagement()).thenReturn(true)
        val initial = 100f
        Mockito.`when`(options.trackingInitialMoveThreshold()).thenReturn(initial)
        camera.initializeOptions(options)
        camera.cameraMode = CameraMode.TRACKING
        Mockito.`when`(moveGestureDetector.moveThreshold).thenReturn(initial)
        camera.onMoveListener.onMoveBegin(moveGestureDetector)
        Mockito.verify(moveGestureDetector, Mockito.atMost(1)).moveThreshold = initial
        Mockito.verify(moveGestureDetector, Mockito.times(0)).moveThresholdRect =
            ArgumentMatchers.any(
                RectF::class.java,
            )
    }

    @Test
    fun gesturesManagement_moveGesture_singlePointer_tracking_thresholdMet() {
        val moveGestureDetector =
            Mockito.mock(
                MoveGestureDetector::class.java,
            )
        Mockito.`when`(moveGestureDetector.pointersCount).thenReturn(1)
        val camera = buildCamera(moveGestureDetector)
        val options =
            Mockito.mock(
                LocationComponentOptions::class.java,
            )
        Mockito.`when`(options.trackingGesturesManagement()).thenReturn(true)
        val initial = 100f
        Mockito.`when`(options.trackingInitialMoveThreshold()).thenReturn(initial)
        camera.initializeOptions(options)

        // verify the number of detector interruptions
        camera.cameraMode = CameraMode.TRACKING
        camera.onMoveListener.onMoveBegin(moveGestureDetector)
        Mockito.`when`(moveGestureDetector.moveThreshold).thenReturn(initial)
        camera.onMoveListener.onMove(moveGestureDetector)
        Mockito.verify(moveGestureDetector, Mockito.times(1)).interrupt()
        camera.onMoveListener.onMoveEnd(moveGestureDetector)
        camera.onMoveListener.onMoveBegin(moveGestureDetector)
        camera.onMoveListener.onMove(moveGestureDetector)
        Mockito.verify(moveGestureDetector, Mockito.times(2)).interrupt()
        camera.onMoveListener.onMoveEnd(moveGestureDetector)
        camera.onMoveListener.onMoveBegin(moveGestureDetector)
        camera.onMoveListener.onMove(moveGestureDetector)
        camera.onMoveListener.onMoveEnd(moveGestureDetector)
        Mockito.verify(moveGestureDetector, Mockito.times(2)).interrupt()

        // verify that threshold are reset
        val moveThresholdCaptor =
            ArgumentCaptor.forClass(
                Float::class.java,
            )
        Mockito.verify(moveGestureDetector, Mockito.atLeastOnce()).moveThreshold =
            moveThresholdCaptor.capture()
        org.junit.Assert.assertEquals(java.lang.Float.valueOf(0f), moveThresholdCaptor.value)
    }

    @Test
    fun gesturesManagement_moveGesture_multiPointer_tracking() {
        val moveGestureDetector =
            Mockito.mock(
                MoveGestureDetector::class.java,
            )
        Mockito.`when`(moveGestureDetector.pointersCount).thenReturn(2)
        val camera = buildCamera(moveGestureDetector)
        val options =
            Mockito.mock(
                LocationComponentOptions::class.java,
            )
        Mockito.`when`(options.trackingGesturesManagement()).thenReturn(true)
        val initial = 100f
        val multiFinger = 200f
        val multiFingerArea = Mockito.mock(RectF::class.java)
        Mockito.`when`(options.trackingInitialMoveThreshold()).thenReturn(initial)
        Mockito.`when`(options.trackingMultiFingerMoveThreshold()).thenReturn(multiFinger)
        Mockito.`when`(options.trackingMultiFingerProtectedMoveArea()).thenReturn(multiFingerArea)
        camera.initializeOptions(options)
        camera.cameraMode = CameraMode.TRACKING
        camera.onMoveListener.onMoveBegin(moveGestureDetector)
        Mockito.verify(moveGestureDetector, Mockito.atMost(1)).moveThreshold = multiFinger
        Mockito.verify(moveGestureDetector, Mockito.atMost(1)).moveThresholdRect = multiFingerArea
    }

    @Test
    fun gesturesManagement_moveGesture_multiPointer_tracking_duplicateCall() {
        val moveGestureDetector =
            Mockito.mock(
                MoveGestureDetector::class.java,
            )
        Mockito.`when`(moveGestureDetector.pointersCount).thenReturn(2)
        val camera = buildCamera(moveGestureDetector)
        val options =
            Mockito.mock(
                LocationComponentOptions::class.java,
            )
        Mockito.`when`(options.trackingGesturesManagement()).thenReturn(true)
        val initial = 100f
        val multiFinger = 200f
        val multiFingerArea = Mockito.mock(RectF::class.java)
        Mockito.`when`(options.trackingInitialMoveThreshold()).thenReturn(initial)
        Mockito.`when`(options.trackingMultiFingerMoveThreshold()).thenReturn(multiFinger)
        Mockito.`when`(options.trackingMultiFingerProtectedMoveArea()).thenReturn(multiFingerArea)
        camera.initializeOptions(options)
        camera.cameraMode = CameraMode.TRACKING
        camera.onMoveListener.onMoveBegin(moveGestureDetector)
        Mockito.`when`(moveGestureDetector.moveThreshold).thenReturn(multiFinger)
        Mockito.`when`(moveGestureDetector.moveThresholdRect).thenReturn(multiFingerArea)
        camera.onMoveListener.onMoveBegin(moveGestureDetector)
        Mockito.verify(moveGestureDetector, Mockito.atMost(1)).moveThreshold = multiFinger
        Mockito.verify(moveGestureDetector, Mockito.atMost(1)).moveThresholdRect = multiFingerArea
    }

    @Test
    fun gesturesManagement_moveGesture_multiPointer_tracking_thresholdMet() {
        val moveGestureDetector =
            Mockito.mock(
                MoveGestureDetector::class.java,
            )
        Mockito.`when`(moveGestureDetector.pointersCount).thenReturn(2)
        val camera = buildCamera(moveGestureDetector)
        val options =
            Mockito.mock(
                LocationComponentOptions::class.java,
            )
        Mockito.`when`(options.trackingGesturesManagement()).thenReturn(true)
        val initial = 100f
        val multiFinger = 200f
        val multiFingerArea = Mockito.mock(RectF::class.java)
        Mockito.`when`(options.trackingInitialMoveThreshold()).thenReturn(initial)
        Mockito.`when`(options.trackingMultiFingerMoveThreshold()).thenReturn(multiFinger)
        Mockito.`when`(options.trackingMultiFingerProtectedMoveArea()).thenReturn(multiFingerArea)
        camera.initializeOptions(options)

        // verify the number of detector interruptions
        camera.cameraMode = CameraMode.TRACKING
        camera.onMoveListener.onMoveBegin(moveGestureDetector)
        Mockito.`when`(moveGestureDetector.moveThreshold).thenReturn(initial)
        Mockito.`when`(moveGestureDetector.moveThreshold).thenReturn(multiFinger)
        Mockito.`when`(moveGestureDetector.moveThresholdRect).thenReturn(multiFingerArea)
        camera.onMoveListener.onMove(moveGestureDetector)
        Mockito.verify(moveGestureDetector, Mockito.times(1)).interrupt()
        camera.onMoveListener.onMoveEnd(moveGestureDetector)
        camera.onMoveListener.onMoveBegin(moveGestureDetector)
        camera.onMoveListener.onMove(moveGestureDetector)
        Mockito.verify(moveGestureDetector, Mockito.times(2)).interrupt()
        camera.onMoveListener.onMoveEnd(moveGestureDetector)
        camera.onMoveListener.onMoveBegin(moveGestureDetector)
        camera.onMoveListener.onMove(moveGestureDetector)
        camera.onMoveListener.onMoveEnd(moveGestureDetector)
        Mockito.verify(moveGestureDetector, Mockito.times(2)).interrupt()

        // verify that threshold are reset
        val moveThresholdCaptor =
            ArgumentCaptor.forClass(
                Float::class.java,
            )
        Mockito.verify(moveGestureDetector, Mockito.atLeastOnce()).moveThreshold =
            moveThresholdCaptor.capture()
        org.junit.Assert.assertEquals(java.lang.Float.valueOf(0f), moveThresholdCaptor.value)
        val areaCaptor =
            ArgumentCaptor.forClass(
                RectF::class.java,
            )
        Mockito.verify(moveGestureDetector, Mockito.atLeastOnce()).moveThresholdRect =
            areaCaptor.capture()
        org.junit.Assert.assertNull(areaCaptor.value)
    }

    @Test
    fun onMove_notCancellingTransitionWhileNone() {
        val trackasiaMap = Mockito.mock(TrackAsiaMap::class.java)
        Mockito.`when`(trackasiaMap.uiSettings).thenReturn(
            Mockito.mock(
                UiSettings::class.java,
            ),
        )
        val moveGestureDetector =
            Mockito.mock(
                MoveGestureDetector::class.java,
            )
        val camera = buildCamera(trackasiaMap)
        camera.initializeOptions(
            Mockito.mock(
                LocationComponentOptions::class.java,
            ),
        )
        camera.cameraMode = CameraMode.NONE
        camera.onMoveListener.onMove(moveGestureDetector)
        Mockito.verify(trackasiaMap, Mockito.times(0)).cancelTransitions()
        Mockito.verify(moveGestureDetector, Mockito.times(0)).interrupt()

        // testing subsequent calls
        camera.onMoveListener.onMove(moveGestureDetector)
        Mockito.verify(trackasiaMap, Mockito.times(0)).cancelTransitions()
        Mockito.verify(moveGestureDetector, Mockito.times(0)).interrupt()
    }

    @Test
    fun onMove_cancellingTransitionWhileGps() {
        val trackasiaMap = Mockito.mock(TrackAsiaMap::class.java)
        Mockito.`when`(trackasiaMap.uiSettings).thenReturn(
            Mockito.mock(
                UiSettings::class.java,
            ),
        )
        val moveGestureDetector =
            Mockito.mock(
                MoveGestureDetector::class.java,
            )
        val camera = buildCamera(trackasiaMap)
        camera.initializeOptions(
            Mockito.mock(
                LocationComponentOptions::class.java,
            ),
        )
        camera.cameraMode = CameraMode.TRACKING
        camera.onMoveListener.onMove(moveGestureDetector)
        Mockito.verify(trackasiaMap, Mockito.times(1)).cancelTransitions()
        Mockito.verify(moveGestureDetector, Mockito.times(1)).interrupt()

        // testing subsequent calls
        camera.onMoveListener.onMove(moveGestureDetector)
        Mockito.verify(trackasiaMap, Mockito.times(1)).cancelTransitions()
        Mockito.verify(moveGestureDetector, Mockito.times(1)).interrupt()
    }

    @Test
    fun onMove_cancellingTransitionWhileBearing() {
        val trackasiaMap = Mockito.mock(TrackAsiaMap::class.java)
        val moveGestureDetector =
            Mockito.mock(
                MoveGestureDetector::class.java,
            )
        val camera = buildCamera(trackasiaMap)
        camera.initializeOptions(
            Mockito.mock(
                LocationComponentOptions::class.java,
            ),
        )
        camera.cameraMode = CameraMode.NONE_COMPASS
        camera.onMoveListener.onMove(moveGestureDetector)
        Mockito.verify(trackasiaMap, Mockito.times(1)).cancelTransitions()
        Mockito.verify(moveGestureDetector, Mockito.times(1)).interrupt()

        // testing subsequent calls
        camera.onMoveListener.onMove(moveGestureDetector)
        Mockito.verify(trackasiaMap, Mockito.times(1)).cancelTransitions()
        Mockito.verify(moveGestureDetector, Mockito.times(1)).interrupt()
    }

    @Test
    fun transition_locationIsNull() {
        val trackasiaMap = Mockito.mock(TrackAsiaMap::class.java)
        val transform =
            Mockito.mock(
                Transform::class.java,
            )
        val camera = buildCamera(trackasiaMap, transform)
        camera.initializeOptions(
            Mockito.mock(
                LocationComponentOptions::class.java,
            ),
        )
        val listener =
            Mockito.mock(
                OnLocationCameraTransitionListener::class.java,
            )
        camera.setCameraMode(
            CameraMode.TRACKING,
            null,
            LocationComponentConstants.TRANSITION_ANIMATION_DURATION_MS,
            null,
            null,
            null,
            listener,
        )
        Assert.assertEquals(CameraMode.TRACKING, camera.cameraMode)
        Mockito.verify(listener).onLocationCameraTransitionFinished(CameraMode.TRACKING)
        Mockito
            .verify(transform, Mockito.times(0))
            .animateCamera(
                ArgumentMatchers.eq(trackasiaMap),
                ArgumentMatchers.any(
                    CameraUpdate::class.java,
                ),
                ArgumentMatchers.any(Int::class.java),
                ArgumentMatchers.any(CancelableCallback::class.java),
            )
    }

    @Test
    fun transition_notTracking() {
        val trackasiaMap = Mockito.mock(TrackAsiaMap::class.java)
        val transform =
            Mockito.mock(
                Transform::class.java,
            )
        val camera = buildCamera(trackasiaMap, transform)
        camera.initializeOptions(
            Mockito.mock(
                LocationComponentOptions::class.java,
            ),
        )
        val listener =
            Mockito.mock(
                OnLocationCameraTransitionListener::class.java,
            )
        val location =
            Mockito.mock(
                Location::class.java,
            )
        camera.setCameraMode(
            CameraMode.NONE,
            location,
            LocationComponentConstants.TRANSITION_ANIMATION_DURATION_MS,
            null,
            null,
            null,
            listener,
        )
        Mockito
            .verify(listener, Mockito.times(1))
            .onLocationCameraTransitionFinished(CameraMode.NONE)
        Mockito
            .verify(transform, Mockito.times(0))
            .animateCamera(
                ArgumentMatchers.eq(trackasiaMap),
                ArgumentMatchers.any(
                    CameraUpdate::class.java,
                ),
                ArgumentMatchers.any(Int::class.java),
                ArgumentMatchers.any(CancelableCallback::class.java),
            )
    }

    @Test
    fun transition_trackingChanged() {
        val trackasiaMap = Mockito.mock(TrackAsiaMap::class.java)
        val transform =
            Mockito.mock(
                Transform::class.java,
            )
        Mockito.`when`(trackasiaMap.cameraPosition).thenReturn(CameraPosition.DEFAULT)
        val projection =
            Mockito.mock(
                Projection::class.java,
            )
        Mockito.`when`(trackasiaMap.projection).thenReturn(projection)
        Mockito
            .`when`(
                projection.getMetersPerPixelAtLatitude(
                    ArgumentMatchers.any(
                        Double::class.java,
                    ),
                ),
            ).thenReturn(java.lang.Double.valueOf(1000.0))
        val camera = buildCamera(trackasiaMap, transform)
        camera.initializeOptions(
            Mockito.mock(
                LocationComponentOptions::class.java,
            ),
        )
        val listener =
            Mockito.mock(
                OnLocationCameraTransitionListener::class.java,
            )
        val location =
            Mockito.mock(
                Location::class.java,
            )
        Mockito
            .doAnswer {
                listener.onLocationCameraTransitionFinished(CameraMode.TRACKING)
                null
            }.`when`(transform)
            .animateCamera(
                ArgumentMatchers.eq(trackasiaMap),
                ArgumentMatchers.any(
                    CameraUpdate::class.java,
                ),
                ArgumentMatchers.any(Int::class.java),
                ArgumentMatchers.any(CancelableCallback::class.java),
            )
        camera.setCameraMode(
            CameraMode.TRACKING,
            location,
            LocationComponentConstants.TRANSITION_ANIMATION_DURATION_MS,
            null,
            null,
            null,
            listener,
        )
        Mockito.verify(listener).onLocationCameraTransitionFinished(CameraMode.TRACKING)
        Mockito
            .verify(transform)
            .animateCamera(
                ArgumentMatchers.eq(trackasiaMap),
                ArgumentMatchers.any(
                    CameraUpdate::class.java,
                ),
                ArgumentMatchers.any(Int::class.java),
                ArgumentMatchers.any(CancelableCallback::class.java),
            )
    }

    @Test
    fun transition_trackingNotChanged() {
        val trackasiaMap = Mockito.mock(TrackAsiaMap::class.java)
        val transform =
            Mockito.mock(
                Transform::class.java,
            )
        Mockito.`when`(trackasiaMap.cameraPosition).thenReturn(CameraPosition.DEFAULT)
        val projection =
            Mockito.mock(
                Projection::class.java,
            )
        Mockito.`when`(trackasiaMap.projection).thenReturn(projection)
        Mockito
            .`when`(
                projection.getMetersPerPixelAtLatitude(
                    ArgumentMatchers.any(
                        Double::class.java,
                    ),
                ),
            ).thenReturn(java.lang.Double.valueOf(1000.0))
        val camera = buildCamera(trackasiaMap, transform)
        camera.initializeOptions(
            Mockito.mock(
                LocationComponentOptions::class.java,
            ),
        )
        val listener =
            Mockito.mock(
                OnLocationCameraTransitionListener::class.java,
            )
        val location =
            Mockito.mock(
                Location::class.java,
            )
        camera.setCameraMode(
            CameraMode.TRACKING,
            location,
            LocationComponentConstants.TRANSITION_ANIMATION_DURATION_MS,
            null,
            null,
            null,
            listener,
        )
        Mockito
            .doAnswer {
                listener.onLocationCameraTransitionFinished(CameraMode.TRACKING_GPS_NORTH)
                null
            }.`when`(transform)
            .animateCamera(
                ArgumentMatchers.eq(trackasiaMap),
                ArgumentMatchers.any(
                    CameraUpdate::class.java,
                ),
                ArgumentMatchers.any(Int::class.java),
                ArgumentMatchers.any(CancelableCallback::class.java),
            )
        camera.setCameraMode(
            CameraMode.TRACKING_GPS_NORTH,
            location,
            LocationComponentConstants.TRANSITION_ANIMATION_DURATION_MS,
            null,
            null,
            null,
            listener,
        )
        Mockito
            .verify(listener, Mockito.times(1))
            .onLocationCameraTransitionFinished(CameraMode.TRACKING_GPS_NORTH)
        Mockito
            .verify(transform, Mockito.times(1))
            .animateCamera(
                ArgumentMatchers.eq(trackasiaMap),
                ArgumentMatchers.any(
                    CameraUpdate::class.java,
                ),
                ArgumentMatchers.any(Int::class.java),
                ArgumentMatchers.any(CancelableCallback::class.java),
            )
    }

    @Test
    fun transition_duplicateMode() {
        val trackasiaMap = Mockito.mock(TrackAsiaMap::class.java)
        val transform =
            Mockito.mock(
                Transform::class.java,
            )
        Mockito.`when`(trackasiaMap.cameraPosition).thenReturn(CameraPosition.DEFAULT)
        val projection =
            Mockito.mock(
                Projection::class.java,
            )
        Mockito.`when`(trackasiaMap.projection).thenReturn(projection)
        Mockito
            .`when`(
                projection.getMetersPerPixelAtLatitude(
                    ArgumentMatchers.any(
                        Double::class.java,
                    ),
                ),
            ).thenReturn(java.lang.Double.valueOf(1000.0))
        val camera = buildCamera(trackasiaMap, transform)
        camera.initializeOptions(
            Mockito.mock(
                LocationComponentOptions::class.java,
            ),
        )
        val listener =
            Mockito.mock(
                OnLocationCameraTransitionListener::class.java,
            )
        val location =
            Mockito.mock(
                Location::class.java,
            )
        camera.setCameraMode(
            CameraMode.TRACKING,
            location,
            LocationComponentConstants.TRANSITION_ANIMATION_DURATION_MS,
            null,
            null,
            null,
            listener,
        )
        Mockito
            .doAnswer {
                listener.onLocationCameraTransitionFinished(CameraMode.TRACKING)
                null
            }.`when`(transform)
            .animateCamera(
                ArgumentMatchers.eq(trackasiaMap),
                ArgumentMatchers.any(
                    CameraUpdate::class.java,
                ),
                ArgumentMatchers.any(Int::class.java),
                ArgumentMatchers.any(CancelableCallback::class.java),
            )
        camera.setCameraMode(
            CameraMode.TRACKING,
            location,
            LocationComponentConstants.TRANSITION_ANIMATION_DURATION_MS,
            null,
            null,
            null,
            listener,
        )
        Mockito
            .verify(listener, Mockito.times(1))
            .onLocationCameraTransitionFinished(CameraMode.TRACKING)
        Mockito
            .verify(transform, Mockito.times(1))
            .animateCamera(
                ArgumentMatchers.eq(trackasiaMap),
                ArgumentMatchers.any(
                    CameraUpdate::class.java,
                ),
                ArgumentMatchers.any(Int::class.java),
                ArgumentMatchers.any(CancelableCallback::class.java),
            )
    }

    @Test
    fun transition_canceled() {
        val trackasiaMap = Mockito.mock(TrackAsiaMap::class.java)
        val transform =
            Mockito.mock(
                Transform::class.java,
            )
        Mockito.`when`(trackasiaMap.cameraPosition).thenReturn(CameraPosition.DEFAULT)
        val projection =
            Mockito.mock(
                Projection::class.java,
            )
        Mockito.`when`(trackasiaMap.projection).thenReturn(projection)
        Mockito
            .`when`(
                projection.getMetersPerPixelAtLatitude(
                    ArgumentMatchers.any(
                        Double::class.java,
                    ),
                ),
            ).thenReturn(java.lang.Double.valueOf(1000.0))
        val camera = buildCamera(trackasiaMap, transform)
        camera.initializeOptions(
            Mockito.mock(
                LocationComponentOptions::class.java,
            ),
        )
        val listener =
            Mockito.mock(
                OnLocationCameraTransitionListener::class.java,
            )
        val location =
            Mockito.mock(
                Location::class.java,
            )
        Mockito
            .doAnswer {
                listener.onLocationCameraTransitionCanceled(CameraMode.TRACKING)
                null
            }.`when`(transform)
            .animateCamera(
                ArgumentMatchers.eq(trackasiaMap),
                ArgumentMatchers.any(
                    CameraUpdate::class.java,
                ),
                ArgumentMatchers.any(Int::class.java),
                ArgumentMatchers.any(CancelableCallback::class.java),
            )
        camera.setCameraMode(
            CameraMode.TRACKING,
            location,
            LocationComponentConstants.TRANSITION_ANIMATION_DURATION_MS,
            null,
            null,
            null,
            listener,
        )
        Mockito.verify(listener).onLocationCameraTransitionCanceled(CameraMode.TRACKING)
        Mockito
            .verify(transform)
            .animateCamera(
                ArgumentMatchers.eq(trackasiaMap),
                ArgumentMatchers.any(
                    CameraUpdate::class.java,
                ),
                ArgumentMatchers.any(Int::class.java),
                ArgumentMatchers.any(CancelableCallback::class.java),
            )
    }

    @Test
    fun transition_mapboxCallbackFinished() {
        val trackasiaMap = Mockito.mock(TrackAsiaMap::class.java)
        val transform =
            Mockito.mock(
                Transform::class.java,
            )
        Mockito.`when`(trackasiaMap.cameraPosition).thenReturn(CameraPosition.DEFAULT)
        val projection =
            Mockito.mock(
                Projection::class.java,
            )
        Mockito.`when`(trackasiaMap.projection).thenReturn(projection)
        Mockito
            .`when`(
                projection.getMetersPerPixelAtLatitude(
                    ArgumentMatchers.any(
                        Double::class.java,
                    ),
                ),
            ).thenReturn(java.lang.Double.valueOf(1000.0))
        val camera = buildCamera(trackasiaMap, transform)
        camera.initializeOptions(
            Mockito.mock(
                LocationComponentOptions::class.java,
            ),
        )
        val listener =
            Mockito.mock(
                OnLocationCameraTransitionListener::class.java,
            )
        val location =
            Mockito.mock(
                Location::class.java,
            )
        Mockito.`when`(location.latitude).thenReturn(1.0)
        Mockito.`when`(location.longitude).thenReturn(1.0)
        Mockito.`when`(location.bearing).thenReturn(30f)
        Mockito.`when`(location.altitude).thenReturn(0.0)
        val callbackCaptor =
            ArgumentCaptor.forClass(
                CancelableCallback::class.java,
            )
        camera.setCameraMode(
            CameraMode.TRACKING,
            location,
            LocationComponentConstants.TRANSITION_ANIMATION_DURATION_MS,
            null,
            null,
            null,
            listener,
        )
        val builder = CameraPosition.Builder().target(LatLng(location))
        Mockito.verify(transform).animateCamera(
            ArgumentMatchers.eq(trackasiaMap),
            ArgumentMatchers.eq(newCameraPosition(builder.build())),
            ArgumentMatchers.eq(LocationComponentConstants.TRANSITION_ANIMATION_DURATION_MS.toInt()),
            callbackCaptor.capture(),
        )
        Assert.assertTrue(camera.isTransitioning)
        callbackCaptor.value.onFinish()
        Assert.assertFalse(camera.isTransitioning)
        Mockito.verify(listener).onLocationCameraTransitionFinished(CameraMode.TRACKING)
    }

    @Test
    fun transition_mapboxCallbackFinishedImmediately() {
        val trackasiaMap = Mockito.mock(TrackAsiaMap::class.java)
        val transform =
            Mockito.mock(
                Transform::class.java,
            )
        Mockito.`when`(trackasiaMap.cameraPosition).thenReturn(CameraPosition.DEFAULT)
        val projection =
            Mockito.mock(
                Projection::class.java,
            )
        Mockito.`when`(trackasiaMap.projection).thenReturn(projection)
        Mockito
            .`when`(
                projection.getMetersPerPixelAtLatitude(
                    ArgumentMatchers.any(
                        Double::class.java,
                    ),
                ),
            ).thenReturn(java.lang.Double.valueOf(1.0))
        val camera = buildCamera(trackasiaMap, transform)
        camera.initializeOptions(
            Mockito.mock(
                LocationComponentOptions::class.java,
            ),
        )
        val listener =
            Mockito.mock(
                OnLocationCameraTransitionListener::class.java,
            )
        val location =
            Mockito.mock(
                Location::class.java,
            )
        Mockito.`when`(location.latitude).thenReturn(1.0)
        Mockito.`when`(location.longitude).thenReturn(1.0)
        Mockito.`when`(location.bearing).thenReturn(30f)
        Mockito.`when`(location.altitude).thenReturn(0.0)
        val callbackCaptor =
            ArgumentCaptor.forClass(
                CancelableCallback::class.java,
            )
        camera.setCameraMode(
            CameraMode.TRACKING,
            location,
            LocationComponentConstants.TRANSITION_ANIMATION_DURATION_MS,
            null,
            null,
            null,
            listener,
        )
        val builder = CameraPosition.Builder().target(LatLng(location))
        Mockito.verify(transform).moveCamera(
            ArgumentMatchers.eq(trackasiaMap),
            ArgumentMatchers.eq(newCameraPosition(builder.build())),
            callbackCaptor.capture(),
        )
        Assert.assertTrue(camera.isTransitioning)
        callbackCaptor.value.onFinish()
        Assert.assertFalse(camera.isTransitioning)
        Mockito.verify(listener).onLocationCameraTransitionFinished(CameraMode.TRACKING)
    }

    @Test
    fun transition_mapboxCallbackCanceled() {
        val trackasiaMap = Mockito.mock(TrackAsiaMap::class.java)
        val transform =
            Mockito.mock(
                Transform::class.java,
            )
        Mockito.`when`(trackasiaMap.cameraPosition).thenReturn(CameraPosition.DEFAULT)
        val projection =
            Mockito.mock(
                Projection::class.java,
            )
        Mockito.`when`(trackasiaMap.projection).thenReturn(projection)
        Mockito
            .`when`(
                projection.getMetersPerPixelAtLatitude(
                    ArgumentMatchers.any(
                        Double::class.java,
                    ),
                ),
            ).thenReturn(java.lang.Double.valueOf(1000.0))
        val camera = buildCamera(trackasiaMap, transform)
        camera.initializeOptions(
            Mockito.mock(
                LocationComponentOptions::class.java,
            ),
        )
        val listener =
            Mockito.mock(
                OnLocationCameraTransitionListener::class.java,
            )
        val location =
            Mockito.mock(
                Location::class.java,
            )
        Mockito.`when`(location.latitude).thenReturn(1.0)
        Mockito.`when`(location.longitude).thenReturn(1.0)
        Mockito.`when`(location.bearing).thenReturn(30f)
        Mockito.`when`(location.altitude).thenReturn(0.0)
        val callbackCaptor =
            ArgumentCaptor.forClass(
                CancelableCallback::class.java,
            )
        camera.setCameraMode(
            CameraMode.TRACKING,
            location,
            LocationComponentConstants.TRANSITION_ANIMATION_DURATION_MS,
            null,
            null,
            null,
            listener,
        )
        val builder = CameraPosition.Builder().target(LatLng(location))
        Mockito.verify(transform).animateCamera(
            ArgumentMatchers.eq(trackasiaMap),
            ArgumentMatchers.eq(newCameraPosition(builder.build())),
            ArgumentMatchers.eq(LocationComponentConstants.TRANSITION_ANIMATION_DURATION_MS.toInt()),
            callbackCaptor.capture(),
        )
        Assert.assertTrue(camera.isTransitioning)
        callbackCaptor.value.onCancel()
        Assert.assertFalse(camera.isTransitioning)
        Mockito.verify(listener).onLocationCameraTransitionCanceled(CameraMode.TRACKING)
    }

    @Test
    fun transition_mapboxAnimateBearing() {
        val trackasiaMap = Mockito.mock(TrackAsiaMap::class.java)
        val transform =
            Mockito.mock(
                Transform::class.java,
            )
        Mockito.`when`(trackasiaMap.cameraPosition).thenReturn(CameraPosition.DEFAULT)
        val projection =
            Mockito.mock(
                Projection::class.java,
            )
        Mockito.`when`(trackasiaMap.projection).thenReturn(projection)
        Mockito
            .`when`(
                projection.getMetersPerPixelAtLatitude(
                    ArgumentMatchers.any(
                        Double::class.java,
                    ),
                ),
            ).thenReturn(java.lang.Double.valueOf(1000.0))
        val camera = buildCamera(trackasiaMap, transform)
        camera.initializeOptions(
            Mockito.mock(
                LocationComponentOptions::class.java,
            ),
        )
        val listener =
            Mockito.mock(
                OnLocationCameraTransitionListener::class.java,
            )
        val location =
            Mockito.mock(
                Location::class.java,
            )
        Mockito.`when`(location.latitude).thenReturn(1.0)
        Mockito.`when`(location.longitude).thenReturn(1.0)
        Mockito.`when`(location.bearing).thenReturn(30f)
        Mockito.`when`(location.altitude).thenReturn(0.0)
        camera.setCameraMode(
            CameraMode.TRACKING_GPS,
            location,
            LocationComponentConstants.TRANSITION_ANIMATION_DURATION_MS,
            null,
            null,
            null,
            listener,
        )
        val builder = CameraPosition.Builder().target(LatLng(location)).bearing(30.0)
        Mockito.verify(transform).animateCamera(
            ArgumentMatchers.eq(trackasiaMap),
            ArgumentMatchers.eq(newCameraPosition(builder.build())),
            ArgumentMatchers.eq(LocationComponentConstants.TRANSITION_ANIMATION_DURATION_MS.toInt()),
            ArgumentMatchers.any(CancelableCallback::class.java),
        )
    }

    @Test
    fun transition_mapboxAnimateNorth() {
        val trackasiaMap = Mockito.mock(TrackAsiaMap::class.java)
        val transform =
            Mockito.mock(
                Transform::class.java,
            )
        Mockito.`when`(trackasiaMap.cameraPosition).thenReturn(CameraPosition.DEFAULT)
        val projection =
            Mockito.mock(
                Projection::class.java,
            )
        Mockito.`when`(trackasiaMap.projection).thenReturn(projection)
        Mockito
            .`when`(
                projection.getMetersPerPixelAtLatitude(
                    ArgumentMatchers.any(
                        Double::class.java,
                    ),
                ),
            ).thenReturn(java.lang.Double.valueOf(1000.0))
        val camera = buildCamera(trackasiaMap, transform)
        camera.initializeOptions(
            Mockito.mock(
                LocationComponentOptions::class.java,
            ),
        )
        val listener =
            Mockito.mock(
                OnLocationCameraTransitionListener::class.java,
            )
        val location =
            Mockito.mock(
                Location::class.java,
            )
        Mockito.`when`(location.latitude).thenReturn(1.0)
        Mockito.`when`(location.longitude).thenReturn(1.0)
        Mockito.`when`(location.bearing).thenReturn(30f)
        Mockito.`when`(location.altitude).thenReturn(0.0)
        camera.setCameraMode(
            CameraMode.TRACKING_GPS_NORTH,
            location,
            LocationComponentConstants.TRANSITION_ANIMATION_DURATION_MS,
            null,
            null,
            null,
            listener,
        )
        val builder = CameraPosition.Builder().target(LatLng(location)).bearing(0.0)
        Mockito.verify(transform).animateCamera(
            ArgumentMatchers.eq(trackasiaMap),
            ArgumentMatchers.eq(newCameraPosition(builder.build())),
            ArgumentMatchers.eq(LocationComponentConstants.TRANSITION_ANIMATION_DURATION_MS.toInt()),
            ArgumentMatchers.any(CancelableCallback::class.java),
        )
    }

    @Test
    fun transition_animatorValuesDuringTransition() {
        val trackasiaMap = Mockito.mock(TrackAsiaMap::class.java)
        val transform =
            Mockito.mock(
                Transform::class.java,
            )
        Mockito.`when`(trackasiaMap.cameraPosition).thenReturn(CameraPosition.DEFAULT)
        val projection =
            Mockito.mock(
                Projection::class.java,
            )
        Mockito.`when`(trackasiaMap.projection).thenReturn(projection)
        Mockito
            .`when`(
                projection.getMetersPerPixelAtLatitude(
                    ArgumentMatchers.any(
                        Double::class.java,
                    ),
                ),
            ).thenReturn(java.lang.Double.valueOf(1000.0))
        val camera = buildCamera(trackasiaMap, transform)
        camera.initializeOptions(
            Mockito.mock(
                LocationComponentOptions::class.java,
            ),
        )
        val listener =
            Mockito.mock(
                OnLocationCameraTransitionListener::class.java,
            )
        val location =
            Mockito.mock(
                Location::class.java,
            )
        val callbackCaptor =
            ArgumentCaptor.forClass(
                CancelableCallback::class.java,
            )
        camera.setCameraMode(
            CameraMode.TRACKING_GPS,
            location,
            LocationComponentConstants.TRANSITION_ANIMATION_DURATION_MS,
            null,
            null,
            null,
            listener,
        )
        Mockito.verify(transform).animateCamera(
            ArgumentMatchers.eq(trackasiaMap),
            ArgumentMatchers.any(CameraUpdate::class.java),
            ArgumentMatchers.eq(LocationComponentConstants.TRANSITION_ANIMATION_DURATION_MS.toInt()),
            callbackCaptor.capture(),
        )
        val latLng = LatLng(10.0, 10.0)
        getAnimationListener<Any>(
            TrackAsiaAnimator.ANIMATOR_CAMERA_LATLNG,
            camera.animationListeners,
        )!!.onNewAnimationValue(latLng)
        getAnimationListener<Any>(
            TrackAsiaAnimator.ANIMATOR_CAMERA_GPS_BEARING,
            camera.animationListeners,
        )!!.onNewAnimationValue(10f)
        getAnimationListener<Any>(
            TrackAsiaAnimator.ANIMATOR_TILT,
            camera.animationListeners,
        )!!.onNewAnimationValue(10f)
        getAnimationListener<Any>(
            TrackAsiaAnimator.ANIMATOR_ZOOM,
            camera.animationListeners,
        )!!.onNewAnimationValue(10f)
        Mockito.verify(transform, Mockito.times(0)).moveCamera(
            ArgumentMatchers.eq(trackasiaMap),
            ArgumentMatchers.any(
                CameraUpdate::class.java,
            ),
            ArgumentMatchers.nullable(CancelableCallback::class.java),
        )
        callbackCaptor.value.onFinish()
        getAnimationListener<Any>(
            TrackAsiaAnimator.ANIMATOR_CAMERA_LATLNG,
            camera.animationListeners,
        )!!.onNewAnimationValue(latLng)
        getAnimationListener<Any>(
            TrackAsiaAnimator.ANIMATOR_CAMERA_GPS_BEARING,
            camera.animationListeners,
        )!!.onNewAnimationValue(10f)
        getAnimationListener<Any>(
            TrackAsiaAnimator.ANIMATOR_TILT,
            camera.animationListeners,
        )!!.onNewAnimationValue(10f)
        getAnimationListener<Any>(
            TrackAsiaAnimator.ANIMATOR_ZOOM,
            camera.animationListeners,
        )!!.onNewAnimationValue(10f)
        Mockito.verify(transform, Mockito.times(4)).moveCamera(
            ArgumentMatchers.eq(trackasiaMap),
            ArgumentMatchers.any(
                CameraUpdate::class.java,
            ),
            ArgumentMatchers.nullable(CancelableCallback::class.java),
        )
    }

    @Test
    fun transition_customAnimation() {
        val trackasiaMap = Mockito.mock(TrackAsiaMap::class.java)
        val transform =
            Mockito.mock(
                Transform::class.java,
            )
        Mockito.`when`(trackasiaMap.cameraPosition).thenReturn(CameraPosition.DEFAULT)
        val projection =
            Mockito.mock(
                Projection::class.java,
            )
        Mockito.`when`(trackasiaMap.projection).thenReturn(projection)
        Mockito
            .`when`(
                projection.getMetersPerPixelAtLatitude(
                    ArgumentMatchers.any(
                        Double::class.java,
                    ),
                ),
            ).thenReturn(java.lang.Double.valueOf(1000.0))
        val camera = buildCamera(trackasiaMap, transform)
        camera.initializeOptions(
            Mockito.mock(
                LocationComponentOptions::class.java,
            ),
        )
        val location =
            Mockito.mock(
                Location::class.java,
            )
        val cameraUpdate =
            newCameraPosition(
                CameraPosition
                    .Builder()
                    .target(LatLng(location))
                    .zoom(14.0)
                    .bearing(13.0)
                    .tilt(45.0)
                    .build(),
            )
        camera.setCameraMode(CameraMode.TRACKING, location, 1200, 14.0, 13.0, 45.0, null)
        Mockito
            .verify(transform)
            .animateCamera(
                ArgumentMatchers.eq(trackasiaMap),
                ArgumentMatchers.eq(cameraUpdate),
                ArgumentMatchers.eq(1200),
                ArgumentMatchers.any(
                    CancelableCallback::class.java,
                ),
            )
    }

    @Test
    fun transition_customAnimationDisabled() {
        val trackasiaMap = Mockito.mock(TrackAsiaMap::class.java)
        val transform =
            Mockito.mock(
                Transform::class.java,
            )
        Mockito.`when`(trackasiaMap.cameraPosition).thenReturn(CameraPosition.DEFAULT)
        val projection =
            Mockito.mock(
                Projection::class.java,
            )
        Mockito.`when`(trackasiaMap.projection).thenReturn(projection)
        Mockito
            .`when`(
                projection.getMetersPerPixelAtLatitude(
                    ArgumentMatchers.any(
                        Double::class.java,
                    ),
                ),
            ).thenReturn(java.lang.Double.valueOf(1000.0))
        val camera = buildCamera(trackasiaMap, transform)
        camera.initializeOptions(
            Mockito.mock(
                LocationComponentOptions::class.java,
            ),
        )
        val location =
            Mockito.mock(
                Location::class.java,
            )
        val cameraUpdate =
            newCameraPosition(
                CameraPosition
                    .Builder()
                    .target(LatLng(location))
                    .zoom(14.0)
                    .bearing(13.0)
                    .tilt(45.0)
                    .build(),
            )
        camera.setEnabled(false)
        camera.setCameraMode(CameraMode.TRACKING, location, 1200, 14.0, 13.0, 45.0, null)
        Mockito
            .verify(transform, Mockito.times(0))
            .animateCamera(
                ArgumentMatchers.eq(trackasiaMap),
                ArgumentMatchers.eq(cameraUpdate),
                ArgumentMatchers.eq(1200),
                ArgumentMatchers.any(
                    CancelableCallback::class.java,
                ),
            )
    }

    private fun buildCamera(onCameraTrackingChangedListener: OnCameraTrackingChangedListener): LocationCameraController {
        val trackasiaMap = Mockito.mock(TrackAsiaMap::class.java)
        Mockito.`when`(trackasiaMap.uiSettings).thenReturn(
            Mockito.mock(
                UiSettings::class.java,
            ),
        )
        val transform =
            Mockito.mock(
                Transform::class.java,
            )
        val projection =
            Mockito.mock(
                Projection::class.java,
            )
        Mockito.`when`(trackasiaMap.projection).thenReturn(projection)
        Mockito
            .`when`(
                projection.getMetersPerPixelAtLatitude(
                    ArgumentMatchers.any(
                        Double::class.java,
                    ),
                ),
            ).thenReturn(java.lang.Double.valueOf(1000.0))
        val moveGestureDetector =
            Mockito.mock(
                MoveGestureDetector::class.java,
            )
        val onCameraMoveInvalidateListener =
            Mockito.mock(
                OnCameraMoveInvalidateListener::class.java,
            )
        val initialGesturesManager =
            Mockito.mock(
                AndroidGesturesManager::class.java,
            )
        val internalGesturesManager =
            Mockito.mock(
                AndroidGesturesManager::class.java,
            )
        return LocationCameraController(
            trackasiaMap,
            transform,
            moveGestureDetector,
            onCameraTrackingChangedListener,
            onCameraMoveInvalidateListener,
            initialGesturesManager,
            internalGesturesManager,
        )
    }

    private fun buildCamera(moveGestureDetector: MoveGestureDetector): LocationCameraController {
        val trackasiaMap = Mockito.mock(TrackAsiaMap::class.java)
        Mockito.`when`(trackasiaMap.uiSettings).thenReturn(
            Mockito.mock(
                UiSettings::class.java,
            ),
        )
        val transform =
            Mockito.mock(
                Transform::class.java,
            )
        val projection =
            Mockito.mock(
                Projection::class.java,
            )
        Mockito.`when`(trackasiaMap.projection).thenReturn(projection)
        Mockito
            .`when`(
                projection.getMetersPerPixelAtLatitude(
                    ArgumentMatchers.any(
                        Double::class.java,
                    ),
                ),
            ).thenReturn(java.lang.Double.valueOf(1000.0))
        val onCameraTrackingChangedListener =
            Mockito.mock(
                OnCameraTrackingChangedListener::class.java,
            )
        val onCameraMoveInvalidateListener =
            Mockito.mock(
                OnCameraMoveInvalidateListener::class.java,
            )
        val initialGesturesManager =
            Mockito.mock(
                AndroidGesturesManager::class.java,
            )
        val internalGesturesManager =
            Mockito.mock(
                AndroidGesturesManager::class.java,
            )
        return LocationCameraController(
            trackasiaMap,
            transform,
            moveGestureDetector,
            onCameraTrackingChangedListener,
            onCameraMoveInvalidateListener,
            initialGesturesManager,
            internalGesturesManager,
        )
    }

    private fun buildCamera(trackasiaMap: TrackAsiaMap): LocationCameraController {
        val transform =
            Mockito.mock(
                Transform::class.java,
            )
        val moveGestureDetector =
            Mockito.mock(
                MoveGestureDetector::class.java,
            )
        val onCameraTrackingChangedListener =
            Mockito.mock(
                OnCameraTrackingChangedListener::class.java,
            )
        val onCameraMoveInvalidateListener =
            Mockito.mock(
                OnCameraMoveInvalidateListener::class.java,
            )
        val initialGesturesManager =
            Mockito.mock(
                AndroidGesturesManager::class.java,
            )
        val internalGesturesManager =
            Mockito.mock(
                AndroidGesturesManager::class.java,
            )
        return LocationCameraController(
            trackasiaMap,
            transform,
            moveGestureDetector,
            onCameraTrackingChangedListener,
            onCameraMoveInvalidateListener,
            initialGesturesManager,
            internalGesturesManager,
        )
    }

    private fun buildCamera(transform: Transform): LocationCameraController {
        val trackasiaMap = Mockito.mock(TrackAsiaMap::class.java)
        Mockito.`when`(trackasiaMap.uiSettings).thenReturn(
            Mockito.mock(
                UiSettings::class.java,
            ),
        )
        val projection =
            Mockito.mock(
                Projection::class.java,
            )
        Mockito.`when`(trackasiaMap.projection).thenReturn(projection)
        Mockito
            .`when`(
                projection.getMetersPerPixelAtLatitude(
                    ArgumentMatchers.any(
                        Double::class.java,
                    ),
                ),
            ).thenReturn(java.lang.Double.valueOf(1000.0))
        Mockito.`when`(trackasiaMap.uiSettings).thenReturn(
            Mockito.mock(
                UiSettings::class.java,
            ),
        )
        val moveGestureDetector =
            Mockito.mock(
                MoveGestureDetector::class.java,
            )
        val onCameraTrackingChangedListener =
            Mockito.mock(
                OnCameraTrackingChangedListener::class.java,
            )
        val onCameraMoveInvalidateListener =
            Mockito.mock(
                OnCameraMoveInvalidateListener::class.java,
            )
        val initialGesturesManager =
            Mockito.mock(
                AndroidGesturesManager::class.java,
            )
        val internalGesturesManager =
            Mockito.mock(
                AndroidGesturesManager::class.java,
            )
        return LocationCameraController(
            trackasiaMap,
            transform,
            moveGestureDetector,
            onCameraTrackingChangedListener,
            onCameraMoveInvalidateListener,
            initialGesturesManager,
            internalGesturesManager,
        )
    }

    private fun buildCamera(
        trackasiaMap: TrackAsiaMap,
        transform: Transform,
    ): LocationCameraController {
        Mockito.`when`(trackasiaMap.uiSettings).thenReturn(
            Mockito.mock(
                UiSettings::class.java,
            ),
        )
        val moveGestureDetector =
            Mockito.mock(
                MoveGestureDetector::class.java,
            )
        val onCameraTrackingChangedListener =
            Mockito.mock(
                OnCameraTrackingChangedListener::class.java,
            )
        val onCameraMoveInvalidateListener =
            Mockito.mock(
                OnCameraMoveInvalidateListener::class.java,
            )
        val initialGesturesManager =
            Mockito.mock(
                AndroidGesturesManager::class.java,
            )
        val internalGesturesManager =
            Mockito.mock(
                AndroidGesturesManager::class.java,
            )
        val locationCameraController =
            LocationCameraController(
                trackasiaMap,
                transform,
                moveGestureDetector,
                onCameraTrackingChangedListener,
                onCameraMoveInvalidateListener,
                initialGesturesManager,
                internalGesturesManager,
            )
        locationCameraController.setEnabled(true)
        return locationCameraController
    }

    private fun buildCamera(
        trackasiaMap: TrackAsiaMap,
        initialGesturesManager: AndroidGesturesManager,
        internalGesturesManager: AndroidGesturesManager,
    ): LocationCameraController {
        val transform =
            Mockito.mock(
                Transform::class.java,
            )
        val moveGestureDetector =
            Mockito.mock(
                MoveGestureDetector::class.java,
            )
        val onCameraTrackingChangedListener =
            Mockito.mock(
                OnCameraTrackingChangedListener::class.java,
            )
        val onCameraMoveInvalidateListener =
            Mockito.mock(
                OnCameraMoveInvalidateListener::class.java,
            )
        return LocationCameraController(
            trackasiaMap,
            transform,
            moveGestureDetector,
            onCameraTrackingChangedListener,
            onCameraMoveInvalidateListener,
            initialGesturesManager,
            internalGesturesManager,
        )
    }

    private fun <T> getAnimationListener(
        @TrackAsiaAnimator.Type animatorType: Int,
        holders: Set<AnimatorListenerHolder>,
    ): AnimationsValueChangeListener<Any>? {
        for (holder in holders) {
            @TrackAsiaAnimator.Type val type = holder.animatorType
            if (type == animatorType) {
                return holder.listener
            }
        }
        return null
    }
}
