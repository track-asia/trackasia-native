package com.trackasia.android.testapp.annotations;

import android.graphics.Color;

import com.trackasia.android.annotations.Polyline;
import com.trackasia.android.annotations.PolylineOptions;
import com.trackasia.android.geometry.LatLng;
import com.trackasia.android.testapp.activity.EspressoTest;

import org.junit.Ignore;
import org.junit.Test;

import static com.trackasia.android.testapp.action.TrackAsiaMapAction.invoke;
import static org.junit.Assert.assertEquals;

public class PolylineTest extends EspressoTest {

  @Test
  @Ignore
  public void addPolylineTest() {
    validateTestSetup();
    invoke(trackasiaMap, (uiController, trackasiaMap) -> {
      LatLng latLngOne = new LatLng();
      LatLng latLngTwo = new LatLng(1, 0);

      assertEquals("Polygons should be empty", 0, trackasiaMap.getPolygons().size());

      final PolylineOptions options = new PolylineOptions();
      options.color(Color.BLUE);
      options.add(latLngOne);
      options.add(latLngTwo);
      Polyline polyline = trackasiaMap.addPolyline(options);

      assertEquals("Polylines should be 1", 1, trackasiaMap.getPolylines().size());
      assertEquals("Polyline id should be 0", 0, polyline.getId());
      assertEquals("Polyline points size should match", 2, polyline.getPoints().size());
      assertEquals("Polyline stroke color should match", Color.BLUE, polyline.getColor());
      trackasiaMap.clear();
      assertEquals("Polyline should be empty", 0, trackasiaMap.getPolylines().size());
    });
  }
}
