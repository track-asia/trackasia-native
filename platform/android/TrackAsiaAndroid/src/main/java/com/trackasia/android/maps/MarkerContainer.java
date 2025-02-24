package com.trackasia.android.maps;

import android.graphics.RectF;

import androidx.annotation.NonNull;
import androidx.collection.LongSparseArray;

import com.trackasia.android.annotations.Annotation;
import com.trackasia.android.annotations.BaseMarkerOptions;
import com.trackasia.android.annotations.Icon;
import com.trackasia.android.annotations.Marker;

import java.util.ArrayList;
import java.util.List;

/**
 * Encapsulates {@link Marker}'s functionality.
 */
class MarkerContainer implements Markers {

  private final NativeMap nativeMapView;
  private final LongSparseArray<Annotation> annotations;
  private final IconManager iconManager;

  MarkerContainer(NativeMap nativeMapView, LongSparseArray<Annotation> annotations, IconManager iconManager) {
    this.nativeMapView = nativeMapView;
    this.annotations = annotations;
    this.iconManager = iconManager;
  }

  @Override
  public Marker addBy(@NonNull BaseMarkerOptions markerOptions, @NonNull TrackAsiaMap trackasiaMap) {
    Marker marker = prepareMarker(markerOptions);
    long id = nativeMapView != null ? nativeMapView.addMarker(marker) : 0;
    marker.setTrackAsiaMap(trackasiaMap);
    marker.setId(id);
    annotations.put(id, marker);
    return marker;
  }

  @NonNull
  @Override
  public List<Marker> addBy(@NonNull List<? extends BaseMarkerOptions> markerOptionsList, @NonNull TrackAsiaMap
      trackasiaMap) {
    int count = markerOptionsList.size();
    List<Marker> markers = new ArrayList<>(count);
    if (nativeMapView != null && count > 0) {
      BaseMarkerOptions markerOptions;
      Marker marker;
      for (int i = 0; i < count; i++) {
        markerOptions = markerOptionsList.get(i);
        marker = prepareMarker(markerOptions);
        markers.add(marker);
      }

      if (markers.size() > 0) {
        long[] ids = nativeMapView.addMarkers(markers);
        for (int i = 0; i < ids.length; i++) {
          Marker createdMarker = markers.get(i);
          createdMarker.setTrackAsiaMap(trackasiaMap);
          createdMarker.setId(ids[i]);
          annotations.put(ids[i], createdMarker);
        }
      }
    }
    return markers;
  }

  @Override
  public void update(@NonNull Marker updatedMarker, @NonNull TrackAsiaMap trackasiaMap) {
    ensureIconLoaded(updatedMarker, trackasiaMap);
    nativeMapView.updateMarker(updatedMarker);
    annotations.setValueAt(annotations.indexOfKey(updatedMarker.getId()), updatedMarker);
  }

  @NonNull
  @Override
  public List<Marker> obtainAll() {
    List<Marker> markers = new ArrayList<>();
    Annotation annotation;
    for (int i = 0; i < annotations.size(); i++) {
      annotation = annotations.get(annotations.keyAt(i));
      if (annotation instanceof Marker) {
        markers.add((Marker) annotation);
      }
    }
    return markers;
  }

  @NonNull
  @Override
  public List<Marker> obtainAllIn(@NonNull RectF rectangle) {
    RectF rect = nativeMapView.getDensityDependantRectangle(rectangle);
    long[] ids = nativeMapView.queryPointAnnotations(rect);
    List<Long> idsList = new ArrayList<>(ids.length);
    for (long id : ids) {
      idsList.add(id);
    }

    List<Marker> annotations = new ArrayList<>(ids.length);
    List<Annotation> annotationList = obtainAnnotations();
    int count = annotationList.size();
    for (int i = 0; i < count; i++) {
      Annotation annotation = annotationList.get(i);
      if (annotation instanceof com.trackasia.android.annotations.Marker && idsList.contains(annotation.getId())) {
        annotations.add((com.trackasia.android.annotations.Marker) annotation);
      }
    }

    return new ArrayList<>(annotations);
  }

  @Override
  public void reload() {
    iconManager.reloadIcons();
    int count = annotations.size();
    for (int i = 0; i < count; i++) {
      Annotation annotation = annotations.get(i);
      if (annotation instanceof Marker) {
        Marker marker = (Marker) annotation;
        nativeMapView.removeAnnotation(annotation.getId());
        long newId = nativeMapView.addMarker(marker);
        marker.setId(newId);
      }
    }
  }

  private Marker prepareMarker(BaseMarkerOptions markerOptions) {
    Marker marker = markerOptions.getMarker();
    Icon icon = iconManager.loadIconForMarker(marker);
    marker.setTopOffsetPixels(iconManager.getTopOffsetPixelsForIcon(icon));
    return marker;
  }

  private void ensureIconLoaded(Marker marker, @NonNull TrackAsiaMap trackasiaMap) {
    iconManager.ensureIconLoaded(marker, trackasiaMap);
  }

  @NonNull
  private List<Annotation> obtainAnnotations() {
    List<Annotation> annotations = new ArrayList<>();
    for (int i = 0; i < this.annotations.size(); i++) {
      annotations.add(this.annotations.get(this.annotations.keyAt(i)));
    }
    return annotations;
  }
}
