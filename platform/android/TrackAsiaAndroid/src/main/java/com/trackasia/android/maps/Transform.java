package com.trackasia.android.maps;

import android.graphics.PointF;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;

import com.trackasia.android.camera.CameraPosition;
import com.trackasia.android.camera.CameraUpdate;
import com.trackasia.android.camera.CameraUpdateFactory;
import com.trackasia.android.constants.TrackAsiaConstants;
import com.trackasia.android.geometry.LatLng;
import com.trackasia.android.log.Logger;

import static com.trackasia.android.maps.TrackAsiaMap.OnCameraMoveStartedListener;

/**
 * Internal use.
 * <p>
 * Resembles the current Map transformation.
 * </p>
 * <p>
 * Responsible for synchronising {@link CameraPosition} state and notifying camera change listeners.
 * </p>
 */
public final class Transform implements MapView.OnCameraDidChangeListener {

  private static final String TAG = "Mbgl-Transform";

  private final NativeMap nativeMap;
  private final MapView mapView;
  private final Handler handler = new Handler(Looper.getMainLooper());

  @Nullable
  private CameraPosition cameraPosition;
  @Nullable
  private TrackAsiaMap.CancelableCallback cameraCancelableCallback;
  private CameraChangeDispatcher cameraChangeDispatcher;

  private final MapView.OnCameraDidChangeListener moveByChangeListener = new MapView.OnCameraDidChangeListener() {
    @Override
    public void onCameraDidChange(boolean animated) {
      if (animated) {
        cameraChangeDispatcher.onCameraIdle();
        mapView.removeOnCameraDidChangeListener(this);
      }
    }
  };

  Transform(MapView mapView, NativeMap nativeMap, CameraChangeDispatcher cameraChangeDispatcher) {
    this.mapView = mapView;
    this.nativeMap = nativeMap;
    this.cameraChangeDispatcher = cameraChangeDispatcher;
  }

  void initialise(@NonNull TrackAsiaMap trackasiaMap, @NonNull TrackAsiaMapOptions options) {
    CameraPosition position = options.getCamera();
    if (position != null && !position.equals(CameraPosition.DEFAULT)) {
      moveCamera(trackasiaMap, CameraUpdateFactory.newCameraPosition(position), null);
    }
    setMinZoom(options.getMinZoomPreference());
    setMaxZoom(options.getMaxZoomPreference());
    setMinPitch(options.getMinPitchPreference());
    setMaxPitch(options.getMaxPitchPreference());
  }

  //
  // Camera API
  //

  @Nullable
  @UiThread
  public final CameraPosition getCameraPosition() {
    if (cameraPosition == null) {
      cameraPosition = invalidateCameraPosition();
    }
    return cameraPosition;
  }

  @Override
  public void onCameraDidChange(boolean animated) {
    if (animated) {
      invalidateCameraPosition();
      if (cameraCancelableCallback != null) {
        final TrackAsiaMap.CancelableCallback callback = cameraCancelableCallback;

        // nullification has to happen before Handler#post, see https://github.com/robolectric/robolectric/issues/1306
        cameraCancelableCallback = null;

        handler.post(new Runnable() {
          @Override
          public void run() {
            callback.onFinish();
          }
        });
      }
      cameraChangeDispatcher.onCameraIdle();
      mapView.removeOnCameraDidChangeListener(this);
    }
  }

  /**
   * Internal use.
   */
  @UiThread
  public final void moveCamera(@NonNull TrackAsiaMap trackasiaMap, CameraUpdate update,
                               @Nullable final TrackAsiaMap.CancelableCallback callback) {
    CameraPosition cameraPosition = update.getCameraPosition(trackasiaMap);
    if (isValidCameraPosition(cameraPosition)) {
      cancelTransitions();
      cameraChangeDispatcher.onCameraMoveStarted(OnCameraMoveStartedListener.REASON_API_ANIMATION);
      nativeMap.jumpTo(cameraPosition.target, cameraPosition.zoom, cameraPosition.tilt, cameraPosition.bearing,
        cameraPosition.padding);
      invalidateCameraPosition();
      cameraChangeDispatcher.onCameraIdle();
      handler.post(new Runnable() {
        @Override
        public void run() {
          if (callback != null) {
            callback.onFinish();
          }
        }
      });
    } else if (callback != null) {
      callback.onFinish();
    }
  }

