package org.trackasia.android.maps;


import androidx.annotation.NonNull;

import org.trackasia.android.annotations.Polygon;
import org.trackasia.android.annotations.PolygonOptions;

import java.util.List;

/**
 * Interface that defines convenient methods for working with a {@link Polygon}'s collection.
 */
interface Polygons {
  Polygon addBy(@NonNull PolygonOptions polygonOptions, @NonNull trackasiaMap trackasiaMap);

  List<Polygon> addBy(@NonNull List<PolygonOptions> polygonOptionsList, @NonNull trackasiaMap trackasiaMap);

  void update(Polygon polygon);

  List<Polygon> obtainAll();
}
