package com.trackasia.android.location;

import android.content.Context;
import android.graphics.PointF;
import android.graphics.RectF;
import android.location.Location;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;

import com.mapbox.android.gestures.AndroidGesturesManager;
import com.mapbox.android.gestures.MoveGestureDetector;
import com.mapbox.android.gestures.RotateGestureDetector;
import com.trackasia.android.location.modes.CameraMode;
import com.trackasia.android.maps.MapLibreMap;
import com.trackasia.android.maps.Transform;

import java.util.HashSet;
import java.util.Set;

import static com.trackasia.android.location.LocationComponentConstants.TRANSITION_ANIMATION_DURATION_MS;

import com.trackasia.android.camera.CameraPosition;
import com.trackasia.android.camera.CameraUpdate;
import com.trackasia.android.camera.CameraUpdateFactory;
import com.trackasia.android.geometry.LatLng;

final class LocationCameraController {

  @CameraMode.Mode
  private int cameraMode;

  private final MapLibreMap trackasiaMap;
  private final Transform transform;
  private final OnCameraTrackingChangedListener internalCameraTrackingChangedListener;
  private LocationComponentOptions options;

  private final MoveGestureDetector moveGestureDetector;
  private final OnCameraMoveInvalidateListener onCameraMoveInvalidateListener;

  private final AndroidGesturesManager initialGesturesManager;
  private final AndroidGesturesManager internalGesturesManager;

  private boolean isTransitioning;
  private LatLng lastLocation;
  private boolean isEnabled;

  LocationCameraController(
    Context context,
    MapLibreMap trackasiaMap,
    Transform transform,
    OnCameraTrackingChangedListener internalCameraTrackingChangedListener,
    @NonNull LocationComponentOptions options,
    OnCameraMoveInvalidateListener onCameraMoveInvalidateListener) {
    this.trackasiaMap = trackasiaMap;
    this.transform = transform;

    initialGesturesManager = trackasiaMap.getGesturesManager();
    internalGesturesManager = new LocationGesturesManager(context);
    moveGestureDetector = internalGesturesManager.getMoveGestureDetector();
    trackasiaMap.addOnRotateListener(onRotateListener);
    trackasiaMap.addOnFlingListener(onFlingListener);
    trackasiaMap.addOnMoveListener(onMoveListener);
    trackasiaMap.addOnCameraMoveListener(onCameraMoveListener);
    this.internalCameraTrackingChangedListener = internalCameraTrackingChangedListener;
    this.onCameraMoveInvalidateListener = onCameraMoveInvalidateListener;
    initializeOptions(options);
  }

  // Package private for testing purposes
  LocationCameraController(MapLibreMap trackasiaMap,
                           Transform transform,
                           MoveGestureDetector moveGestureDetector,
                           OnCameraTrackingChangedListener internalCameraTrackingChangedListener,
                           OnCameraMoveInvalidateListener onCameraMoveInvalidateListener,
                           AndroidGesturesManager initialGesturesManager,
                           AndroidGesturesManager internalGesturesManager) {
    this.trackasiaMap = trackasiaMap;
    trackasiaMap.addOnCameraMoveListener(onCameraMoveListener);
    this.transform = transform;
    this.moveGestureDetector = moveGestureDetector;
    this.internalCameraTrackingChangedListener = internalCameraTrackingChangedListener;
    this.onCameraMoveInvalidateListener = onCameraMoveInvalidateListener;
    this.internalGesturesManager = internalGesturesManager;
    this.initialGesturesManager = initialGesturesManager;
  }

  void initializeOptions(LocationComponentOptions options) {
    this.options = options;
    if (options.trackingGesturesManagement()) {
      if (trackasiaMap.getGesturesManager() != internalGesturesManager) {
        trackasiaMap.setGesturesManager(internalGesturesManager, true, true);
      }
      adjustGesturesThresholds();
    } else if (trackasiaMap.getGesturesManager() != initialGesturesManager) {
      trackasiaMap.setGesturesManager(initialGesturesManager, true, true);
    }
  }

  void setCameraMode(@CameraMode.Mode int cameraMode) {
    setCameraMode(cameraMode, null, TRANSITION_ANIMATION_DURATION_MS, null, null, null, null);
  }

