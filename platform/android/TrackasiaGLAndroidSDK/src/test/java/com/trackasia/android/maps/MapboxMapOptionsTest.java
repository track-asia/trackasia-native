package com.trackasia.android.maps;

import android.graphics.Color;
import android.view.Gravity;

import com.trackasia.android.camera.CameraPosition;
import com.trackasia.android.constants.TrackasiaConstants;
import com.trackasia.android.geometry.LatLng;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import java.util.Arrays;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class MapboxMapOptionsTest {

  private static final double DELTA = 1e-15;

  @Test
  public void testSanity() {
    assertNotNull("should not be null", new TrackasiaMapOptions());
  }

  @Test
  public void testDebugEnabled() {
    assertFalse(new TrackasiaMapOptions().getDebugActive());
    assertTrue(new TrackasiaMapOptions().debugActive(true).getDebugActive());
    assertFalse(new TrackasiaMapOptions().debugActive(false).getDebugActive());
  }

  @Test
  public void testCompassEnabled() {
    assertTrue(new TrackasiaMapOptions().compassEnabled(true).getCompassEnabled());
    assertFalse(new TrackasiaMapOptions().compassEnabled(false).getCompassEnabled());
  }

  @Test
  public void testCompassGravity() {
    assertEquals(Gravity.TOP | Gravity.END, new TrackasiaMapOptions().getCompassGravity());
    assertEquals(Gravity.BOTTOM, new TrackasiaMapOptions().compassGravity(Gravity.BOTTOM).getCompassGravity());
    assertNotEquals(Gravity.START, new TrackasiaMapOptions().compassGravity(Gravity.BOTTOM).getCompassGravity());
  }

  @Test
  public void testCompassMargins() {
    assertTrue(Arrays.equals(new int[] {0, 1, 2, 3}, new TrackasiaMapOptions().compassMargins(
      new int[] {0, 1, 2, 3}).getCompassMargins()));
    assertFalse(Arrays.equals(new int[] {0, 1, 2, 3}, new TrackasiaMapOptions().compassMargins(
      new int[] {0, 0, 0, 0}).getCompassMargins()));
  }

  @Test
  public void testLogoEnabled() {
    assertTrue(new TrackasiaMapOptions().logoEnabled(true).getLogoEnabled());
    assertFalse(new TrackasiaMapOptions().logoEnabled(false).getLogoEnabled());
  }

  @Test
  public void testLogoGravity() {
    assertEquals(Gravity.BOTTOM | Gravity.START, new TrackasiaMapOptions().getLogoGravity());
    assertEquals(Gravity.BOTTOM, new TrackasiaMapOptions().logoGravity(Gravity.BOTTOM).getLogoGravity());
    assertNotEquals(Gravity.START, new TrackasiaMapOptions().logoGravity(Gravity.BOTTOM).getLogoGravity());
  }

  @Test
  public void testLogoMargins() {
    assertTrue(Arrays.equals(new int[] {0, 1, 2, 3}, new TrackasiaMapOptions().logoMargins(
      new int[] {0, 1, 2, 3}).getLogoMargins()));
    assertFalse(Arrays.equals(new int[] {0, 1, 2, 3}, new TrackasiaMapOptions().logoMargins(
      new int[] {0, 0, 0, 0}).getLogoMargins()));
  }

  @Test
  public void testAttributionTintColor() {
    assertEquals(-1, new TrackasiaMapOptions().getAttributionTintColor());
    assertEquals(Color.RED, new TrackasiaMapOptions().attributionTintColor(Color.RED).getAttributionTintColor());
  }

  @Test
  public void testAttributionEnabled() {
    assertTrue(new TrackasiaMapOptions().attributionEnabled(true).getAttributionEnabled());
    assertFalse(new TrackasiaMapOptions().attributionEnabled(false).getAttributionEnabled());
  }

  @Test
  public void testAttributionGravity() {
    assertEquals(Gravity.BOTTOM | Gravity.START, new TrackasiaMapOptions().getAttributionGravity());
    assertEquals(Gravity.BOTTOM, new TrackasiaMapOptions().attributionGravity(Gravity.BOTTOM).getAttributionGravity());
    assertNotEquals(Gravity.START, new TrackasiaMapOptions().attributionGravity(Gravity.BOTTOM).getAttributionGravity());
  }

  @Test
  public void testAttributionMargins() {
    assertTrue(Arrays.equals(new int[] {0, 1, 2, 3}, new TrackasiaMapOptions().attributionMargins(
      new int[] {0, 1, 2, 3}).getAttributionMargins()));
    assertFalse(Arrays.equals(new int[] {0, 1, 2, 3}, new TrackasiaMapOptions().attributionMargins(
      new int[] {0, 0, 0, 0}).getAttributionMargins()));
  }

  @Test
  public void testMinZoom() {
    assertEquals(TrackasiaConstants.MINIMUM_ZOOM, new TrackasiaMapOptions().getMinZoomPreference(), DELTA);
    assertEquals(5.0f, new TrackasiaMapOptions().minZoomPreference(5.0f).getMinZoomPreference(), DELTA);
    assertNotEquals(2.0f, new TrackasiaMapOptions().minZoomPreference(5.0f).getMinZoomPreference(), DELTA);
  }

  @Test
  public void testMaxZoom() {
    assertEquals(TrackasiaConstants.MAXIMUM_ZOOM, new TrackasiaMapOptions().getMaxZoomPreference(), DELTA);
    assertEquals(5.0f, new TrackasiaMapOptions().maxZoomPreference(5.0f).getMaxZoomPreference(), DELTA);
    assertNotEquals(2.0f, new TrackasiaMapOptions().maxZoomPreference(5.0f).getMaxZoomPreference(), DELTA);
  }

  @Test
  public void testMinPitch() {
    assertEquals(TrackasiaConstants.MINIMUM_PITCH, new TrackasiaMapOptions().getMinPitchPreference(), DELTA);
    assertEquals(5.0f, new TrackasiaMapOptions().minPitchPreference(5.0f).getMinPitchPreference(), DELTA);
    assertNotEquals(2.0f, new TrackasiaMapOptions().minPitchPreference(5.0f).getMinPitchPreference(), DELTA);
  }

  @Test
  public void testMaxPitch() {
    assertEquals(TrackasiaConstants.MAXIMUM_PITCH, new TrackasiaMapOptions().getMaxPitchPreference(), DELTA);
    assertEquals(5.0f, new TrackasiaMapOptions().maxPitchPreference(5.0f).getMaxPitchPreference(), DELTA);
    assertNotEquals(2.0f, new TrackasiaMapOptions().maxPitchPreference(5.0f).getMaxPitchPreference(), DELTA);
  }

  @Test
  public void testTiltGesturesEnabled() {
    assertTrue(new TrackasiaMapOptions().getTiltGesturesEnabled());
    assertTrue(new TrackasiaMapOptions().tiltGesturesEnabled(true).getTiltGesturesEnabled());
    assertFalse(new TrackasiaMapOptions().tiltGesturesEnabled(false).getTiltGesturesEnabled());
  }

  @Test
  public void testScrollGesturesEnabled() {
    assertTrue(new TrackasiaMapOptions().getScrollGesturesEnabled());
    assertTrue(new TrackasiaMapOptions().scrollGesturesEnabled(true).getScrollGesturesEnabled());
    assertFalse(new TrackasiaMapOptions().scrollGesturesEnabled(false).getScrollGesturesEnabled());
  }

  @Test
  public void testHorizontalScrollGesturesEnabled() {
    assertTrue(new TrackasiaMapOptions().getHorizontalScrollGesturesEnabled());
    assertTrue(new TrackasiaMapOptions().horizontalScrollGesturesEnabled(true).getHorizontalScrollGesturesEnabled());
    assertFalse(new TrackasiaMapOptions().horizontalScrollGesturesEnabled(false).getHorizontalScrollGesturesEnabled());
  }

  @Test
  public void testZoomGesturesEnabled() {
    assertTrue(new TrackasiaMapOptions().getZoomGesturesEnabled());
    assertTrue(new TrackasiaMapOptions().zoomGesturesEnabled(true).getZoomGesturesEnabled());
    assertFalse(new TrackasiaMapOptions().zoomGesturesEnabled(false).getZoomGesturesEnabled());
  }

  @Test
  public void testRotateGesturesEnabled() {
    assertTrue(new TrackasiaMapOptions().getRotateGesturesEnabled());
    assertTrue(new TrackasiaMapOptions().rotateGesturesEnabled(true).getRotateGesturesEnabled());
    assertFalse(new TrackasiaMapOptions().rotateGesturesEnabled(false).getRotateGesturesEnabled());
  }

  @Test
  public void testCamera() {
    CameraPosition position = new CameraPosition.Builder().build();
    assertEquals(new CameraPosition.Builder(position).build(), new TrackasiaMapOptions().camera(position).getCamera());
    assertNotEquals(new CameraPosition.Builder().target(new LatLng(1, 1)), new TrackasiaMapOptions().camera(position));
    assertNull(new TrackasiaMapOptions().getCamera());
  }

  @Test
  public void testPrefetchesTiles() {
    // Default value
    assertTrue(new TrackasiaMapOptions().getPrefetchesTiles());

    // Check mutations
    assertTrue(new TrackasiaMapOptions().setPrefetchesTiles(true).getPrefetchesTiles());
    assertFalse(new TrackasiaMapOptions().setPrefetchesTiles(false).getPrefetchesTiles());
  }

  @Test
  public void testPrefetchZoomDelta() {
    // Default value
    assertEquals(4, new TrackasiaMapOptions().getPrefetchZoomDelta());

    // Check mutations
    assertEquals(5, new TrackasiaMapOptions().setPrefetchZoomDelta(5).getPrefetchZoomDelta());
  }


  @Test
  public void testCrossSourceCollisions() {
    // Default value
    assertTrue(new TrackasiaMapOptions().getCrossSourceCollisions());

    // check mutations
    assertTrue(new TrackasiaMapOptions().crossSourceCollisions(true).getCrossSourceCollisions());
    assertFalse(new TrackasiaMapOptions().crossSourceCollisions(false).getCrossSourceCollisions());
  }

  @Test
  public void testLocalIdeographFontFamily_enabledByDefault() {
    TrackasiaMapOptions options = TrackasiaMapOptions.createFromAttributes(RuntimeEnvironment.application, null);
    assertEquals(TrackasiaConstants.DEFAULT_FONT, options.getLocalIdeographFontFamily());
  }
}

