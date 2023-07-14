package org.trackasia.android.testapp.camera;

import org.trackasia.android.camera.CameraUpdate;
import org.trackasia.android.maps.trackasiaMap;

public class CameraEaseTest extends CameraTest {

  @Override
  void executeCameraMovement(CameraUpdate cameraUpdate, trackasiaMap.CancelableCallback callback) {
    trackasiaMap.easeCamera(cameraUpdate, callback);
  }
}