
package org.trackasia.android.testapp.camera;

import org.trackasia.android.camera.CameraUpdate;
import org.trackasia.android.maps.TrackAsiaMap;

public class CameraMoveTest extends CameraTest {
  @Override
  void executeCameraMovement(CameraUpdate cameraUpdate, TrackAsiaMap.CancelableCallback callback) {
    trackasiaMap.moveCamera(cameraUpdate, callback);
  }
}