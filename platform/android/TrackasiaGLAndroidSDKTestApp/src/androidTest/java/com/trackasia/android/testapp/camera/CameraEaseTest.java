package com.trackasia.android.testapp.camera;

import com.trackasia.android.camera.CameraUpdate;
import com.trackasia.android.maps.MapboxMap;

public class CameraEaseTest extends CameraTest {

  @Override
  void executeCameraMovement(CameraUpdate cameraUpdate, MapboxMap.CancelableCallback callback) {
    mapboxMap.easeCamera(cameraUpdate, callback);
  }
}