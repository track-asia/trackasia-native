package com.trackasia.android.testapp.camera;

import com.trackasia.android.camera.CameraUpdate;
import com.trackasia.android.maps.MapLibreMap;

public class CameraEaseTest extends CameraTest {

  @Override
  void executeCameraMovement(CameraUpdate cameraUpdate, MapLibreMap.CancelableCallback callback) {
    trackasiaMap.easeCamera(cameraUpdate, callback);
  }
}