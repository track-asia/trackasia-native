
package com.trackasia.android.testapp.camera;

import com.trackasia.android.camera.CameraUpdate;
import com.trackasia.android.maps.MapLibreMap;

public class CameraMoveTest extends CameraTest {
  @Override
  void executeCameraMovement(CameraUpdate cameraUpdate, MapLibreMap.CancelableCallback callback) {
    trackasiaMap.moveCamera(cameraUpdate, callback);
  }
}