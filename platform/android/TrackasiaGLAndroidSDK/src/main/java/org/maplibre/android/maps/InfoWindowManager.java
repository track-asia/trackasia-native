package org.trackasia.android.maps;

import android.text.TextUtils;

import androidx.annotation.Nullable;

import org.trackasia.android.annotations.InfoWindow;
import org.trackasia.android.annotations.Marker;

import java.util.ArrayList;
import java.util.List;

/**
 * Responsible for managing InfoWindows shown on the Map.
 * <p>
 * Maintains a {@link List} of opened {@link InfoWindow} and tracks configurations as
 * allowConcurrentMultipleInfoWindows which allows to have multiple {@link InfoWindow} open at the
 * same time. Responsible for managing listeners as
 * {@link trackasiaMap.OnInfoWindowClickListener} and
 * {@link trackasiaMap.OnInfoWindowLongClickListener}.
 * </p>
 */
class InfoWindowManager {

  private final List<InfoWindow> infoWindows = new ArrayList<>();

  @Nullable
  private trackasiaMap.InfoWindowAdapter infoWindowAdapter;
  private boolean allowConcurrentMultipleInfoWindows;

  @Nullable
  private trackasiaMap.OnInfoWindowClickListener onInfoWindowClickListener;
  @Nullable
  private trackasiaMap.OnInfoWindowLongClickListener onInfoWindowLongClickListener;
  @Nullable
  private trackasiaMap.OnInfoWindowCloseListener onInfoWindowCloseListener;

  void update() {
    if (!infoWindows.isEmpty()) {
      for (InfoWindow infoWindow : infoWindows) {
        infoWindow.update();
      }
    }
  }

  void setInfoWindowAdapter(@Nullable trackasiaMap.InfoWindowAdapter infoWindowAdapter) {
    this.infoWindowAdapter = infoWindowAdapter;
  }

  @Nullable
  trackasiaMap.InfoWindowAdapter getInfoWindowAdapter() {
    return infoWindowAdapter;
  }

  void setAllowConcurrentMultipleOpenInfoWindows(boolean allow) {
    allowConcurrentMultipleInfoWindows = allow;
  }

  boolean isAllowConcurrentMultipleOpenInfoWindows() {
    return allowConcurrentMultipleInfoWindows;
  }

  boolean isInfoWindowValidForMarker(@Nullable Marker marker) {
    return marker != null && (!TextUtils.isEmpty(marker.getTitle()) || !TextUtils.isEmpty(marker.getSnippet()));
  }

  void setOnInfoWindowClickListener(@Nullable trackasiaMap.OnInfoWindowClickListener listener) {
    onInfoWindowClickListener = listener;
  }

  @Nullable
  trackasiaMap.OnInfoWindowClickListener getOnInfoWindowClickListener() {
    return onInfoWindowClickListener;
  }

  void setOnInfoWindowLongClickListener(@Nullable trackasiaMap.OnInfoWindowLongClickListener listener) {
    onInfoWindowLongClickListener = listener;
  }

  @Nullable
  trackasiaMap.OnInfoWindowLongClickListener getOnInfoWindowLongClickListener() {
    return onInfoWindowLongClickListener;
  }

  void setOnInfoWindowCloseListener(@Nullable trackasiaMap.OnInfoWindowCloseListener listener) {
    onInfoWindowCloseListener = listener;
  }

  @Nullable
  trackasiaMap.OnInfoWindowCloseListener getOnInfoWindowCloseListener() {
    return onInfoWindowCloseListener;
  }

  public void add(InfoWindow infoWindow) {
    infoWindows.add(infoWindow);
  }
}
