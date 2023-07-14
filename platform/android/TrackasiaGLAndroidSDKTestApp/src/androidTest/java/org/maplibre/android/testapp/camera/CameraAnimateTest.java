package org.trackasia.android.testapp.camera;

import org.trackasia.android.camera.CameraUpdate;
import org.trackasia.android.maps.trackasiaMap;

public class CameraAnimateTest extends CameraTest {
  @Override
  void executeCameraMovement(CameraUpdate cameraUpdate, trackasiaMap.CancelableCallback callback) {
    trackasiaMap.animateCamera(cameraUpdate, callback);
  }
}