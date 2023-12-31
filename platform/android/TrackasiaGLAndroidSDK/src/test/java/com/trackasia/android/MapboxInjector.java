package com.trackasia.android;

import android.content.Context;

import androidx.annotation.NonNull;

import com.trackasia.android.util.TileServerOptions;

import java.lang.reflect.Field;

public class MapboxInjector {

  private static final String FIELD_INSTANCE = "INSTANCE";

  public static void inject(@NonNull Context context, @NonNull String apiKey,
                            @NonNull TileServerOptions options) {
    Trackasia mapbox = new Trackasia(context, apiKey, options);
    try {
      Field instance = Trackasia.class.getDeclaredField(FIELD_INSTANCE);
      instance.setAccessible(true);
      instance.set(mapbox, mapbox);
    } catch (Exception exception) {
      throw new AssertionError();
    }
  }

  public static void clear() {
    try {
      Field field = Trackasia.class.getDeclaredField(FIELD_INSTANCE);
      field.setAccessible(true);
      field.set(field, null);
    } catch (Exception exception) {
      throw new AssertionError();
    }
  }
}
