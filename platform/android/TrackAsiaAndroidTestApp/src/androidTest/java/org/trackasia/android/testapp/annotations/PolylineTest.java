package org.trackasia.android.testapp.annotations;

import android.graphics.Color;

import org.trackasia.android.annotations.Polyline;
import org.trackasia.android.annotations.PolylineOptions;
import org.trackasia.android.geometry.LatLng;
import org.trackasia.android.testapp.activity.EspressoTest;

import org.junit.Ignore;
import org.junit.Test;

import static org.trackasia.android.testapp.action.TrackAsiaMapAction.invoke;
import static org.junit.Assert.assertEquals;

public class PolylineTest extends EspressoTest {

  @Test
  @Ignore
  public void addPolylineTest() {
    validateTestSetup();
    invoke(trackasiaMap, (uiController, mapboxMap) -> {
      LatLng latLngOne = new LatLng();
      LatLng latLngTwo = new LatLng(1, 0);

      assertEquals("Polygons should be empty", 0, mapboxMap.getPolygons().size());

      final PolylineOptions options = new PolylineOptions();
      options.color(Color.BLUE);
      options.add(latLngOne);
      options.add(latLngTwo);
      Polyline polyline = mapboxMap.addPolyline(options);

      assertEquals("Polylines should be 1", 1, mapboxMap.getPolylines().size());
      assertEquals("Polyline id should be 0", 0, polyline.getId());
      assertEquals("Polyline points size should match", 2, polyline.getPoints().size());
      assertEquals("Polyline stroke color should match", Color.BLUE, polyline.getColor());
      mapboxMap.clear();
      assertEquals("Polyline should be empty", 0, mapboxMap.getPolylines().size());
    });
  }
}
