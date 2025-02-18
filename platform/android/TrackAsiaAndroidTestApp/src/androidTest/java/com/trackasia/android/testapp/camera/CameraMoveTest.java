
package com.trackasia.android.testapp.camera;

import com.trackasia.android.camera.CameraUpdate;
import com.trackasia.android.maps.TrackAsiaMap;

public class CameraMoveTest extends CameraTest {
  @Override
  void executeCameraMovement(CameraUpdate cameraUpdate, TrackAsiaMap.CancelableCallback callback) {
    trackasiaMap.moveCamera(cameraUpdate, callback);
  }
}