  void setCameraMode(@CameraMode.Mode final int cameraMode, @Nullable Location lastLocation,
                     long transitionDuration,
                     @Nullable Double zoom, @Nullable Double bearing, @Nullable Double tilt,
                     @Nullable OnLocationCameraTransitionListener internalTransitionListener) {
    if (this.cameraMode == cameraMode) {
      if (internalTransitionListener != null) {
        internalTransitionListener.onLocationCameraTransitionFinished(cameraMode);
      }
      return;
    }

    final boolean wasTracking = isLocationTracking();
    this.cameraMode = cameraMode;

    if (cameraMode != CameraMode.NONE) {
      trackasiaMap.cancelTransitions();
    }

    adjustGesturesThresholds();
    notifyCameraTrackingChangeListener(wasTracking);
    transitionToCurrentLocation(
      wasTracking, lastLocation, transitionDuration, zoom, bearing, tilt, internalTransitionListener);
  }

  /**
   * Initiates a camera animation to the current location if location tracking was engaged.
   * Notifies an internal listener when the transition's finished to invalidate animators and notify external listeners.
   */
  private void transitionToCurrentLocation(boolean wasTracking, Location lastLocation,
                                           long transitionDuration,
                                           Double zoom, Double bearing, Double tilt,
                                           final OnLocationCameraTransitionListener internalTransitionListener) {
    if (!wasTracking && isLocationTracking() && lastLocation != null && isEnabled) {
      isTransitioning = true;
      LatLng target = new LatLng(lastLocation);

      CameraPosition.Builder builder = new CameraPosition.Builder().target(target);
      if (zoom != null) {
        builder.zoom(zoom);
      }
      if (tilt != null) {
        builder.tilt(tilt);
      }
      if (bearing != null) {
        builder.bearing(bearing);
      } else {
        if (isLocationBearingTracking()) {
          builder.bearing(cameraMode == CameraMode.TRACKING_GPS_NORTH ? 0 : lastLocation.getBearing());
        }
      }

      CameraUpdate update = CameraUpdateFactory.newCameraPosition(builder.build());
      MapLibreMap.CancelableCallback callback = new MapLibreMap.CancelableCallback() {
        @Override
        public void onCancel() {
          isTransitioning = false;
          if (internalTransitionListener != null) {
            internalTransitionListener.onLocationCameraTransitionCanceled(cameraMode);
          }
        }

        @Override
        public void onFinish() {
          isTransitioning = false;
          if (internalTransitionListener != null) {
            internalTransitionListener.onLocationCameraTransitionFinished(cameraMode);
          }
        }
      };

      CameraPosition currentPosition = trackasiaMap.getCameraPosition();
      if (Utils.immediateAnimation(trackasiaMap.getProjection(), currentPosition.target, target)) {
        transform.moveCamera(
            trackasiaMap,
          update,
          callback);
      } else {
        transform.animateCamera(
            trackasiaMap,
          update,
          (int) transitionDuration,
          callback);
      }
    } else {
      if (internalTransitionListener != null) {
        internalTransitionListener.onLocationCameraTransitionFinished(cameraMode);
      }
    }
  }

  int getCameraMode() {
    return cameraMode;
  }

  private void setBearing(float bearing) {
    if (isTransitioning) {
      return;
    }

    transform.moveCamera(trackasiaMap, CameraUpdateFactory.bearingTo(bearing), null);
    onCameraMoveInvalidateListener.onInvalidateCameraMove();
  }

  private void setLatLng(@NonNull LatLng latLng) {
    if (isTransitioning) {
      return;
    }
    lastLocation = latLng;
    transform.moveCamera(trackasiaMap, CameraUpdateFactory.newLatLng(latLng), null);
    onCameraMoveInvalidateListener.onInvalidateCameraMove();
  }

  private void setZoom(float zoom) {
    if (isTransitioning) {
      return;
    }

    transform.moveCamera(trackasiaMap, CameraUpdateFactory.zoomTo(zoom), null);
    onCameraMoveInvalidateListener.onInvalidateCameraMove();
  }

  private void setTilt(float tilt) {
    if (isTransitioning) {
      return;
    }

    transform.moveCamera(trackasiaMap, CameraUpdateFactory.tiltTo(tilt), null);
    onCameraMoveInvalidateListener.onInvalidateCameraMove();
  }

  private final MapLibreAnimator.AnimationsValueChangeListener<LatLng> latLngValueListener =
    new MapLibreAnimator.AnimationsValueChangeListener<LatLng>() {
      @Override
      public void onNewAnimationValue(LatLng value) {
        setLatLng(value);
      }
    };

