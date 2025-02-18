package com.trackasia.android.testapp.activity;

import android.content.Context;

import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;

import com.trackasia.android.AppCenter;
import com.trackasia.android.TrackAsia;
import com.trackasia.android.maps.MapView;
import com.trackasia.android.maps.TrackAsiaMap;
import com.trackasia.android.testapp.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import timber.log.Timber;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;

/**
 * Base class for all Activity test hooking into an existing Activity that will load style.
 */
public abstract class BaseTest extends AppCenter {

  private static final int WAIT_TIMEOUT = 30; //seconds

  @Rule
  public ActivityTestRule rule = new ActivityTestRule<>(getActivityClass());

  @Rule
  public TestName testName = new TestName();

  @Rule
  public GrantPermissionRule grantLocationPermissionRule = GrantPermissionRule
          .grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

  protected TrackAsiaMap trackasiaMap;
  protected MapView mapView;
  private final CountDownLatch latch = new CountDownLatch(1);

  @Before
  @CallSuper
  public void beforeTest() {
    initializeMap();
    holdTestRunnerForStyleLoad();
  }

  @After
  @CallSuper
  public void afterTest() {
    super.afterTest();
  }

  @UiThread
  @CallSuper
  protected void initMap(TrackAsiaMap trackasiaMap) {
    this.trackasiaMap = trackasiaMap;
    trackasiaMap.getStyle(style -> latch.countDown());
  }

  protected void validateTestSetup() {
    if (!TrackAsia.isConnected()) {
      Timber.e("Not connected to the internet while running test");
    }
    assertNotNull("MapView isn't initialized", mapView);
    assertNotNull("TrackAsiaMap isn't initialized", trackasiaMap);
    assertNotNull("Style isn't initialized", trackasiaMap.getStyle());
    assertTrue("Style isn't fully loaded", trackasiaMap.getStyle().isFullyLoaded());
  }

  protected abstract Class getActivityClass();

  private void initializeMap() {
    try {
      rule.runOnUiThread(() -> {
        mapView = rule.getActivity().findViewById(R.id.mapView);
        if (mapView != null) {
          mapView.getMapAsync(this::initMap);
        } else {
          Timber.w("Skipping map load test since mapView is not found.");
          latch.countDown();
        }
      });
    } catch (Throwable throwable) {
      throwable.printStackTrace();
    }
  }

  private void holdTestRunnerForStyleLoad() {
    boolean interrupted;
    try {
      interrupted = latch.await(WAIT_TIMEOUT, TimeUnit.SECONDS);
    } catch (InterruptedException ignore) {
      interrupted = true;
    }

    if (!interrupted) {
      Timber.e("Timeout occurred for %s", testName.getMethodName());
      validateTestSetup();
    }
  }

  protected Context getContext() {
    return rule.getActivity();
  }

}
