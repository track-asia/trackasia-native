package com.trackasia.android.location;

import android.graphics.Bitmap;
import android.graphics.PointF;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.trackasia.geojson.Feature;
import com.trackasia.android.location.modes.RenderMode;
import com.trackasia.android.log.Logger;
import com.trackasia.android.maps.TrackAsiaMap;
import com.trackasia.android.maps.Style;
import com.trackasia.android.style.expressions.Expression;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.trackasia.android.location.LocationComponentConstants.BACKGROUND_ICON;
import static com.trackasia.android.location.LocationComponentConstants.BACKGROUND_LAYER;
import static com.trackasia.android.location.LocationComponentConstants.BACKGROUND_STALE_ICON;
import static com.trackasia.android.location.LocationComponentConstants.BEARING_ICON;
import static com.trackasia.android.location.LocationComponentConstants.BEARING_LAYER;
import static com.trackasia.android.location.LocationComponentConstants.FOREGROUND_ICON;
import static com.trackasia.android.location.LocationComponentConstants.FOREGROUND_LAYER;
import static com.trackasia.android.location.LocationComponentConstants.FOREGROUND_STALE_ICON;
import static com.trackasia.android.style.expressions.Expression.interpolate;
import static com.trackasia.android.style.expressions.Expression.linear;
import static com.trackasia.android.style.expressions.Expression.stop;
import static com.trackasia.android.style.expressions.Expression.zoom;

import com.trackasia.android.geometry.LatLng;

final class LocationLayerController {

  private static final String TAG = "Mbgl-LocationLayerController";

  @RenderMode.Mode
  private int renderMode;

  private final TrackAsiaMap trackasiaMap;
  private final LayerBitmapProvider bitmapProvider;
  private LocationComponentOptions options;
  private final OnRenderModeChangedListener internalRenderModeChangedListener;
  private final boolean useSpecializedLocationLayer;

  private boolean isHidden = true;
  private boolean isStale;

  private LocationComponentPositionManager positionManager;

  private LocationLayerRenderer locationLayerRenderer;

  LocationLayerController(TrackAsiaMap trackasiaMap, Style style,
                          LayerSourceProvider layerSourceProvider,
                          LayerFeatureProvider featureProvider,
                          LayerBitmapProvider bitmapProvider,
                          @NonNull LocationComponentOptions options,
                          @NonNull OnRenderModeChangedListener internalRenderModeChangedListener,
                          boolean useSpecializedLocationLayer) {
    this.trackasiaMap = trackasiaMap;
    this.bitmapProvider = bitmapProvider;
    this.internalRenderModeChangedListener = internalRenderModeChangedListener;
    this.useSpecializedLocationLayer = useSpecializedLocationLayer;
    this.isStale = options.enableStaleState();
    if (useSpecializedLocationLayer) {
      locationLayerRenderer = layerSourceProvider.getIndicatorLocationLayerRenderer();
    } else {
      locationLayerRenderer =
        layerSourceProvider.getSymbolLocationLayerRenderer(featureProvider, isStale);
    }
    initializeComponents(style, options);
  }

  void initializeComponents(Style style, LocationComponentOptions options) {
    this.positionManager = new LocationComponentPositionManager(style, options.layerAbove(), options.layerBelow());
    locationLayerRenderer.initializeComponents(style);
    locationLayerRenderer.addLayers(positionManager);
    applyStyle(options);

    if (isHidden) {
      hide();
    } else {
      show();
    }
  }

  void applyStyle(@NonNull LocationComponentOptions options) {
    if (positionManager.update(options.layerAbove(), options.layerBelow())) {
      locationLayerRenderer.removeLayers();
      locationLayerRenderer.addLayers(positionManager);
      if (isHidden) {
        hide();
      }
    }

    this.options = options;
    styleBitmaps(options);
    locationLayerRenderer.styleAccuracy(options.accuracyAlpha(), options.accuracyColor());
    styleScaling(options);
    locationLayerRenderer.stylePulsingCircle(options);
    determineIconsSource(options);

    if (!isHidden) {
      show();
    }
  }

  void setGpsBearing(float gpsBearing) {
    locationLayerRenderer.setGpsBearing(gpsBearing);
  }

