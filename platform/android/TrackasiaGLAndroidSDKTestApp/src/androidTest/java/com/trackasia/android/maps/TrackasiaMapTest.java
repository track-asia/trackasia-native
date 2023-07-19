package com.trackasia.android.maps;

import android.graphics.Color;
import android.view.View;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;

import com.trackasia.android.annotations.BaseMarkerOptions;
import com.trackasia.android.annotations.Marker;
import com.trackasia.android.annotations.MarkerOptions;
import com.trackasia.android.annotations.Polygon;
import com.trackasia.android.annotations.PolygonOptions;
import com.trackasia.android.annotations.Polyline;
import com.trackasia.android.annotations.PolylineOptions;
import com.trackasia.android.exceptions.InvalidMarkerPositionException;
import com.trackasia.android.geometry.LatLng;
import com.trackasia.android.testapp.R;
import com.trackasia.android.testapp.activity.EspressoTest;

import org.hamcrest.Matcher;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * This test is responsible for testing the public API.
 * <p>
 * Methods executed on MapboxMap are called from a ViewAction to ensure correct synchronisation
 * with the application UI-thread.
 * </p>
 * @deprecated remove this file when removing deprecated annotations
 */
@Deprecated
public class TrackasiaMapTest extends EspressoTest {

  //
  // InfoWindow
  //

  @Test
  public void testConcurrentInfoWindowEnabled() {
    validateTestSetup();
    onView(withId(R.id.mapView)).perform(new TrackasiaMapAction((uiController, view) -> {
      trackasiaMap.setAllowConcurrentMultipleOpenInfoWindows(true);
      assertTrue("ConcurrentWindows should be true", trackasiaMap.isAllowConcurrentMultipleOpenInfoWindows());
    }));
  }

  @Test
  public void testConcurrentInfoWindowDisabled() {
    validateTestSetup();
    onView(withId(R.id.mapView)).perform(new TrackasiaMapAction((uiController, view) -> {
      trackasiaMap.setAllowConcurrentMultipleOpenInfoWindows(false);
      assertFalse("ConcurrentWindows should be false", trackasiaMap.isAllowConcurrentMultipleOpenInfoWindows());
    }));
  }

  @Test
  public void testInfoWindowAdapter() {
    validateTestSetup();
    onView(withId(R.id.mapView)).perform(new TrackasiaMapAction((uiController, view) -> {
      TrackasiaMap.InfoWindowAdapter infoWindowAdapter = marker -> null;
      trackasiaMap.setInfoWindowAdapter(infoWindowAdapter);
      assertEquals("InfoWindowAdpter should be the same", infoWindowAdapter, trackasiaMap.getInfoWindowAdapter());
    }));
  }

  //
  // Annotations
  //

  @Test
  public void testAddMarker() {
    validateTestSetup();
    onView(withId(R.id.mapView)).perform(new TrackasiaMapAction((uiController, view) -> {
      MarkerOptions markerOptions = new MarkerOptions().position(new LatLng());
      Marker marker = trackasiaMap.addMarker(markerOptions);
      assertTrue("Marker should be contained", trackasiaMap.getMarkers().contains(marker));
    }));
  }

  @Test(expected = InvalidMarkerPositionException.class)
  public void testAddMarkerInvalidPosition() {
    new MarkerOptions().getMarker();
  }

  @Test
  public void testAddMarkers() {
    validateTestSetup();
    onView(withId(R.id.mapView)).perform(new TrackasiaMapAction((uiController, view) -> {
      List<BaseMarkerOptions> markerList = new ArrayList<>();
      MarkerOptions markerOptions1 = new MarkerOptions().position(new LatLng()).title("a");
      MarkerOptions markerOptions2 = new MarkerOptions().position(new LatLng()).title("b");
      markerList.add(markerOptions1);
      markerList.add(markerOptions2);
      List<Marker> markers = trackasiaMap.addMarkers(markerList);
      assertEquals("Markers size should be 2", 2, trackasiaMap.getMarkers().size());
      assertTrue(trackasiaMap.getMarkers().contains(markers.get(0)));
      assertTrue(trackasiaMap.getMarkers().contains(markers.get(1)));
    }));
  }

