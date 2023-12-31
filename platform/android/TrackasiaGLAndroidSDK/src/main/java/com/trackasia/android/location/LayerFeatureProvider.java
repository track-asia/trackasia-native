package com.trackasia.android.location;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Point;

import static com.trackasia.android.location.LocationComponentConstants.PROPERTY_COMPASS_BEARING;
import static com.trackasia.android.location.LocationComponentConstants.PROPERTY_GPS_BEARING;
import static com.trackasia.android.location.LocationComponentConstants.PROPERTY_LOCATION_STALE;

class LayerFeatureProvider {

  @NonNull
  Feature generateLocationFeature(@Nullable Feature locationFeature, boolean isStale) {
    if (locationFeature != null) {
      return locationFeature;
    }
    locationFeature = Feature.fromGeometry(Point.fromLngLat(0.0, 0.0));
    locationFeature.addNumberProperty(PROPERTY_GPS_BEARING, 0f);
    locationFeature.addNumberProperty(PROPERTY_COMPASS_BEARING, 0f);
    locationFeature.addBooleanProperty(PROPERTY_LOCATION_STALE, isStale);
    return locationFeature;
  }
}
