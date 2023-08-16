package com.trackasia.android.location;

import androidx.annotation.NonNull;

import com.mapbox.geojson.Feature;
import com.trackasia.android.style.layers.CircleLayer;
import com.trackasia.android.style.layers.Layer;
import com.trackasia.android.style.layers.Property;
import com.trackasia.android.style.layers.SymbolLayer;
import com.trackasia.android.style.layers.TransitionOptions;
import com.trackasia.android.style.sources.GeoJsonOptions;
import com.trackasia.android.style.sources.GeoJsonSource;

import java.util.HashSet;
import java.util.Set;

import static com.trackasia.android.location.LocationComponentConstants.ACCURACY_LAYER;
import static com.trackasia.android.location.LocationComponentConstants.BACKGROUND_LAYER;
import static com.trackasia.android.location.LocationComponentConstants.BEARING_LAYER;
import static com.trackasia.android.location.LocationComponentConstants.FOREGROUND_LAYER;
import static com.trackasia.android.location.LocationComponentConstants.LOCATION_SOURCE;
import static com.trackasia.android.location.LocationComponentConstants.PROPERTY_ACCURACY_ALPHA;
import static com.trackasia.android.location.LocationComponentConstants.PROPERTY_ACCURACY_COLOR;
import static com.trackasia.android.location.LocationComponentConstants.PROPERTY_ACCURACY_RADIUS;
import static com.trackasia.android.location.LocationComponentConstants.PROPERTY_BACKGROUND_ICON;
import static com.trackasia.android.location.LocationComponentConstants.PROPERTY_BACKGROUND_STALE_ICON;
import static com.trackasia.android.location.LocationComponentConstants.PROPERTY_BEARING_ICON;
import static com.trackasia.android.location.LocationComponentConstants.PROPERTY_COMPASS_BEARING;
import static com.trackasia.android.location.LocationComponentConstants.PROPERTY_FOREGROUND_ICON;
import static com.trackasia.android.location.LocationComponentConstants.PROPERTY_FOREGROUND_ICON_OFFSET;
import static com.trackasia.android.location.LocationComponentConstants.PROPERTY_FOREGROUND_STALE_ICON;
import static com.trackasia.android.location.LocationComponentConstants.PROPERTY_GPS_BEARING;
import static com.trackasia.android.location.LocationComponentConstants.PROPERTY_LOCATION_STALE;
import static com.trackasia.android.location.LocationComponentConstants.PROPERTY_SHADOW_ICON_OFFSET;
import static com.trackasia.android.location.LocationComponentConstants.PULSING_CIRCLE_LAYER;
import static com.trackasia.android.location.LocationComponentConstants.SHADOW_ICON;
import static com.trackasia.android.location.LocationComponentConstants.SHADOW_LAYER;
import static com.trackasia.android.style.expressions.Expression.get;
import static com.trackasia.android.style.expressions.Expression.literal;
import static com.trackasia.android.style.expressions.Expression.match;
import static com.trackasia.android.style.expressions.Expression.stop;
import static com.trackasia.android.style.expressions.Expression.switchCase;
import static com.trackasia.android.style.layers.Property.ICON_ROTATION_ALIGNMENT_MAP;
import static com.trackasia.android.style.layers.PropertyFactory.circleColor;
import static com.trackasia.android.style.layers.PropertyFactory.circleOpacity;
import static com.trackasia.android.style.layers.PropertyFactory.circlePitchAlignment;
import static com.trackasia.android.style.layers.PropertyFactory.circleRadius;
import static com.trackasia.android.style.layers.PropertyFactory.circleStrokeColor;
import static com.trackasia.android.style.layers.PropertyFactory.iconAllowOverlap;
import static com.trackasia.android.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.trackasia.android.style.layers.PropertyFactory.iconImage;
import static com.trackasia.android.style.layers.PropertyFactory.iconOffset;
import static com.trackasia.android.style.layers.PropertyFactory.iconRotate;
import static com.trackasia.android.style.layers.PropertyFactory.iconRotationAlignment;

class LayerSourceProvider {

  private static final String EMPTY_STRING = "";