  void setRenderMode(@RenderMode.Mode int renderMode) {
    if (this.renderMode == renderMode) {
      return;
    }
    this.renderMode = renderMode;

    styleBitmaps(options);
    determineIconsSource(options);
    if (!isHidden) {
      show();
    }
    internalRenderModeChangedListener.onRenderModeChanged(renderMode);
  }

  int getRenderMode() {
    return renderMode;
  }

  //
  // Layer action
  //

  void show() {
    isHidden = false;
    locationLayerRenderer.show(renderMode, isStale);
  }

  void hide() {
    isHidden = true;
    locationLayerRenderer.hide();
  }

  boolean isHidden() {
    return isHidden;
  }

  boolean isConsumingCompass() {
    return renderMode == RenderMode.COMPASS;
  }

  //
  // Styling
  //

  private void styleBitmaps(LocationComponentOptions options) {
    // shadow
    Bitmap shadowBitmap = null;
    if (options.elevation() > 0) {
      // Only add icon elevation if the values greater than 0.
      shadowBitmap = bitmapProvider.generateShadowBitmap(options);
    }

    // background
    Bitmap backgroundBitmap = bitmapProvider.generateBitmap(
      options.backgroundDrawable(), options.backgroundTintColor()
    );
    Bitmap backgroundStaleBitmap = bitmapProvider.generateBitmap(
      options.backgroundDrawableStale(), options.backgroundStaleTintColor()
    );

    // compass bearing
    Bitmap bearingBitmap = bitmapProvider.generateBitmap(options.bearingDrawable(), options.bearingTintColor());

    // foreground
    Bitmap foregroundBitmap = bitmapProvider.generateBitmap(
      options.foregroundDrawable(), options.foregroundTintColor()
    );
    Bitmap foregroundStaleBitmap = bitmapProvider.generateBitmap(
      options.foregroundDrawableStale(), options.foregroundStaleTintColor()
    );
    if (renderMode == RenderMode.GPS) {
      foregroundBitmap = bitmapProvider.generateBitmap(
        options.gpsDrawable(), options.foregroundTintColor()
      );
      foregroundStaleBitmap = bitmapProvider.generateBitmap(
        options.gpsDrawable(), options.foregroundStaleTintColor()
      );
    }

    locationLayerRenderer.addBitmaps(
      renderMode,
      shadowBitmap,
      backgroundBitmap,
      backgroundStaleBitmap,
      bearingBitmap,
      foregroundBitmap,
      foregroundStaleBitmap
    );
  }

  private void styleScaling(@NonNull LocationComponentOptions options) {
    Expression scaleExpression = interpolate(linear(), zoom(),
      stop(trackasiaMap.getMinZoomLevel(), options.minZoomIconScale()),
      stop(trackasiaMap.getMaxZoomLevel(), options.maxZoomIconScale())
    );

    locationLayerRenderer.styleScaling(scaleExpression);
  }

  private void determineIconsSource(LocationComponentOptions options) {
    String foregroundIconString = buildIconString(
      renderMode == RenderMode.GPS ? options.gpsName() : options.foregroundName(), FOREGROUND_ICON);
    String foregroundStaleIconString = buildIconString(options.foregroundStaleName(), FOREGROUND_STALE_ICON);
    String backgroundIconString = buildIconString(options.backgroundName(), BACKGROUND_ICON);
    String backgroundStaleIconString = buildIconString(options.backgroundStaleName(), BACKGROUND_STALE_ICON);
    String bearingIconString = buildIconString(options.bearingName(), BEARING_ICON);

    locationLayerRenderer.updateIconIds(
      foregroundIconString,
      foregroundStaleIconString,
      backgroundIconString,
      backgroundStaleIconString,
      bearingIconString
    );
  }

  @NonNull
  private String buildIconString(@Nullable String bitmapName, @NonNull String drawableName) {
    if (bitmapName != null) {
      if (useSpecializedLocationLayer) {
        Logger.e(TAG, bitmapName + " replacement ID provided for an unsupported specialized location layer");
        return drawableName;
      }
      return bitmapName;
    }
    return drawableName;
  }

  void setLocationsStale(boolean isStale) {
    this.isStale = isStale;
    locationLayerRenderer.setLocationStale(isStale, renderMode);
  }

