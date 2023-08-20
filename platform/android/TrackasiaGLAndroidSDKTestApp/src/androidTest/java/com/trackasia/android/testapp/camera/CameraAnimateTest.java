package com.trackasia.android.testapp.camera;

import com.trackasia.android.camera.CameraUpdate;
import com.trackasia.android.maps.TrackasiaMap;

public class CameraAnimateTest extends CameraTest {
  @Override
  void executeCameraMovement(CameraUpdate cameraUpdate, TrackasiaMap.CancelableCallback callback) {
    mapboxMap.animateCamera(cameraUpdate, callback);
  }
}