  private final MapLibreAnimator.AnimationsValueChangeListener<Float> gpsBearingValueListener =
    new MapLibreAnimator.AnimationsValueChangeListener<Float>() {
      @Override
      public void onNewAnimationValue(Float value) {
        boolean trackingNorth = cameraMode == CameraMode.TRACKING_GPS_NORTH
          && trackasiaMap.getCameraPosition().bearing == 0;

        if (!trackingNorth) {
          setBearing(value);
        }
      }
    };

  private final MapLibreAnimator.AnimationsValueChangeListener<Float> compassBearingValueListener =
    new MapLibreAnimator.AnimationsValueChangeListener<Float>() {
      @Override
      public void onNewAnimationValue(Float value) {
        if (cameraMode == CameraMode.TRACKING_COMPASS
          || cameraMode == CameraMode.NONE_COMPASS) {
          setBearing(value);
        }
      }
    };

  private final MapLibreAnimator.AnimationsValueChangeListener<Float> zoomValueListener =
    new MapLibreAnimator.AnimationsValueChangeListener<Float>() {
      @Override
      public void onNewAnimationValue(Float value) {
        setZoom(value);
      }
    };

  private final MapLibreAnimator.AnimationsValueChangeListener<Float> tiltValueListener =
    new MapLibreAnimator.AnimationsValueChangeListener<Float>() {
      @Override
      public void onNewAnimationValue(Float value) {
        setTilt(value);
      }
    };

  Set<AnimatorListenerHolder> getAnimationListeners() {
    Set<AnimatorListenerHolder> holders = new HashSet<>();
    if (isLocationTracking()) {
      holders.add(new AnimatorListenerHolder(MapLibreAnimator.ANIMATOR_CAMERA_LATLNG, latLngValueListener));
    }

    if (isLocationBearingTracking()) {
      holders.add(new AnimatorListenerHolder(MapLibreAnimator.ANIMATOR_CAMERA_GPS_BEARING, gpsBearingValueListener));
    }

    if (isConsumingCompass()) {
      holders.add(new AnimatorListenerHolder(
        MapLibreAnimator.ANIMATOR_CAMERA_COMPASS_BEARING,
        compassBearingValueListener));
    }

    holders.add(new AnimatorListenerHolder(MapLibreAnimator.ANIMATOR_ZOOM, zoomValueListener));
    holders.add(new AnimatorListenerHolder(MapLibreAnimator.ANIMATOR_TILT, tiltValueListener));
    return holders;
  }

  boolean isTransitioning() {
    return isTransitioning;
  }

  private void adjustGesturesThresholds() {
    if (options.trackingGesturesManagement()) {
      if (isLocationTracking()) {
        moveGestureDetector.setMoveThreshold(options.trackingInitialMoveThreshold());
      } else {
        moveGestureDetector.setMoveThreshold(0f);
        moveGestureDetector.setMoveThresholdRect(null);
      }
    }
  }

  boolean isConsumingCompass() {
    return cameraMode == CameraMode.TRACKING_COMPASS
      || cameraMode == CameraMode.NONE_COMPASS;
  }

  void setEnabled(boolean enabled) {
    isEnabled = enabled;
  }

  private boolean isLocationTracking() {
    return cameraMode == CameraMode.TRACKING
      || cameraMode == CameraMode.TRACKING_COMPASS
      || cameraMode == CameraMode.TRACKING_GPS
      || cameraMode == CameraMode.TRACKING_GPS_NORTH;
  }

  private boolean isBearingTracking() {
    return cameraMode == CameraMode.NONE_COMPASS
      || cameraMode == CameraMode.TRACKING_COMPASS
      || cameraMode == CameraMode.NONE_GPS
      || cameraMode == CameraMode.TRACKING_GPS
      || cameraMode == CameraMode.TRACKING_GPS_NORTH;
  }

  private boolean isLocationBearingTracking() {
    return cameraMode == CameraMode.TRACKING_GPS
      || cameraMode == CameraMode.TRACKING_GPS_NORTH
      || cameraMode == CameraMode.NONE_GPS;
  }

