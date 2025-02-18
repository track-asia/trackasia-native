package com.trackasia.android.testapp.camera;

import androidx.annotation.NonNull;
import androidx.test.annotation.UiThreadTest;

import com.trackasia.geojson.Point;
import com.trackasia.geojson.Polygon;
import com.trackasia.android.camera.CameraPosition;
import com.trackasia.android.geometry.LatLng;
import com.trackasia.android.geometry.LatLngBounds;
import com.trackasia.android.testapp.activity.BaseTest;
import com.trackasia.android.testapp.activity.espresso.DeviceIndependentTestActivity;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CameraForTest extends BaseTest {

  @Test
  @UiThreadTest
  public void testGetCameraForLatLngBounds() {
    validateTestSetup();
    CameraPosition actualPosition = trackasiaMap.getCameraForLatLngBounds(
      LatLngBounds.from(10, 10, -10, -10));
    CameraPosition expectedPosition = new CameraPosition.Builder()
      .target(new LatLng()).zoom(4.16).tilt(0).bearing(0).build();
    assertEquals("Latitude should match",
      expectedPosition.target.getLatitude(), actualPosition.target.getLatitude(), 0.00001f);
    assertEquals("Longitude should match",
      expectedPosition.target.getLongitude(), actualPosition.target.getLongitude(), 0.00001f);
    assertEquals("Bearing should match",
      expectedPosition.zoom, actualPosition.zoom, 0.01f);
    assertEquals("Tilt should match", expectedPosition.tilt, actualPosition.tilt, 0.01f);
  }

  @Test
  @UiThreadTest
  public void testGetCameraForLatLngBoundsPadding() {
    validateTestSetup();
    CameraPosition actualPosition = trackasiaMap.getCameraForLatLngBounds(
      LatLngBounds.from(10, 10, -10, -10), new int[] {5, 5, 5, 5});
    CameraPosition expectedPosition = new CameraPosition.Builder()
      .target(new LatLng()).zoom(4.13).tilt(0).bearing(0).build();
    assertEquals("Latitude should match",
      expectedPosition.target.getLatitude(), actualPosition.target.getLatitude(), 0.00001f);
    assertEquals("Longitude should match",
      expectedPosition.target.getLongitude(), actualPosition.target.getLongitude(), 0.00001f);
    assertEquals("Zoom should match",
      expectedPosition.zoom, actualPosition.zoom, 0.01f);
    assertEquals("Tilt should match",
      expectedPosition.tilt, actualPosition.tilt, 0.01f);
    assertEquals("Bearing should match",
      expectedPosition.bearing, actualPosition.bearing, 0.01f);
  }

  @Test
  @UiThreadTest
  public void testGetCameraForLatLngBoundsBearing() {
    validateTestSetup();
    CameraPosition actualPosition = trackasiaMap.getCameraForLatLngBounds(
      LatLngBounds.from(10, 10, -10, -10), 45, 0);
    CameraPosition expectedPosition = new CameraPosition.Builder()
      .target(new LatLng()).zoom(3.66).tilt(0).bearing(45).build();
    assertEquals("Latitude should match",
      expectedPosition.target.getLatitude(), actualPosition.target.getLatitude(), 0.00001f);
    assertEquals("Longitude should match",
      expectedPosition.target.getLongitude(), actualPosition.target.getLongitude(), 0.00001f);
    assertEquals("Zoom should match",
      expectedPosition.zoom, actualPosition.zoom, 0.01f);
    assertEquals("Tilt should match",
      expectedPosition.tilt, actualPosition.tilt, 0.01f);
    assertEquals("Bearing should match",
      expectedPosition.bearing, actualPosition.bearing, 0.01f);
  }

  @Test
  @UiThreadTest
  public void testGetCameraForLatLngBoundsTilt() {
    validateTestSetup();
    CameraPosition actualPosition = trackasiaMap.getCameraForLatLngBounds(
      LatLngBounds.from(10, 10, -10, -10), 0, 45);
    CameraPosition expectedPosition = new CameraPosition.Builder()
      .target(new LatLng(-0.264576975267, 0)).zoom(4.13).tilt(45).bearing(0).build();
    assertEquals("Latitude should match",
      expectedPosition.target.getLatitude(), actualPosition.target.getLatitude(), 0.00001f);
    assertEquals("Longitude should match",
      expectedPosition.target.getLongitude(), actualPosition.target.getLongitude(), 0.00001f);
    assertEquals("Zoom should match",
      expectedPosition.zoom, actualPosition.zoom, 0.01f);
    assertEquals("Tilt should match",
      expectedPosition.tilt, actualPosition.tilt, 0.01f);
    assertEquals("Bearing should match",
      expectedPosition.bearing, actualPosition.bearing, 0.01f);
  }

  @Test
  @UiThreadTest
  public void testGetCameraForLatLngBoundsAll() {
    validateTestSetup();
    CameraPosition actualPosition = trackasiaMap.getCameraForLatLngBounds(
      LatLngBounds.from(10, 10, -10, -10), new int[] {5, 5, 5, 5}, 45, 45);
    CameraPosition expectedPosition = new CameraPosition.Builder()
      .target(new LatLng(-0.3732134634, -0.3713191053)).zoom(3.63).tilt(45).bearing(45).build();
    assertEquals("Latitude should match",
      expectedPosition.target.getLatitude(), actualPosition.target.getLatitude(), 0.00001f);
    assertEquals("Longitude should match",
      expectedPosition.target.getLongitude(), actualPosition.target.getLongitude(), 0.00001f);
    assertEquals("Zoom should match",
      expectedPosition.zoom, actualPosition.zoom, 0.01f);
    assertEquals("Tilt should match",
      expectedPosition.tilt, actualPosition.tilt, 0.01f);
    assertEquals("Bearing should match",
      expectedPosition.bearing, actualPosition.bearing, 0.01f);
  }

  @Test
  @UiThreadTest
  public void testGetCameraForGeometry() {
    validateTestSetup();
    List<List<Point>> polygonDefinition = getPolygonDefinition();
    CameraPosition actualPosition = trackasiaMap.getCameraForGeometry(Polygon.fromLngLats(polygonDefinition));
    CameraPosition expectedPosition = new CameraPosition.Builder()
      .target(new LatLng()).zoom(4.16).tilt(0).bearing(0).build();
    assertEquals("Latitude should match",
      expectedPosition.target.getLatitude(), actualPosition.target.getLatitude(), 0.00001f);
    assertEquals("Longitude should match",
      expectedPosition.target.getLongitude(), actualPosition.target.getLongitude(), 0.00001f);
    assertEquals("Bearing should match",
      expectedPosition.zoom, actualPosition.zoom, 0.01f);
    assertEquals("Tilt should match", expectedPosition.tilt, actualPosition.tilt, 0.01f);
  }

  @NonNull
  private List<List<Point>> getPolygonDefinition() {
    return new ArrayList<List<Point>>() {
      {
        add(new ArrayList<Point>() {
          {
            add(Point.fromLngLat(10, 10));
            add(Point.fromLngLat(-10, 10));
            add(Point.fromLngLat(-10, -10));
            add(Point.fromLngLat(10, -10));
          }
        });
      }
    };
  }

  @Test
  @UiThreadTest
  public void testGetCameraForGeometryPadding() {
    validateTestSetup();
    List<List<Point>> polygonDefinition = getPolygonDefinition();
    CameraPosition actualPosition = trackasiaMap.getCameraForGeometry(Polygon.fromLngLats(polygonDefinition),
      new int[] {5, 5, 5, 5});
    CameraPosition expectedPosition = new CameraPosition.Builder()
      .target(new LatLng()).zoom(4.13).tilt(0).bearing(0).build();
    assertEquals("Latitude should match",
      expectedPosition.target.getLatitude(), actualPosition.target.getLatitude(), 0.00001f);
    assertEquals("Longitude should match",
      expectedPosition.target.getLongitude(), actualPosition.target.getLongitude(), 0.00001f);
    assertEquals("Zoom should match",
      expectedPosition.zoom, actualPosition.zoom, 0.01f);
    assertEquals("Tilt should match",
      expectedPosition.tilt, actualPosition.tilt, 0.01f);
    assertEquals("Bearing should match",
      expectedPosition.bearing, actualPosition.bearing, 0.01f);
  }

  @Test
  @UiThreadTest
  public void testGetCameraForGeometryBearing() {
    validateTestSetup();
    List<List<Point>> polygonDefinition = getPolygonDefinition();
    CameraPosition actualPosition = trackasiaMap.getCameraForGeometry(Polygon.fromLngLats(polygonDefinition), 45, 0);
    CameraPosition expectedPosition = new CameraPosition.Builder()
      .target(new LatLng()).zoom(3.66).tilt(0).bearing(45).build();
    assertEquals("Latitude should match",
      expectedPosition.target.getLatitude(), actualPosition.target.getLatitude(), 0.00001f);
    assertEquals("Longitude should match",
      expectedPosition.target.getLongitude(), actualPosition.target.getLongitude(), 0.00001f);
    assertEquals("Zoom should match",
      expectedPosition.zoom, actualPosition.zoom, 0.01f);
    assertEquals("Tilt should match",
      expectedPosition.tilt, actualPosition.tilt, 0.01f);
    assertEquals("Bearing should match",
      expectedPosition.bearing, actualPosition.bearing, 0.01f);
  }

  @Test
  @UiThreadTest
  public void testGetCameraForGeometryTilt() {
    validateTestSetup();
    List<List<Point>> polygonDefinition = getPolygonDefinition();
    CameraPosition actualPosition = trackasiaMap.getCameraForGeometry(Polygon.fromLngLats(polygonDefinition), 0, 45);
    CameraPosition expectedPosition = new CameraPosition.Builder()
      .target(new LatLng(-0.2645769752, 0)).zoom(4.13).tilt(45).bearing(0).build();
    assertEquals("Latitude should match",
      expectedPosition.target.getLatitude(), actualPosition.target.getLatitude(), 0.00001f);
    assertEquals("Longitude should match",
      expectedPosition.target.getLongitude(), actualPosition.target.getLongitude(), 0.00001f);
    assertEquals("Zoom should match",
      expectedPosition.zoom, actualPosition.zoom, 0.01f);
    assertEquals("Tilt should match",
      expectedPosition.tilt, actualPosition.tilt, 0.01f);
    assertEquals("Bearing should match",
      expectedPosition.bearing, actualPosition.bearing, 0.01f);
  }

  @Test
  @UiThreadTest
  public void testGetCameraForGeometryAll() {
    validateTestSetup();
    List<List<Point>> polygonDefinition = getPolygonDefinition();
    CameraPosition actualPosition = trackasiaMap.getCameraForGeometry(Polygon.fromLngLats(polygonDefinition),
      new int[] {5, 5, 5, 5}, 45, 45);
    CameraPosition expectedPosition = new CameraPosition.Builder()
      .target(new LatLng(-0.373213463, -0.37131910534)).zoom(3.63).tilt(45).bearing(45).build();
    assertEquals("Latitude should match",
      expectedPosition.target.getLatitude(), actualPosition.target.getLatitude(), 0.00001f);
    assertEquals("Longitude should match",
      expectedPosition.target.getLongitude(), actualPosition.target.getLongitude(), 0.00001f);
    assertEquals("Zoom should match",
      expectedPosition.zoom, actualPosition.zoom, 0.01f);
    assertEquals("Tilt should match",
      expectedPosition.tilt, actualPosition.tilt, 0.01f);
    assertEquals("Bearing should match",
      expectedPosition.bearing, actualPosition.bearing, 0.01f);
  }

  @Test
  @UiThreadTest
  public void testGetCameraForGeometryDeprecatedApi() {
    validateTestSetup();
    List<List<Point>> polygonDefinition = getPolygonDefinition();
    CameraPosition actualPosition = trackasiaMap.getCameraForGeometry(
      Polygon.fromLngLats(polygonDefinition),
      new int[] {5, 5, 5, 5},
      45, 0);
    CameraPosition expectedPosition = new CameraPosition.Builder()
      .target(new LatLng()).zoom(3.63).tilt(0).bearing(45).build();
    assertEquals("Latitude should match",
      expectedPosition.target.getLatitude(), actualPosition.target.getLatitude(), 0.00001f);
    assertEquals("Longitude should match",
      expectedPosition.target.getLongitude(), actualPosition.target.getLongitude(), 0.00001f);
    assertEquals("Zoom should match",
      expectedPosition.zoom, actualPosition.zoom, 0.01f);
    assertEquals("Tilt should match",
      expectedPosition.tilt, actualPosition.tilt, 0.01f);
    assertEquals("Bearing should match",
      expectedPosition.bearing, actualPosition.bearing, 0.01f);
  }

  @Override
  protected Class getActivityClass() {
    return DeviceIndependentTestActivity.class;
  }
}
