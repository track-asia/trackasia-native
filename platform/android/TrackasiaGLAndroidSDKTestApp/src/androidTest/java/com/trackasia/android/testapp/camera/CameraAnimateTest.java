package com.trackasia.android.testapp.camera;

import com.trackasia.android.camera.CameraUpdate;
import com.trackasia.android.maps.MapLibreMap;

public class CameraAnimateTest extends CameraTest {
  @Override
  void executeCameraMovement(CameraUpdate cameraUpdate, MapLibreMap.CancelableCallback callback) {
    trackasiaMap.animateCamera(cameraUpdate, callback);
  }
}