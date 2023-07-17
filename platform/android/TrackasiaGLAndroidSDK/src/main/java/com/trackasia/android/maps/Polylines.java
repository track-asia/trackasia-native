package com.trackasia.android.maps;


import androidx.annotation.NonNull;

import com.trackasia.android.annotations.Polyline;
import com.trackasia.android.annotations.PolylineOptions;

import java.util.List;

/**
 * Interface that defines convenient methods for working with a {@link Polyline}'s collection.
 */
interface Polylines {
  Polyline addBy(@NonNull PolylineOptions polylineOptions, @NonNull MapLibreMap trackasiaMap);

  List<Polyline> addBy(@NonNull List<PolylineOptions> polylineOptionsList, @NonNull MapLibreMap trackasiaMap);

  void update(Polyline polyline);

  List<Polyline> obtainAll();
}
