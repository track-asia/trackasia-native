package org.trackasia.android.testapp.geometry;

import androidx.test.annotation.UiThreadTest;

import com.google.gson.JsonArray;
import org.trackasia.geojson.Feature;
import org.trackasia.geojson.Point;
import org.trackasia.geojson.Polygon;
import org.trackasia.android.geometry.LatLng;
import org.trackasia.android.style.expressions.Expression;
import org.trackasia.android.style.layers.PropertyFactory;
import org.trackasia.android.style.layers.SymbolLayer;
import org.trackasia.android.style.sources.GeoJsonSource;
import org.trackasia.android.testapp.action.TrackAsiaMapAction;
import org.trackasia.android.testapp.activity.EspressoTest;
import org.trackasia.android.testapp.utils.TestingAsyncUtils;

import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static org.trackasia.geojson.Feature.fromGeometry;
import static org.trackasia.geojson.FeatureCollection.fromFeatures;
import static org.trackasia.geojson.GeometryCollection.fromGeometries;
import static org.trackasia.geojson.LineString.fromLngLats;
import static org.trackasia.geojson.MultiLineString.fromLineString;
import static org.trackasia.geojson.MultiPolygon.fromPolygon;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertFalse;

/**
 * Instrumentation test to validate java geojson conversion to c++
 */
public class GeoJsonConversionTest extends EspressoTest {

  // Regression test for #12343
  @Test
  @UiThreadTest
  public void testEmptyFeatureCollection() {
    validateTestSetup();
    trackasiaMap.getStyle().addSource(
      new GeoJsonSource("test-id",
        fromFeatures(singletonList(fromGeometry(fromGeometries(emptyList()))))
      )
    );
    trackasiaMap.getStyle().addLayer(new SymbolLayer("test-id", "test-id"));
  }

  @Test
  @UiThreadTest
  public void testPointFeatureCollection() {
    validateTestSetup();
    trackasiaMap.getStyle().addSource(
      new GeoJsonSource("test-id",
        fromFeatures(singletonList(fromGeometry(Point.fromLngLat(0.0, 0.0))))
      )
    );
    trackasiaMap.getStyle().addLayer(new SymbolLayer("test-id", "test-id"));
  }

  @Test
  @UiThreadTest
  public void testMultiPointFeatureCollection() {
    validateTestSetup();
    trackasiaMap.getStyle().addSource(
      new GeoJsonSource("test-id",
        fromFeatures(singletonList(fromGeometry(fromLngLats(emptyList()))))
      )
    );
    trackasiaMap.getStyle().addLayer(new SymbolLayer("test-id", "test-id"));
  }

  @Test
  @UiThreadTest
  public void testPolygonFeatureCollection() {
    validateTestSetup();
    trackasiaMap.getStyle().addSource(
      new GeoJsonSource("test-id",
        fromFeatures(singletonList(fromGeometry(Polygon.fromLngLats(emptyList()))))
      )
    );
    trackasiaMap.getStyle().addLayer(new SymbolLayer("test-id", "test-id"));
  }

  @Test
  @UiThreadTest
  public void testMultiPolygonFeatureCollection() {
    validateTestSetup();
    trackasiaMap.getStyle().addSource(
      new GeoJsonSource("test-id",
        fromFeatures(singletonList(fromGeometry(fromPolygon(Polygon.fromLngLats(emptyList())))))
      )
    );
    trackasiaMap.getStyle().addLayer(new SymbolLayer("test-id", "test-id"));
  }

  @Test
  @UiThreadTest
  public void testLineStringFeatureCollection() {
    validateTestSetup();
    trackasiaMap.getStyle().addSource(
      new GeoJsonSource("test-id",
        fromFeatures(singletonList(fromGeometry(fromLngLats(emptyList()))))
      )
    );
    trackasiaMap.getStyle().addLayer(new SymbolLayer("test-id", "test-id"));
  }

  @Test
  @UiThreadTest
  public void testMultiLineStringFeatureCollection() {
    validateTestSetup();
    trackasiaMap.getStyle().addSource(
      new GeoJsonSource("test-id",
        fromFeatures(singletonList(fromGeometry(fromLineString(fromLngLats(emptyList())))))
      )
    );
    trackasiaMap.getStyle().addLayer(new SymbolLayer("test-id", "test-id"));
  }


  @Test
  public void testNegativeNumberPropertyConversion() {
    validateTestSetup();
    onView(isRoot()).perform(new TrackAsiaMapAction((uiController, trackasiaMap) -> {
      LatLng latLng = new LatLng();
      Feature feature = Feature.fromGeometry(Point.fromLngLat(latLng.getLongitude(), latLng.getLatitude()));

      JsonArray foregroundJsonArray = new JsonArray();
      foregroundJsonArray.add(0f);
      foregroundJsonArray.add(-3f);
      feature.addProperty("property", foregroundJsonArray);

      GeoJsonSource source = new GeoJsonSource("source", feature);
      trackasiaMap.getStyle().addSource(source);

      SymbolLayer layer = new SymbolLayer("layer", "source")
        .withProperties(
          PropertyFactory.iconOffset(Expression.get("property")),
          PropertyFactory.iconImage("icon-zoo")
        );
      trackasiaMap.getStyle().addLayer(layer);

      TestingAsyncUtils.INSTANCE.waitForLayer(uiController, mapView);

      assertFalse(trackasiaMap.queryRenderedFeatures(trackasiaMap.getProjection().toScreenLocation(latLng)).isEmpty());
    }, trackasiaMap));
  }
}