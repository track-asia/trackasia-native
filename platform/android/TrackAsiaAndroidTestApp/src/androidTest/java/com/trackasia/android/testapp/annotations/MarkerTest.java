package com.trackasia.android.testapp.annotations;

import com.trackasia.android.annotations.Marker;
import com.trackasia.android.annotations.MarkerOptions;
import com.trackasia.android.geometry.LatLng;
import com.trackasia.android.testapp.action.TrackAsiaMapAction;
import com.trackasia.android.testapp.activity.EspressoTest;
import com.trackasia.android.testapp.utils.TestConstants;

import org.junit.Ignore;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.trackasia.android.testapp.action.TrackAsiaMapAction.invoke;
import static org.junit.Assert.assertEquals;

public class MarkerTest extends EspressoTest {

  private Marker marker;

  @Test
  @Ignore
  public void addMarkerTest() {
    validateTestSetup();
    TrackAsiaMapAction.invoke(trackasiaMap, (uiController, trackasiaMap) -> {
      assertEquals("Markers should be empty", 0, trackasiaMap.getMarkers().size());

      MarkerOptions options = new MarkerOptions();
      options.setPosition(new LatLng());
      options.setSnippet(TestConstants.TEXT_MARKER_SNIPPET);
      options.setTitle(TestConstants.TEXT_MARKER_TITLE);
      marker = trackasiaMap.addMarker(options);

      assertEquals("Markers size should be 1, ", 1, trackasiaMap.getMarkers().size());
      assertEquals("Marker id should be 0", 0, marker.getId());
      assertEquals("Marker target should match", new LatLng(), marker.getPosition());
      assertEquals("Marker snippet should match", TestConstants.TEXT_MARKER_SNIPPET, marker.getSnippet());
      assertEquals("Marker target should match", TestConstants.TEXT_MARKER_TITLE, marker.getTitle());
      trackasiaMap.clear();
      assertEquals("Markers should be empty", 0, trackasiaMap.getMarkers().size());
    });
  }

  @Test
  @Ignore
  public void showInfoWindowTest() {
    validateTestSetup();
    invoke(trackasiaMap, (uiController, trackasiaMap) -> {
      final MarkerOptions options = new MarkerOptions();
      options.setPosition(new LatLng());
      options.setSnippet(TestConstants.TEXT_MARKER_SNIPPET);
      options.setTitle(TestConstants.TEXT_MARKER_TITLE);
      marker = trackasiaMap.addMarker(options);
      trackasiaMap.selectMarker(marker);
    });
    onView(withText(TestConstants.TEXT_MARKER_TITLE)).check(matches(isDisplayed()));
    onView(withText(TestConstants.TEXT_MARKER_SNIPPET)).check(matches(isDisplayed()));
  }

}