  @UiThread
  final void easeCamera(@NonNull TrackAsiaMap trackasiaMap, CameraUpdate update, int durationMs,
                        boolean easingInterpolator,
                        @Nullable final TrackAsiaMap.CancelableCallback callback) {
    CameraPosition cameraPosition = update.getCameraPosition(trackasiaMap);
    if (isValidCameraPosition(cameraPosition)) {
      cancelTransitions();
      cameraChangeDispatcher.onCameraMoveStarted(OnCameraMoveStartedListener.REASON_API_ANIMATION);

      if (callback != null) {
        cameraCancelableCallback = callback;
      }
      mapView.addOnCameraDidChangeListener(this);
      nativeMap.easeTo(cameraPosition.target, cameraPosition.zoom, cameraPosition.bearing, cameraPosition.tilt,
        cameraPosition.padding, durationMs, easingInterpolator);
    } else if (callback != null) {
      callback.onFinish();
    }
  }

  /**
   * Internal use.
   */
  @UiThread
  public final void animateCamera(@NonNull TrackAsiaMap trackasiaMap, CameraUpdate update, int durationMs,
                                  @Nullable final TrackAsiaMap.CancelableCallback callback) {
    CameraPosition cameraPosition = update.getCameraPosition(trackasiaMap);
    if (isValidCameraPosition(cameraPosition)) {
      cancelTransitions();
      cameraChangeDispatcher.onCameraMoveStarted(OnCameraMoveStartedListener.REASON_API_ANIMATION);

      if (callback != null) {
        cameraCancelableCallback = callback;
      }
      mapView.addOnCameraDidChangeListener(this);
      nativeMap.flyTo(cameraPosition.target, cameraPosition.zoom, cameraPosition.bearing,
        cameraPosition.tilt, cameraPosition.padding, durationMs);
    } else if (callback != null) {
      callback.onFinish();
    }
  }

  private boolean isValidCameraPosition(@Nullable CameraPosition cameraPosition) {
    return cameraPosition != null && !cameraPosition.equals(this.cameraPosition);
  }

  @UiThread
  @Nullable
  CameraPosition invalidateCameraPosition() {
    if (nativeMap != null) {
      CameraPosition cameraPosition = nativeMap.getCameraPosition();
      if (this.cameraPosition != null && !this.cameraPosition.equals(cameraPosition)) {
        cameraChangeDispatcher.onCameraMove();
      }

      this.cameraPosition = cameraPosition;
    }
    return cameraPosition;
  }

  void cancelTransitions() {
    // notify user about cancel
    cameraChangeDispatcher.onCameraMoveCanceled();

    // notify animateCamera and easeCamera about cancelling
    if (cameraCancelableCallback != null) {
      final TrackAsiaMap.CancelableCallback callback = cameraCancelableCallback;
      cameraChangeDispatcher.onCameraIdle();

      // nullification has to happen before Handler#post, see https://github.com/robolectric/robolectric/issues/1306
      cameraCancelableCallback = null;

      handler.post(new Runnable() {
        @Override
        public void run() {
          callback.onCancel();
        }
      });
    }

    // cancel ongoing transitions
    nativeMap.cancelTransitions();

    cameraChangeDispatcher.onCameraIdle();
  }

  @UiThread
  void resetNorth() {
    cancelTransitions();
    nativeMap.resetNorth();
  }

  //
  // non Camera API
  //

  // Zoom in or out

  double getRawZoom() {
    return nativeMap.getZoom();
  }

  void zoomBy(double zoomAddition, @NonNull PointF focalPoint) {
    setZoom(nativeMap.getZoom() + zoomAddition, focalPoint);
  }