  //
  // Map click event
  //

  boolean onMapClick(@NonNull LatLng point) {
    PointF screenLoc = trackasiaMap.getProjection().toScreenLocation(point);
    List<Feature> features = trackasiaMap.queryRenderedFeatures(screenLoc,
      BACKGROUND_LAYER,
      FOREGROUND_LAYER,
      BEARING_LAYER
    );
    return !features.isEmpty();
  }

  private final TrackAsiaAnimator.AnimationsValueChangeListener<LatLng> latLngValueListener =
    new TrackAsiaAnimator.AnimationsValueChangeListener<LatLng>() {
      @Override
      public void onNewAnimationValue(LatLng value) {
        locationLayerRenderer.setLatLng(value);
      }
  };

  private final TrackAsiaAnimator.AnimationsValueChangeListener<Float> gpsBearingValueListener =
    new TrackAsiaAnimator.AnimationsValueChangeListener<Float>() {
      @Override
      public void onNewAnimationValue(Float value) {
        locationLayerRenderer.setGpsBearing(value);
      }
  };

  private final TrackAsiaAnimator.AnimationsValueChangeListener<Float> compassBearingValueListener =
    new TrackAsiaAnimator.AnimationsValueChangeListener<Float>() {
      @Override
      public void onNewAnimationValue(Float value) {
        locationLayerRenderer.setCompassBearing(value);
      }
  };

  private final TrackAsiaAnimator.AnimationsValueChangeListener<Float> accuracyValueListener =
    new TrackAsiaAnimator.AnimationsValueChangeListener<Float>() {
      @Override
      public void onNewAnimationValue(Float value) {
        locationLayerRenderer.setAccuracyRadius(value);
      }
  };

  /**
   * The listener that handles the updating of the pulsing circle's radius and opacity.
   */
  private final TrackAsiaAnimator.AnimationsValueChangeListener<Float> pulsingCircleRadiusListener =
    new TrackAsiaAnimator.AnimationsValueChangeListener<Float>() {
      @Override
      public void onNewAnimationValue(Float newPulseRadiusValue) {
        Float newPulseOpacityValue = null;
        if (options.pulseFadeEnabled()) {
          newPulseOpacityValue = (float) 1 - ((newPulseRadiusValue / 100) * 3);
        }
        locationLayerRenderer.updatePulsingUi(newPulseRadiusValue, newPulseOpacityValue);
      }
  };

  Set<AnimatorListenerHolder> getAnimationListeners() {
    Set<AnimatorListenerHolder> holders = new HashSet<>();
    holders.add(new AnimatorListenerHolder(TrackAsiaAnimator.ANIMATOR_LAYER_LATLNG, latLngValueListener));

    if (renderMode == RenderMode.GPS) {
      holders.add(new AnimatorListenerHolder(TrackAsiaAnimator.ANIMATOR_LAYER_GPS_BEARING, gpsBearingValueListener));
    } else if (renderMode == RenderMode.COMPASS) {
      holders.add(
        new AnimatorListenerHolder(TrackAsiaAnimator.ANIMATOR_LAYER_COMPASS_BEARING, compassBearingValueListener));
    }

    if (renderMode == RenderMode.COMPASS || renderMode == RenderMode.NORMAL) {
      holders.add(new AnimatorListenerHolder(TrackAsiaAnimator.ANIMATOR_LAYER_ACCURACY, accuracyValueListener));
    }

    if (options.pulseEnabled()) {
      holders.add(new AnimatorListenerHolder(TrackAsiaAnimator.ANIMATOR_PULSING_CIRCLE,
          pulsingCircleRadiusListener));
    }
    return holders;
  }

  void cameraBearingUpdated(double bearing) {
    if (renderMode != RenderMode.GPS) {
      locationLayerRenderer.cameraBearingUpdated(bearing);
    }
  }

  void cameraTiltUpdated(double tilt) {
    locationLayerRenderer.cameraTiltUpdated(tilt);
  }

  void adjustPulsingCircleLayerVisibility(boolean visible) {
    locationLayerRenderer.adjustPulsingCircleLayerVisibility(visible);
  }
}
