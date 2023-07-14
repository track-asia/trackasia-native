package org.trackasia.android.maps;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import androidx.test.annotation.UiThreadTest;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import org.trackasia.android.AppCenter;
import org.trackasia.android.trackasia;
import org.trackasia.android.exceptions.trackasiaConfigurationException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4ClassRunner.class)
public class trackasiaTest extends AppCenter {

  private static final String API_KEY = "pk.0000000001";
  private static final String API_KEY_2 = "pk.0000000002";

  private String realToken;

  @Before
  public void setup() {
    realToken = trackasia.getApiKey();
  }

  @Test
  @UiThreadTest
  public void testConnected() {
    assertTrue(trackasia.isConnected());

    // test manual connectivity
    trackasia.setConnected(true);
    assertTrue(trackasia.isConnected());
    trackasia.setConnected(false);
    assertFalse(trackasia.isConnected());

    // reset to Android connectivity
    trackasia.setConnected(null);
    assertTrue(trackasia.isConnected());
  }

  @Test
  @UiThreadTest
  public void setApiKey() {
    trackasia.setApiKey(API_KEY);
    assertSame(API_KEY, trackasia.getApiKey());
    trackasia.setApiKey(API_KEY_2);
    assertSame(API_KEY_2, trackasia.getApiKey());
  }

  @Test
  @UiThreadTest
  public void setNullApiKey() {
    assertThrows(trackasiaConfigurationException.class, () -> trackasia.setApiKey(null));
  }

  @After
  public void tearDown() {
    trackasia.setApiKey(realToken);
  }
}