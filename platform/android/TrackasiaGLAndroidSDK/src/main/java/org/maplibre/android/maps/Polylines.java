package org.trackasia.android.maps;


import androidx.annotation.NonNull;

import org.trackasia.android.annotations.Polyline;
import org.trackasia.android.annotations.PolylineOptions;

import java.util.List;

/**
 * Interface that defines convenient methods for working with a {@link Polyline}'s collection.
 */
interface Polylines {
  Polyline addBy(@NonNull PolylineOptions polylineOptions, @NonNull trackasiaMap trackasiaMap);

  List<Polyline> addBy(@NonNull List<PolylineOptions> polylineOptionsList, @NonNull trackasiaMap trackasiaMap);

  void update(Polyline polyline);

  List<Polyline> obtainAll();
}