  void setZoom(double zoom, @NonNull PointF focalPoint) {
    nativeMap.setZoom(zoom, focalPoint, 0);
  }

  // Direction
  double getBearing() {
    double direction = -nativeMap.getBearing();

    while (direction > 360) {
      direction -= 360;
    }
    while (direction < 0) {
      direction += 360;
    }

    return direction;
  }

  double getRawBearing() {
    return nativeMap.getBearing();
  }

  void setBearing(double bearing) {
    nativeMap.setBearing(bearing, 0);
  }

  void setBearing(double bearing, float focalX, float focalY) {
    nativeMap.setBearing(bearing, focalX, focalY, 0);
  }

  void setBearing(double bearing, float focalX, float focalY, long duration) {
    nativeMap.setBearing(bearing, focalX, focalY, duration);
  }


  //
  // LatLng / CenterCoordinate
  //

  LatLng getLatLng() {
    return nativeMap.getLatLng();
  }

  //
  // Pitch / Tilt
  //

  double getTilt() {
    return nativeMap.getPitch();
  }

  void setTilt(Double pitch) {
    nativeMap.setPitch(pitch, 0);
  }

  //
  // Center coordinate
  //

  LatLng getCenterCoordinate() {
    return nativeMap.getLatLng();
  }

  void setCenterCoordinate(LatLng centerCoordinate) {
    nativeMap.setLatLng(centerCoordinate, 0);
  }

  void setGestureInProgress(boolean gestureInProgress) {
    nativeMap.setGestureInProgress(gestureInProgress);
    if (!gestureInProgress) {
      invalidateCameraPosition();
    }
  }

  void moveBy(double offsetX, double offsetY, long duration) {
    if (duration > 0) {
      mapView.addOnCameraDidChangeListener(moveByChangeListener);
    }
    nativeMap.moveBy(offsetX, offsetY, duration);
  }

  //
  // Min & Max ZoomLevel
  //

  void setMinZoom(double minZoom) {
    if ((minZoom < TrackAsiaConstants.MINIMUM_ZOOM) || (minZoom > TrackAsiaConstants.MAXIMUM_ZOOM)) {
      Logger.e(TAG, String.format("Not setting minZoomPreference, value is in unsupported range: %s", minZoom));
      return;
    }
    nativeMap.setMinZoom(minZoom);
  }

  double getMinZoom() {
    return nativeMap.getMinZoom();
  }

  void setMaxZoom(double maxZoom) {
    if ((maxZoom < TrackAsiaConstants.MINIMUM_ZOOM) || (maxZoom > TrackAsiaConstants.MAXIMUM_ZOOM)) {
      Logger.e(TAG, String.format("Not setting maxZoomPreference, value is in unsupported range: %s", maxZoom));
      return;
    }
    nativeMap.setMaxZoom(maxZoom);
  }

  double getMaxZoom() {
    return nativeMap.getMaxZoom();
  }

  void setMinPitch(double minPitch) {
    if ((minPitch < TrackAsiaConstants.MINIMUM_PITCH) || (minPitch > TrackAsiaConstants.MAXIMUM_PITCH)) {
      Logger.e(TAG, String.format("Not setting minPitchPreference, value is in unsupported range: %s", minPitch));
      return;
    }
    nativeMap.setMinPitch(minPitch);
  }

  double getMinPitch() {
    return nativeMap.getMinPitch();
  }

  void setMaxPitch(double maxPitch) {
    if ((maxPitch < TrackAsiaConstants.MINIMUM_PITCH) || (maxPitch > TrackAsiaConstants.MAXIMUM_PITCH)) {
      Logger.e(TAG, String.format("Not setting maxPitchPreference, value is in unsupported range: %s", maxPitch));
      return;
    }
    nativeMap.setMaxPitch(maxPitch);
  }

  double getMaxPitch() {
    return nativeMap.getMaxPitch();
  }
}
