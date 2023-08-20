package com.trackasia.android.testapp.utils;

import android.content.Context;
import android.widget.TextView;

import com.trackasia.android.camera.CameraPosition;
import com.trackasia.android.maps.TrackasiaMap;
import com.trackasia.android.testapp.R;

public class IdleZoomListener implements TrackasiaMap.OnCameraIdleListener {

  private TrackasiaMap mapboxMap;
  private TextView textView;

  public IdleZoomListener(TrackasiaMap mapboxMap, TextView textView) {
    this.mapboxMap = mapboxMap;
    this.textView = textView;
  }

  @Override
  public void onCameraIdle() {
    Context context = textView.getContext();
    CameraPosition position = mapboxMap.getCameraPosition();
    textView.setText(String.format(context.getString(R.string.debug_zoom), position.zoom));
  }
}