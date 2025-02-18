package com.trackasia.android.testapp.maps.widgets;

import android.view.View;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;

import com.trackasia.android.maps.TrackAsiaMap;
import com.trackasia.android.testapp.activity.EspressoTest;

import org.hamcrest.Matcher;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withTagValue;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

public class LogoTest extends EspressoTest {

  @Test
  public void testDefault() {
    validateTestSetup();
    onView(withTagValue(is("logoView"))).check(matches(isDisplayed()));
  }

  @Test
  public void testDisabled() {
    validateTestSetup();

    onView(withTagValue(is("logoView")))
            .perform(new DisableAction(trackasiaMap))
            .check(matches(not(isDisplayed())));
  }

  private class DisableAction implements ViewAction {

    private TrackAsiaMap trackasiaMap;

    DisableAction(TrackAsiaMap map) {
      trackasiaMap = map;
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
      trackasiaMap.getUiSettings().setLogoEnabled(false);
    }
  }
}