  @Test
  public void testAddMarkersEmpty() {
    validateTestSetup();
    onView(withId(R.id.mapView)).perform(new TrackasiaMapAction((uiController, view) -> {
      List<BaseMarkerOptions> markerList = new ArrayList<>();
      trackasiaMap.addMarkers(markerList);
      assertEquals("Markers size should be 0", 0, trackasiaMap.getMarkers().size());
    }));
  }

  @Test
  public void testAddMarkersSingleMarker() {
    validateTestSetup();
    onView(withId(R.id.mapView)).perform(new TrackasiaMapAction((uiController, view) -> {
      List<BaseMarkerOptions> markerList = new ArrayList<>();
      MarkerOptions markerOptions = new MarkerOptions().title("a").position(new LatLng());
      markerList.add(markerOptions);
      List<Marker> markers = trackasiaMap.addMarkers(markerList);
      assertEquals("Markers size should be 1", 1, trackasiaMap.getMarkers().size());
      assertTrue(trackasiaMap.getMarkers().contains(markers.get(0)));
    }));
  }

  @Test
  public void testAddPolygon() {
    validateTestSetup();
    onView(withId(R.id.mapView)).perform(new TrackasiaMapAction((uiController, view) -> {
      PolygonOptions polygonOptions = new PolygonOptions().add(new LatLng());
      Polygon polygon = trackasiaMap.addPolygon(polygonOptions);
      assertTrue("Polygon should be contained", trackasiaMap.getPolygons().contains(polygon));
    }));
  }

  @Test
  public void testAddEmptyPolygon() {
    validateTestSetup();
    onView(withId(R.id.mapView)).perform(new TrackasiaMapAction((uiController, view) -> {
      PolygonOptions polygonOptions = new PolygonOptions();
      Polygon polygon = trackasiaMap.addPolygon(polygonOptions);
      assertTrue("Polygon should be ignored", !trackasiaMap.getPolygons().contains(polygon));
    }));
  }

  @Test
  public void testAddPolygons() {
    validateTestSetup();
    onView(withId(R.id.mapView)).perform(new TrackasiaMapAction((uiController, view) -> {
      List<PolygonOptions> polygonList = new ArrayList<>();
      PolygonOptions polygonOptions1 = new PolygonOptions().fillColor(Color.BLACK).add(new LatLng());
      PolygonOptions polygonOptions2 = new PolygonOptions().fillColor(Color.WHITE).add(new LatLng());
      PolygonOptions polygonOptions3 = new PolygonOptions();
      polygonList.add(polygonOptions1);
      polygonList.add(polygonOptions2);
      polygonList.add(polygonOptions3);
      trackasiaMap.addPolygons(polygonList);
      assertEquals("Polygons size should be 2", 2, trackasiaMap.getPolygons().size());
      assertTrue(trackasiaMap.getPolygons().contains(polygonOptions1.getPolygon()));
      assertTrue(trackasiaMap.getPolygons().contains(polygonOptions2.getPolygon()));
      assertTrue("Polygon should be ignored", !trackasiaMap.getPolygons().contains(polygonOptions3.getPolygon()));
    }));
  }

  @Test
  public void addPolygonsEmpty() {
    validateTestSetup();
    onView(withId(R.id.mapView)).perform(new TrackasiaMapAction((uiController, view) -> {
      trackasiaMap.addPolygons(new ArrayList<PolygonOptions>());
      assertEquals("Polygons size should be 0", 0, trackasiaMap.getPolygons().size());
    }));
  }

  @Test
  public void addPolygonsSingle() {
    validateTestSetup();
    onView(withId(R.id.mapView)).perform(new TrackasiaMapAction((uiController, view) -> {
      List<PolygonOptions> polygonList = new ArrayList<>();
      PolygonOptions polygonOptions = new PolygonOptions().fillColor(Color.BLACK).add(new LatLng());
      polygonList.add(polygonOptions);
      trackasiaMap.addPolygons(polygonList);
      assertEquals("Polygons size should be 1", 1, trackasiaMap.getPolygons().size());
      assertTrue(trackasiaMap.getPolygons().contains(polygonOptions.getPolygon()));
    }));
  }

