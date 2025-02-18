package com.trackasia.android.testapp.style;

import android.graphics.Color;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import android.view.View;

import com.trackasia.android.style.light.Light;
import com.trackasia.android.style.expressions.Expression;
import com.trackasia.android.style.layers.FillExtrusionLayer;
import com.trackasia.android.style.layers.TransitionOptions;
import com.trackasia.android.style.light.Position;
import com.trackasia.android.testapp.R;
import com.trackasia.android.testapp.activity.BaseTest;
import com.trackasia.android.testapp.activity.style.FillExtrusionStyleTestActivity;

import timber.log.Timber;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static com.trackasia.android.style.expressions.Expression.eq;
import static com.trackasia.android.style.layers.Property.ANCHOR_MAP;
import static com.trackasia.android.style.layers.PropertyFactory.fillExtrusionBase;
import static com.trackasia.android.style.layers.PropertyFactory.fillExtrusionColor;
import static com.trackasia.android.style.layers.PropertyFactory.fillExtrusionHeight;
import static com.trackasia.android.style.layers.PropertyFactory.fillExtrusionOpacity;

import static com.trackasia.android.testapp.action.TrackAsiaMapAction.invoke;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4ClassRunner.class)
public class LightTest extends BaseTest {

  private Light light;

  @Test
  public void testAnchor() {
    validateTestSetup();
    setupLight();
    Timber.i("anchor");
    invoke(trackasiaMap, (uiController, trackasiaMap) -> {
      assertNotNull(light);
      // Set and Get
      light.setAnchor(ANCHOR_MAP);
      assertEquals("Anchor should match", ANCHOR_MAP, light.getAnchor());
    });
  }

  @Test
  public void testPositionTransition() {
    validateTestSetup();
    setupLight();
    Timber.i("positionTransitionOptions");
    invoke(trackasiaMap, (uiController, trackasiaMap) -> {
      assertNotNull(light);
      // Set and Get
      TransitionOptions options = new TransitionOptions(300, 100);
      light.setPositionTransition(options);
      assertEquals("Transition options should match", options, light.getPositionTransition());
    });
  }

  @Test
  public void testPosition() {
    validateTestSetup();
    setupLight();
    Timber.i("position");
    invoke(trackasiaMap,(uiController, trackasiaMap) -> {
      assertNotNull(light);
      // Set and Get
      Position position = new Position(1, 2, 3);
      light.setPosition(position);
      assertEquals("Position should match", position, light.getPosition());
    });
  }

  @Test
  public void testColorTransition() {
    validateTestSetup();
    setupLight();
    Timber.i("colorTransitionOptions");
    invoke(trackasiaMap, (uiController, trackasiaMap) -> {
      assertNotNull(light);
      // Set and Get
      TransitionOptions options = new TransitionOptions(300, 100);
      light.setColorTransition(options);
      assertEquals("Transition options should match", options, light.getColorTransition());
    });
  }

  @Test
  public void testColor() {
    validateTestSetup();
    setupLight();
    Timber.i("color");
    invoke(trackasiaMap, (uiController, trackasiaMap) -> {
      assertNotNull(light);
      // Set and Get
      light.setColor("rgba(255,128,0,0.7)");
      assertEquals("Color should match", "rgba(255,128,0,0.7)", light.getColor());
    });
  }

  @Test
  public void testIntensityTransition() {
    validateTestSetup();
    setupLight();
    Timber.i("intensityTransitionOptions");
    invoke(trackasiaMap, (uiController, trackasiaMap) -> {
      assertNotNull(light);
      // Set and Get
      TransitionOptions options = new TransitionOptions(300, 100);
      light.setIntensityTransition(options);
      assertEquals("Transition options should match", options, light.getIntensityTransition());
    });
  }

  @Test
  public void testIntensity() {
    validateTestSetup();
    setupLight();
    Timber.i("intensity");
    invoke(trackasiaMap, (uiController, trackasiaMap) -> {
      assertNotNull(light);
      // Set and Get
      light.setIntensity(0.3f);
      assertEquals("Intensity should match", 0.3f, light.getIntensity(), 0f);
    });
  }

  private void setupLight() {
    onView(withId(R.id.mapView)).perform(new ViewAction() {
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
        light = trackasiaMap.getStyle().getLight();
        FillExtrusionLayer fillExtrusionLayer = new FillExtrusionLayer("3d-buildings", "composite");
        fillExtrusionLayer.setSourceLayer("building");
        fillExtrusionLayer.setFilter(eq(Expression.get("extrude"), "true"));
        fillExtrusionLayer.setMinZoom(15);
        fillExtrusionLayer.setProperties(
          fillExtrusionColor(Color.LTGRAY),
          fillExtrusionHeight(Expression.get("height")),
          fillExtrusionBase(Expression.get("min_height")),
          fillExtrusionOpacity(0.6f)
        );
        trackasiaMap.getStyle().addLayer(fillExtrusionLayer);
      }
    });
  }

  @Override
  protected Class getActivityClass() {
    return FillExtrusionStyleTestActivity.class;
  }
}
