package com.trackasia.android.testapp.activity;

import androidx.annotation.UiThread;

import com.trackasia.android.maps.TrackasiaMap;
import com.trackasia.android.maps.Style;
import com.trackasia.android.testapp.activity.espresso.EspressoTestActivity;


/**
 * Base class for all tests using EspressoTestActivity as wrapper.
 * <p>
 * Loads "assets/streets.json" as style.
 * </p>
 */
public class EspressoTest extends BaseTest {

  @Override
  protected final Class getActivityClass() {
    return EspressoTestActivity.class;
  }

  @UiThread
  @Override
  protected void initMap(TrackasiaMap mapboxMap) {
    mapboxMap.setStyle("https://tiles.track-asia.com/tiles/v1/style-streets.json?key=public");
    super.initMap(mapboxMap);
  }
}
