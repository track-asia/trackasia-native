package com.trackasia.android.maps;

import android.graphics.RectF;

import androidx.annotation.NonNull;

import com.trackasia.android.annotations.BaseMarkerOptions;
import com.trackasia.android.annotations.Marker;

import java.util.List;

/**
 * Interface that defines convenient methods for working with a {@link Marker}'s collection.
 */
interface Markers {
  Marker addBy(@NonNull BaseMarkerOptions markerOptions, @NonNull TrackasiaMap mapboxMap);

  List<Marker> addBy(@NonNull List<? extends BaseMarkerOptions> markerOptionsList, @NonNull TrackasiaMap mapboxMap);

  void update(@NonNull Marker updatedMarker, @NonNull TrackasiaMap mapboxMap);

  List<Marker> obtainAll();

  @NonNull
  List<Marker> obtainAllIn(@NonNull RectF rectangle);

  void reload();
}
