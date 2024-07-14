package org.trackasia.android.testapp.camera;

import org.trackasia.android.camera.CameraUpdate;
import org.trackasia.android.maps.TrackAsiaMap;

public class CameraAnimateTest extends CameraTest {
  @Override
  void executeCameraMovement(CameraUpdate cameraUpdate, TrackAsiaMap.CancelableCallback callback) {
    trackasiaMap.animateCamera(cameraUpdate, callback);
  }
}