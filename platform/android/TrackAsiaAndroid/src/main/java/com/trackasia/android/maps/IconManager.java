package com.trackasia.android.maps;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import com.trackasia.android.TrackAsia;
import com.trackasia.android.annotations.Icon;
import com.trackasia.android.annotations.IconFactory;
import com.trackasia.android.annotations.Marker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Responsible for managing icons added to the Map.
 * <p>
 * Maintains a {@link List} of {@link Icon} and  is responsible for initialising default markers.
 * </p>
 * <p>
 * Keep track of icons added and the resulting average icon size. This is used internally by our
 * gestures detection to calculate the size of a touch target.
 * </p>
 */
class IconManager {

  private final Map<Icon, Integer> iconMap = new HashMap<>();

  private NativeMap nativeMap;
  private int highestIconWidth;
  private int highestIconHeight;

  IconManager(NativeMap nativeMap) {
    this.nativeMap = nativeMap;
  }

  Icon loadIconForMarker(@NonNull Marker marker) {
    Icon icon = marker.getIcon();
    if (icon == null) {
      // TODO replace with anchor implementation, we are faking an anchor by adding extra pixels and diving height by 2
      icon = loadDefaultIconForMarker(marker);
    } else {
      updateHighestIconSize(icon);
    }
    addIcon(icon);
    return icon;
  }

  int getTopOffsetPixelsForIcon(@NonNull Icon icon) {
    return (int) (nativeMap.getTopOffsetPixelsForAnnotationSymbol(icon.getId()) * nativeMap.getPixelRatio());
  }

  int getHighestIconWidth() {
    return highestIconWidth;
  }

  int getHighestIconHeight() {
    return highestIconHeight;
  }

  private Icon loadDefaultIconForMarker(Marker marker) {
    Icon icon = IconFactory.getInstance(TrackAsia.getApplicationContext()).defaultMarker();
    Bitmap bitmap = icon.getBitmap();
    updateHighestIconSize(bitmap.getWidth(), bitmap.getHeight() / 2);
    marker.setIcon(icon);
    return icon;
  }

  private void addIcon(@NonNull Icon icon) {
    addIcon(icon, true);
  }

  private void addIcon(@NonNull Icon icon, boolean addIconToMap) {
    if (!iconMap.keySet().contains(icon)) {
      iconMap.put(icon, 1);
      if (addIconToMap) {
        loadIcon(icon);
      }
    } else {
      iconMap.put(icon, iconMap.get(icon) + 1);
    }
  }

  private void updateHighestIconSize(Icon icon) {
    updateHighestIconSize(icon.getBitmap());
  }

  private void updateHighestIconSize(Bitmap bitmap) {
    updateHighestIconSize(bitmap.getWidth(), bitmap.getHeight());
  }

  private void updateHighestIconSize(int width, int height) {
    if (width > highestIconWidth) {
      highestIconWidth = width;
    }

    if (height > highestIconHeight) {
      highestIconHeight = height;
    }
  }

  private void loadIcon(Icon icon) {
    Bitmap bitmap = icon.getBitmap();
    nativeMap.addAnnotationIcon(icon.getId(),
      bitmap.getWidth(),
      bitmap.getHeight(),
      icon.getScale(),
      icon.toBytes());
  }

  void reloadIcons() {
    for (Icon icon : iconMap.keySet()) {
      loadIcon(icon);
    }
  }

  void ensureIconLoaded(@NonNull Marker marker, @NonNull TrackAsiaMap trackasiaMap) {
    Icon icon = marker.getIcon();
    if (icon == null) {
      icon = loadDefaultIconForMarker(marker);
    }
    addIcon(icon);
    setTopOffsetPixels(marker, trackasiaMap, icon);
  }

  private void setTopOffsetPixels(Marker marker, @NonNull TrackAsiaMap trackasiaMap, @NonNull Icon icon) {
    // this seems to be a costly operation according to the profiler so I'm trying to save some calls
    Marker previousMarker = marker.getId() != -1 ? (Marker) trackasiaMap.getAnnotation(marker.getId()) : null;
    if (previousMarker == null || previousMarker.getIcon() == null || previousMarker.getIcon() != marker.getIcon()) {
      marker.setTopOffsetPixels(getTopOffsetPixelsForIcon(icon));
    }
  }

  void iconCleanup(@NonNull Icon icon) {
    Integer refCounter = iconMap.get(icon);
    if (refCounter != null) {
      refCounter--;
      if (refCounter == 0) {
        remove(icon);
      } else {
        updateIconRefCounter(icon, refCounter);
      }
    }
  }

  private void remove(Icon icon) {
    nativeMap.removeAnnotationIcon(icon.getId());
    iconMap.remove(icon);
  }

  private void updateIconRefCounter(Icon icon, int refCounter) {
    iconMap.put(icon, refCounter);
  }

}
