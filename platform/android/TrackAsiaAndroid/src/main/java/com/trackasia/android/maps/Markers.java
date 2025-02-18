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
  Marker addBy(@NonNull BaseMarkerOptions markerOptions, @NonNull TrackAsiaMap trackasiaMap);

  List<Marker> addBy(@NonNull List<? extends BaseMarkerOptions> markerOptionsList, @NonNull TrackAsiaMap trackasiaMap);

  void update(@NonNull Marker updatedMarker, @NonNull TrackAsiaMap trackasiaMap);

  List<Marker> obtainAll();

  @NonNull
  List<Marker> obtainAllIn(@NonNull RectF rectangle);

  void reload();
}
