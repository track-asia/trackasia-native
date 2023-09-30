package com.trackasia.android.maps;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import androidx.test.annotation.UiThreadTest;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import com.trackasia.android.AppCenter;
import com.trackasia.android.Trackasia;
import com.trackasia.android.exceptions.MapboxConfigurationException;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4ClassRunner.class)
public class MapboxTest extends AppCenter {

  private static final String API_KEY = "pk.0000000001";
  private static final String API_KEY_2 = "pk.0000000002";

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  private String realToken;

  @Before
  public void setup() {
    realToken = Trackasia.getApiKey();
  }

  @Test
  @UiThreadTest
  public void testConnected() {
    assertTrue(Trackasia.isConnected());

    // test manual connectivity
    Trackasia.setConnected(true);
    assertTrue(Trackasia.isConnected());
    Trackasia.setConnected(false);
    assertFalse(Trackasia.isConnected());

    // reset to Android connectivity
    Trackasia.setConnected(null);
    assertTrue(Trackasia.isConnected());
  }

  @Test
  @UiThreadTest
  public void setApiKey() {
    Trackasia.setApiKey(API_KEY);
    assertSame(API_KEY, Trackasia.getApiKey());
    Trackasia.setApiKey(API_KEY_2);
    assertSame(API_KEY_2, Trackasia.getApiKey());
  }

  @Test
  @UiThreadTest
  public void setNullApiKey() {
    expectedException.expect(MapboxConfigurationException.class);
    expectedException.expectMessage(
      "A valid API key is required, currently provided key is: " + null
    );

    Trackasia.setApiKey(null);
  }

  @After
  public void tearDown() {
    Trackasia.setApiKey(realToken);
  }
}