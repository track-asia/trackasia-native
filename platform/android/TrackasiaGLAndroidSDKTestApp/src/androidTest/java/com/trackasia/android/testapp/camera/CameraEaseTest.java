package com.trackasia.android.testapp.camera;

import com.trackasia.android.camera.CameraUpdate;
import com.trackasia.android.maps.TrackasiaMap;

public class CameraEaseTest extends CameraTest {

  @Override
  void executeCameraMovement(CameraUpdate cameraUpdate, TrackasiaMap.CancelableCallback callback) {
    trackasiaMap.easeCamera(cameraUpdate, callback);
  }
}