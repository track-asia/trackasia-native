package com.trackasia.android.testapp.activity.style;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.trackasia.android.maps.MapView;
import com.trackasia.android.maps.TrackasiaMap;
import com.trackasia.android.maps.OnMapReadyCallback;
import com.trackasia.android.maps.Style;
import com.trackasia.android.style.expressions.Expression;
import com.trackasia.android.style.layers.SymbolLayer;
import com.trackasia.android.style.sources.GeoJsonSource;
import com.trackasia.android.testapp.R;
import com.trackasia.android.utils.BitmapUtils;

import static com.trackasia.android.style.expressions.Expression.FormatOption.formatFontScale;
import static com.trackasia.android.style.expressions.Expression.FormatOption.formatTextColor;
import static com.trackasia.android.style.expressions.Expression.FormatOption.formatTextFont;
import static com.trackasia.android.style.expressions.Expression.formatEntry;
import static com.trackasia.android.style.expressions.Expression.image;
import static com.trackasia.android.style.expressions.Expression.literal;
import static com.trackasia.android.style.layers.PropertyFactory.textAllowOverlap;
import static com.trackasia.android.style.layers.PropertyFactory.textField;

/**
 * Test image in label.
 */
public class ImageInLabelActivity extends AppCompatActivity implements OnMapReadyCallback {
  private static final String ORIGINAL_SOURCE = "ORIGINAL_SOURCE";
  private static final String ORIGINAL_LAYER = "ORIGINAL_LAYER";
  private MapView mapView;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_stretchable_image);
    mapView = findViewById(R.id.mapView);
    mapView.onCreate(savedInstanceState);
    mapView.getMapAsync(this);
  }

  @Override
  public void onMapReady(@NonNull TrackasiaMap mapboxMap) {
    mapboxMap.setStyle("https://tiles.track-asia.com/tiles/v1/style-streets.json?key=public", style -> {
      Bitmap us = BitmapUtils.getBitmapFromDrawable(getResources().getDrawable(R.drawable.ic_us));
      Bitmap android = BitmapUtils.getBitmapFromDrawable(getResources().getDrawable(R.drawable.ic_android));

      style.addImage("us", us);
      style.addImage("android", android);

      Point point = Point.fromLngLat(-10, 0);
      Feature feature = Feature.fromGeometry(point);
      FeatureCollection originalCollection = FeatureCollection.fromFeature(feature);
      GeoJsonSource originalSource = new GeoJsonSource(ORIGINAL_SOURCE, originalCollection);
      SymbolLayer originalLayer = new SymbolLayer(ORIGINAL_LAYER, ORIGINAL_SOURCE)
        .withProperties(
          textAllowOverlap(true),
          textField(Expression
            .format(
              formatEntry(literal("Android: "),
                formatFontScale(1.0),
                formatTextColor(Color.BLUE),
                formatTextFont(new String[] {"Ubuntu Medium", "Arial Unicode MS Regular"})),
              formatEntry(image(literal("android"))),
              formatEntry(literal("Us: "),
                formatFontScale(1.5),
                formatTextColor(Color.YELLOW)),
              formatEntry(image(literal("us"))),
              formatEntry(literal("suffix"),
                formatFontScale(2.0),
                formatTextColor(Color.CYAN))
            )
          )

        );

      style.addSource(originalSource);
      style.addLayer(originalLayer);
    });
  }

  @Override
  protected void onStart() {
    super.onStart();
    mapView.onStart();
  }

  @Override
  protected void onResume() {
    super.onResume();
    mapView.onResume();
  }

  @Override
  protected void onPause() {
    super.onPause();
    mapView.onPause();
  }

  @Override
  protected void onStop() {
    super.onStop();
    mapView.onStop();
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    mapView.onSaveInstanceState(outState);
  }

  @Override
  public void onLowMemory() {
    super.onLowMemory();
    mapView.onLowMemory();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    mapView.onDestroy();
  }

}
