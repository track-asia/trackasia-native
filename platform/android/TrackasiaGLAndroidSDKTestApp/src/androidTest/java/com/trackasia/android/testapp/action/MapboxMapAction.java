package com.trackasia.android.testapp.action;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;

import com.trackasia.android.maps.TrackasiaMap;

import org.hamcrest.Matcher;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

public class MapboxMapAction implements ViewAction {

  private OnInvokeActionListener invokeViewAction;
  private TrackasiaMap mapboxMap;

  public MapboxMapAction(OnInvokeActionListener invokeViewAction, TrackasiaMap mapboxMap) {
    this.invokeViewAction = invokeViewAction;
    this.mapboxMap = mapboxMap;
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
    invokeViewAction.onInvokeAction(uiController, mapboxMap);
  }

  public static void invoke(TrackasiaMap mapboxMap, OnInvokeActionListener invokeViewAction) {
    onView(withId(android.R.id.content)).perform(new MapboxMapAction(invokeViewAction, mapboxMap));
  }

  public interface OnInvokeActionListener {
    void onInvokeAction(@NonNull UiController uiController, @NonNull TrackasiaMap mapboxMap);
  }
}