  @Test
  public void testAddPolyline() {
    validateTestSetup();
    onView(withId(R.id.mapView)).perform(new TrackasiaMapAction((uiController, view) -> {
      PolylineOptions polylineOptions = new PolylineOptions().add(new LatLng());
      Polyline polyline = trackasiaMap.addPolyline(polylineOptions);
      assertTrue("Polyline should be contained", trackasiaMap.getPolylines().contains(polyline));
    }));
  }

  @Test
  public void testAddEmptyPolyline() {
    validateTestSetup();
    onView(withId(R.id.mapView)).perform(new TrackasiaMapAction((uiController, view) -> {
      PolylineOptions polylineOptions = new PolylineOptions();
      Polyline polyline = trackasiaMap.addPolyline(polylineOptions);
      assertTrue("Polyline should be ignored", !trackasiaMap.getPolylines().contains(polyline));
    }));
  }

  @Test
  public void testAddPolylines() {
    validateTestSetup();
    onView(withId(R.id.mapView)).perform(new TrackasiaMapAction((uiController, view) -> {
      List<PolylineOptions> polylineList = new ArrayList<>();
      PolylineOptions polygonOptions1 = new PolylineOptions().color(Color.BLACK).add(new LatLng());
      PolylineOptions polygonOptions2 = new PolylineOptions().color(Color.WHITE).add(new LatLng());
      PolylineOptions polygonOptions3 = new PolylineOptions();
      polylineList.add(polygonOptions1);
      polylineList.add(polygonOptions2);
      polylineList.add(polygonOptions3);
      trackasiaMap.addPolylines(polylineList);
      assertEquals("Polygons size should be 2", 2, trackasiaMap.getPolylines().size());
      assertTrue(trackasiaMap.getPolylines().contains(polygonOptions1.getPolyline()));
      assertTrue(trackasiaMap.getPolylines().contains(polygonOptions2.getPolyline()));
      assertTrue(
        "Polyline should be ignored", !trackasiaMap.getPolylines().contains(polygonOptions3.getPolyline())
      );
    }));
  }

  @Test
  public void testAddPolylinesEmpty() {
    validateTestSetup();
    onView(withId(R.id.mapView)).perform(new TrackasiaMapAction((uiController, view) -> {
      trackasiaMap.addPolylines(new ArrayList<PolylineOptions>());
      assertEquals("Polygons size should be 0", 0, trackasiaMap.getPolylines().size());
    }));
  }

  @Test
  public void testAddPolylinesSingle() {
    validateTestSetup();
    onView(withId(R.id.mapView)).perform(new TrackasiaMapAction((uiController, view) -> {
      List<PolylineOptions> polylineList = new ArrayList<>();
      PolylineOptions polygonOptions = new PolylineOptions().color(Color.BLACK).add(new LatLng());
      polylineList.add(polygonOptions);
      trackasiaMap.addPolylines(polylineList);
      assertEquals("Polygons size should be 1", 1, trackasiaMap.getPolylines().size());
      assertTrue(trackasiaMap.getPolylines().contains(polygonOptions.getPolyline()));
    }));
  }

  @Test
  public void testRemoveMarker() {
    validateTestSetup();
    onView(withId(R.id.mapView)).perform(new TrackasiaMapAction((uiController, view) -> {
      MarkerOptions markerOptions = new MarkerOptions().position(new LatLng());
      Marker marker = trackasiaMap.addMarker(markerOptions);
      trackasiaMap.removeMarker(marker);
      assertTrue("Markers should be empty", trackasiaMap.getMarkers().isEmpty());
    }));
  }

  @Test
  public void testRemovePolygon() {
    validateTestSetup();
    onView(withId(R.id.mapView)).perform(new TrackasiaMapAction((uiController, view) -> {
      PolygonOptions polygonOptions = new PolygonOptions();
      Polygon polygon = trackasiaMap.addPolygon(polygonOptions);
      trackasiaMap.removePolygon(polygon);
      assertTrue("Polygons should be empty", trackasiaMap.getPolylines().isEmpty());
    }));
  }