  private void notifyCameraTrackingChangeListener(boolean wasTracking) {
    internalCameraTrackingChangedListener.onCameraTrackingChanged(cameraMode);
    if (wasTracking && !isLocationTracking()) {
      trackasiaMap.getUiSettings().setFocalPoint(null);
      internalCameraTrackingChangedListener.onCameraTrackingDismissed();
    }
  }

  private MapLibreMap.OnCameraMoveListener onCameraMoveListener = new MapLibreMap.OnCameraMoveListener() {

    @Override
    public void onCameraMove() {
      if (isLocationTracking() && lastLocation != null && options.trackingGesturesManagement()) {
        PointF focalPoint = trackasiaMap.getProjection().toScreenLocation(lastLocation);
        trackasiaMap.getUiSettings().setFocalPoint(focalPoint);
      }
    }
  };

  @NonNull
  @VisibleForTesting
  MapLibreMap.OnMoveListener onMoveListener = new MapLibreMap.OnMoveListener() {
    private boolean interrupt;

    @Override
    public void onMoveBegin(@NonNull MoveGestureDetector detector) {
      if (options.trackingGesturesManagement() && isLocationTracking()) {
        if (detector.getPointersCount() > 1) {
          applyMultiFingerThresholdArea(detector);
          applyMultiFingerMoveThreshold(detector);
        } else {
          applySingleFingerMoveThreshold(detector);
        }
      } else {
        setCameraMode(CameraMode.NONE);
      }
    }

    private void applyMultiFingerThresholdArea(@NonNull MoveGestureDetector detector) {
      RectF currentRect = detector.getMoveThresholdRect();
      if (currentRect != null && !currentRect.equals(options.trackingMultiFingerProtectedMoveArea())) {
        detector.setMoveThresholdRect(options.trackingMultiFingerProtectedMoveArea());
        interrupt = true;
      } else if (currentRect == null && options.trackingMultiFingerProtectedMoveArea() != null) {
        detector.setMoveThresholdRect(options.trackingMultiFingerProtectedMoveArea());
        interrupt = true;
      }
    }

    private void applyMultiFingerMoveThreshold(@NonNull MoveGestureDetector detector) {
      if (detector.getMoveThreshold() != options.trackingMultiFingerMoveThreshold()) {
        detector.setMoveThreshold(options.trackingMultiFingerMoveThreshold());
        interrupt = true;
      }
    }

    private void applySingleFingerMoveThreshold(@NonNull MoveGestureDetector detector) {
      if (detector.getMoveThreshold() != options.trackingInitialMoveThreshold()) {
        detector.setMoveThreshold(options.trackingInitialMoveThreshold());
        interrupt = true;
      }
    }

    @Override
    public void onMove(@NonNull MoveGestureDetector detector) {
      if (interrupt) {
        detector.interrupt();
        return;
      }

      if (isLocationTracking() || isBearingTracking()) {
        setCameraMode(CameraMode.NONE);
        detector.interrupt();
      }
    }

    @Override
    public void onMoveEnd(@NonNull MoveGestureDetector detector) {
      if (options.trackingGesturesManagement() && !interrupt && isLocationTracking()) {
        detector.setMoveThreshold(options.trackingInitialMoveThreshold());
        detector.setMoveThresholdRect(null);
      }
      interrupt = false;
    }
  };

  @NonNull
  private MapLibreMap.OnRotateListener onRotateListener = new MapLibreMap.OnRotateListener() {
    @Override
    public void onRotateBegin(@NonNull RotateGestureDetector detector) {
      if (isBearingTracking()) {
        setCameraMode(CameraMode.NONE);
      }
    }

    @Override
    public void onRotate(@NonNull RotateGestureDetector detector) {
      // no implementation
    }

    @Override
    public void onRotateEnd(@NonNull RotateGestureDetector detector) {
      // no implementation
    }
  };

  @NonNull
  private MapLibreMap.OnFlingListener onFlingListener = new MapLibreMap.OnFlingListener() {
    @Override
    public void onFling() {
      setCameraMode(CameraMode.NONE);
    }
  };

  private class LocationGesturesManager extends AndroidGesturesManager {

    LocationGesturesManager(Context context) {
      super(context);
    }

    @Override
    public boolean onTouchEvent(@Nullable MotionEvent motionEvent) {
      if (motionEvent != null) {
        int action = motionEvent.getActionMasked();
        if (action == MotionEvent.ACTION_UP) {
          adjustGesturesThresholds();
        }
      }
      return super.onTouchEvent(motionEvent);
    }
  }
}