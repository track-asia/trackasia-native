package com.trackasia.android.testapp.action;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;

import com.trackasia.android.maps.MapLibreMap;

import org.hamcrest.Matcher;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

public class MapLibreMapAction implements ViewAction {

  private OnInvokeActionListener invokeViewAction;
  private MapLibreMap trackasiaMap;

  public MapLibreMapAction(OnInvokeActionListener invokeViewAction, MapLibreMap trackasiaMap) {
    this.invokeViewAction = invokeViewAction;
    this.trackasiaMap = trackasiaMap;
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
    invokeViewAction.onInvokeAction(uiController, trackasiaMap);
  }

  public static void invoke(MapLibreMap trackasiaMap, OnInvokeActionListener invokeViewAction) {
    onView(withId(android.R.id.content)).perform(new MapLibreMapAction(invokeViewAction, trackasiaMap));
  }

  public interface OnInvokeActionListener {
    void onInvokeAction(@NonNull UiController uiController, @NonNull MapLibreMap trackasiaMap);
  }
}