  @Test
  public void testRemovePolyline() {
    validateTestSetup();
    onView(withId(R.id.mapView)).perform(new TrackasiaMapAction((uiController, view) -> {
      PolylineOptions polylineOptions = new PolylineOptions();
      Polyline polyline = trackasiaMap.addPolyline(polylineOptions);
      trackasiaMap.removePolyline(polyline);
      assertTrue("Polylines should be empty", trackasiaMap.getPolylines().isEmpty());
    }));
  }

  @Test
  public void testRemoveAnnotation() {
    validateTestSetup();
    onView(withId(R.id.mapView)).perform(new TrackasiaMapAction((uiController, view) -> {
      MarkerOptions markerOptions = new MarkerOptions().position(new LatLng());
      Marker marker = trackasiaMap.addMarker(markerOptions);
      trackasiaMap.removeAnnotation(marker);
      assertTrue("Annotations should be empty", trackasiaMap.getAnnotations().isEmpty());
    }));
  }

  @Test
  public void testRemoveAnnotationById() {
    validateTestSetup();
    onView(withId(R.id.mapView)).perform(new TrackasiaMapAction((uiController, view) -> {
      MarkerOptions markerOptions = new MarkerOptions().position(new LatLng());
      trackasiaMap.addMarker(markerOptions);
      // id will always be 0 in unit tests
      trackasiaMap.removeAnnotation(0);
      assertTrue("Annotations should be empty", trackasiaMap.getAnnotations().isEmpty());
    }));
  }

  @Test
  public void testRemoveAnnotations() {
    validateTestSetup();
    onView(withId(R.id.mapView)).perform(new TrackasiaMapAction((uiController, view) -> {
      List<BaseMarkerOptions> markerList = new ArrayList<>();
      MarkerOptions markerOptions1 = new MarkerOptions().title("a").position(new LatLng());
      MarkerOptions markerOptions2 = new MarkerOptions().title("b").position(new LatLng());
      markerList.add(markerOptions1);
      markerList.add(markerOptions2);
      trackasiaMap.addMarkers(markerList);
      trackasiaMap.removeAnnotations();
      assertTrue("Annotations should be empty", trackasiaMap.getAnnotations().isEmpty());
    }));
  }

  @Test
  public void testClear() {
    validateTestSetup();
    onView(withId(R.id.mapView)).perform(new TrackasiaMapAction((uiController, view) -> {
      List<BaseMarkerOptions> markerList = new ArrayList<>();
      MarkerOptions markerOptions1 = new MarkerOptions().title("a").position(new LatLng());
      MarkerOptions markerOptions2 = new MarkerOptions().title("b").position(new LatLng());
      markerList.add(markerOptions1);
      markerList.add(markerOptions2);
      trackasiaMap.addMarkers(markerList);
      trackasiaMap.clear();
      assertTrue("Annotations should be empty", trackasiaMap.getAnnotations().isEmpty());
    }));
  }

  @Test
  public void testRemoveAnnotationsByList() {
    validateTestSetup();
    onView(withId(R.id.mapView)).perform(new TrackasiaMapAction((uiController, view) -> {
      List<BaseMarkerOptions> markerList = new ArrayList<>();
      MarkerOptions markerOptions1 = new MarkerOptions().title("a").position(new LatLng());
      MarkerOptions markerOptions2 = new MarkerOptions().title("b").position(new LatLng());
      markerList.add(markerOptions1);
      markerList.add(markerOptions2);
      List<Marker> markers = trackasiaMap.addMarkers(markerList);
      Marker marker = trackasiaMap.addMarker(new MarkerOptions().position(new LatLng()).title("c"));
      trackasiaMap.removeAnnotations(markers);
      assertTrue("Annotations should not be empty", trackasiaMap.getAnnotations().size() == 1);
      assertTrue("Marker should be contained", trackasiaMap.getAnnotations().contains(marker));
    }));
  }

