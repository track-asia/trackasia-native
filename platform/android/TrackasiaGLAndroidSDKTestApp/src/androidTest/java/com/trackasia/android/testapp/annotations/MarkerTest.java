package com.trackasia.android.testapp.annotations;

import com.trackasia.android.annotations.Marker;
import com.trackasia.android.annotations.MarkerOptions;
import com.trackasia.android.geometry.LatLng;
import com.trackasia.android.testapp.action.MapboxMapAction;
import com.trackasia.android.testapp.activity.EspressoTest;
import com.trackasia.android.testapp.utils.TestConstants;

import org.junit.Ignore;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.trackasia.android.testapp.action.MapboxMapAction.invoke;
import static org.junit.Assert.assertEquals;

public class MarkerTest extends EspressoTest {

  private Marker marker;

  @Test
  @Ignore
  public void addMarkerTest() {
    validateTestSetup();
    MapboxMapAction.invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertEquals("Markers should be empty", 0, mapboxMap.getMarkers().size());

      MarkerOptions options = new MarkerOptions();
      options.setPosition(new LatLng());
      options.setSnippet(TestConstants.TEXT_MARKER_SNIPPET);
      options.setTitle(TestConstants.TEXT_MARKER_TITLE);
      marker = mapboxMap.addMarker(options);

      assertEquals("Markers size should be 1, ", 1, mapboxMap.getMarkers().size());
      assertEquals("Marker id should be 0", 0, marker.getId());
      assertEquals("Marker target should match", new LatLng(), marker.getPosition());
      assertEquals("Marker snippet should match", TestConstants.TEXT_MARKER_SNIPPET, marker.getSnippet());
      assertEquals("Marker target should match", TestConstants.TEXT_MARKER_TITLE, marker.getTitle());
      mapboxMap.clear();
      assertEquals("Markers should be empty", 0, mapboxMap.getMarkers().size());
    });
  }

  @Test
  @Ignore
  public void showInfoWindowTest() {
    validateTestSetup();
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      final MarkerOptions options = new MarkerOptions();
      options.setPosition(new LatLng());
      options.setSnippet(TestConstants.TEXT_MARKER_SNIPPET);
      options.setTitle(TestConstants.TEXT_MARKER_TITLE);
      marker = mapboxMap.addMarker(options);
      mapboxMap.selectMarker(marker);
    });
    onView(withText(TestConstants.TEXT_MARKER_TITLE)).check(matches(isDisplayed()));
    onView(withText(TestConstants.TEXT_MARKER_SNIPPET)).check(matches(isDisplayed()));
  }

}