  @NonNull
  GeoJsonSource generateSource(Feature locationFeature) {
    return new GeoJsonSource(
      LOCATION_SOURCE,
      locationFeature,
      new GeoJsonOptions().withMaxZoom(16)
    );
  }

  @NonNull
  Layer generateLayer(@NonNull String layerId) {
    SymbolLayer layer = new SymbolLayer(layerId, LOCATION_SOURCE);
    layer.setProperties(
      iconAllowOverlap(true),
      iconIgnorePlacement(true),
      iconRotationAlignment(ICON_ROTATION_ALIGNMENT_MAP),
      iconRotate(
        match(literal(layerId), literal(0f),
          stop(FOREGROUND_LAYER, get(PROPERTY_GPS_BEARING)),
          stop(BACKGROUND_LAYER, get(PROPERTY_GPS_BEARING)),
          stop(SHADOW_LAYER, get(PROPERTY_GPS_BEARING)),
          stop(BEARING_LAYER, get(PROPERTY_COMPASS_BEARING))
        )
      ),
      iconImage(
        match(literal(layerId), literal(EMPTY_STRING),
          stop(FOREGROUND_LAYER, switchCase(
            get(PROPERTY_LOCATION_STALE), get(PROPERTY_FOREGROUND_STALE_ICON),
            get(PROPERTY_FOREGROUND_ICON))),
          stop(BACKGROUND_LAYER, switchCase(
            get(PROPERTY_LOCATION_STALE), get(PROPERTY_BACKGROUND_STALE_ICON),
            get(PROPERTY_BACKGROUND_ICON))),
          stop(SHADOW_LAYER, literal(SHADOW_ICON)),
          stop(BEARING_LAYER, get(PROPERTY_BEARING_ICON))
        )
      ),
      iconOffset(
        match(literal(layerId), literal(new Float[] {0f, 0f}),
          stop(literal(FOREGROUND_LAYER), get(PROPERTY_FOREGROUND_ICON_OFFSET)),
          stop(literal(SHADOW_LAYER), get(PROPERTY_SHADOW_ICON_OFFSET))
        )
      )
    );
    return layer;
  }

  @NonNull
  Layer generateAccuracyLayer() {
    return new CircleLayer(ACCURACY_LAYER, LOCATION_SOURCE)
      .withProperties(
        circleRadius(get(PROPERTY_ACCURACY_RADIUS)),
        circleColor(get(PROPERTY_ACCURACY_COLOR)),
        circleOpacity(get(PROPERTY_ACCURACY_ALPHA)),
        circleStrokeColor(get(PROPERTY_ACCURACY_COLOR)),
        circlePitchAlignment(Property.CIRCLE_PITCH_ALIGNMENT_MAP)
      );
  }

  Set<String> getEmptyLayerSet() {
    return new HashSet<>();
  }

  LocationLayerRenderer getSymbolLocationLayerRenderer(LayerFeatureProvider featureProvider,
                                                       boolean isStale) {
    return new SymbolLocationLayerRenderer(this, featureProvider, isStale);
  }

  LocationLayerRenderer getIndicatorLocationLayerRenderer() {
    return new IndicatorLocationLayerRenderer(this);
  }

  Layer generateLocationComponentLayer() {
    LocationIndicatorLayer layer = new LocationIndicatorLayer(FOREGROUND_LAYER);
    layer.setLocationTransition(new TransitionOptions(0, 0));
    layer.setProperties(
        LocationPropertyFactory.perspectiveCompensation(0.9f),
        LocationPropertyFactory.imageTiltDisplacement(4f)
    );
    return layer;
  }

  /**
   * Adds a {@link CircleLayer} to the map to support the {@link LocationComponent} pulsing UI functionality.
   *
   * @return a {@link CircleLayer} with the correct data-driven styling. Tilting the map will keep the pulsing
   * layer aligned with the map plane.
   */
  @NonNull
  Layer generatePulsingCircleLayer() {
    return new CircleLayer(PULSING_CIRCLE_LAYER, LOCATION_SOURCE)
        .withProperties(
            circlePitchAlignment(Property.CIRCLE_PITCH_ALIGNMENT_MAP)
        );
  }
}