  @Test
  public void testGetAnnotationById() {
    validateTestSetup();
    onView(withId(R.id.mapView)).perform(new TrackasiaMapAction((uiController, view) -> {
      MarkerOptions markerOptions = new MarkerOptions().position(new LatLng());
      Marker initialMarker = trackasiaMap.addMarker(markerOptions);
      Marker retrievedMarker = (Marker) trackasiaMap.getAnnotation(0);
      assertEquals("Markers should match", initialMarker, retrievedMarker);
    }));
  }

  @Test
  public void testGetAnnotations() {
    validateTestSetup();
    onView(withId(R.id.mapView)).perform(
      new TrackasiaMapAction((uiController, view) ->
        assertNotNull("Annotations should be non null", trackasiaMap.getAnnotations()))
    );
  }

  @Test
  public void testGetMarkers() {
    validateTestSetup();
    onView(withId(R.id.mapView)).perform(
      new TrackasiaMapAction((uiController, view) ->
        assertNotNull("Markers should be non null", trackasiaMap.getMarkers()))
    );
  }

  @Test
  public void testGetPolygons() {
    validateTestSetup();
    onView(withId(R.id.mapView)).perform(new TrackasiaMapAction((uiController, view) ->
      assertNotNull("Polygons should be non null", trackasiaMap.getPolygons()))
    );
  }

  @Test
  public void testGetPolylines() {
    validateTestSetup();
    onView(withId(R.id.mapView)).perform(new TrackasiaMapAction((uiController, view) ->
      assertNotNull("Polylines should be non null", trackasiaMap.getPolylines()))
    );
  }

  @Test
  public void testGetSelectedMarkers() {
    validateTestSetup();
    onView(withId(R.id.mapView)).perform(new TrackasiaMapAction((uiController, view) ->
      assertNotNull("Selected markers should be non null", trackasiaMap.getSelectedMarkers()))
    );
  }

  @Test
  public void testSelectMarker() {
    validateTestSetup();
    onView(withId(R.id.mapView)).perform(new TrackasiaMapAction((uiController, view) -> {
      MarkerOptions markerOptions = new MarkerOptions().position(new LatLng());
      Marker marker = trackasiaMap.addMarker(markerOptions);
      trackasiaMap.selectMarker(marker);
      assertTrue("Marker should be contained", trackasiaMap.getSelectedMarkers().contains(marker));
    }));
  }

  @Test
  public void testDeselectMarker() {
    validateTestSetup();
    onView(withId(R.id.mapView)).perform(new TrackasiaMapAction((uiController, view) -> {
      MarkerOptions markerOptions = new MarkerOptions().position(new LatLng());
      Marker marker = trackasiaMap.addMarker(markerOptions);
      trackasiaMap.selectMarker(marker);
      trackasiaMap.deselectMarker(marker);
      assertTrue("Selected markers should be empty", trackasiaMap.getSelectedMarkers().isEmpty());
    }));
  }

  @Test
  public void testDeselectMarkers() {
    validateTestSetup();
    onView(withId(R.id.mapView)).perform(new TrackasiaMapAction((uiController, view) -> {
      MarkerOptions markerOptions = new MarkerOptions().position(new LatLng());
      Marker marker1 = trackasiaMap.addMarker(markerOptions);
      Marker marker2 = trackasiaMap.addMarker(markerOptions);
      trackasiaMap.selectMarker(marker1);
      trackasiaMap.selectMarker(marker2);
      trackasiaMap.deselectMarkers();
      assertTrue("Selected markers should be empty", trackasiaMap.getSelectedMarkers().isEmpty());
    }));
  }

  public class TrackasiaMapAction implements ViewAction {

    private InvokeViewAction invokeViewAction;

    TrackasiaMapAction(InvokeViewAction invokeViewAction) {
      this.invokeViewAction = invokeViewAction;
    }

    @Override
    public Matcher<View> getConstraints() {
      return isDisplayed();
    }

    @Override
    public String getDescription() {
      return getClass().getSimpleName();
    }

    @Override
    public void perform(UiController uiController, View view) {
      invokeViewAction.onViewAction(uiController, view);
    }
  }

  interface InvokeViewAction {
    void onViewAction(UiController uiController, View view);
  }
}