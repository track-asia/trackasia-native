package org.trackasia.android.location;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.trackasia.geojson.Feature;
import org.trackasia.geojson.Point;

class LayerFeatureProvider {

  @NonNull
  Feature generateLocationFeature(@Nullable Feature locationFeature, boolean isStale) {
    if (locationFeature != null) {
      return locationFeature;
    }
    locationFeature = Feature.fromGeometry(Point.fromLngLat(0.0, 0.0));
    locationFeature.addNumberProperty(LocationComponentConstants.PROPERTY_GPS_BEARING, 0f);
    locationFeature.addNumberProperty(LocationComponentConstants.PROPERTY_COMPASS_BEARING, 0f);
    locationFeature.addBooleanProperty(LocationComponentConstants.PROPERTY_LOCATION_STALE, isStale);
    return locationFeature;
  }
}
