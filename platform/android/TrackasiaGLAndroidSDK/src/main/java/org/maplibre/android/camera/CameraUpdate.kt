package org.trackasia.android.camera

import org.trackasia.android.maps.trackasiaMap

/**
 * Interface definition for camera updates.
 */
interface CameraUpdate {
    /**
     * Get the camera position from the camera update.
     *
     * @param trackasiaMap Map object to build the position from
     * @return the camera position from the implementing camera update
     */
    fun getCameraPosition(trackasiaMap: trackasiaMap): CameraPosition?
}
