package com.trackasia.android.testapp.activity.espresso;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.trackasia.android.maps.MapView;
import com.trackasia.android.maps.TrackasiaMap;
import com.trackasia.android.maps.OnMapReadyCallback;
import com.trackasia.android.maps.Style;
import com.trackasia.android.testapp.R;

public class DeviceIndependentTestActivity extends AppCompatActivity implements OnMapReadyCallback {

  public MapView mapView;
  protected TrackasiaMap mapboxMap;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_camera_test);

    // Initialize map as normal
    mapView = findViewById(R.id.mapView);
    mapView.onCreate(savedInstanceState);
    mapView.getMapAsync(this);
  }

  @Override
  public void onMapReady(@NonNull TrackasiaMap map) {
    mapboxMap = map;
    mapboxMap.setStyle("https://tiles.track-asia.com/tiles/v1/style-streets.json?key=public");
  }

  public TrackasiaMap getMapboxMap() {
    return mapboxMap;
  }

  @Override
  public void onResume() {
    super.onResume();
    mapView.onResume();
  }

  @Override
  protected void onStart() {
    super.onStart();
    mapView.onStart();
  }

  @Override
  public void onPause() {
    super.onPause();
    mapView.onPause();
  }

  @Override
  protected void onStop() {
    super.onStop();
    mapView.onStop();
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    mapView.onSaveInstanceState(outState);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    mapView.onDestroy();
  }

  @Override
  public void onLowMemory() {
    super.onLowMemory();
    mapView.onLowMemory();
  }
}
