package com.trackasia.android.exceptions;

import com.trackasia.android.WellKnownTileServer;

import android.content.Context;

import androidx.annotation.NonNull;

/**
 * A MapboxConfigurationException is thrown by MapboxMap when the SDK hasn't been properly initialised.
 * <p>
 * This occurs either when {@link com.trackasia.android.Mapbox} is not correctly initialised or the provided apiKey
 * through {@link com.trackasia.android.Mapbox#getInstance(Context, String, WellKnownTileServer)} isn't valid.
 * </p>
 *
 * @see com.trackasia.android.Mapbox#getInstance(Context, String,  WellKnownTileServer)
 */
public class MapboxConfigurationException extends RuntimeException {

  /**
   * Creates a Mapbox configuration exception thrown by MapboxMap when the SDK hasn't been properly initialised.
   */
  public MapboxConfigurationException() {
    super("\nUsing MapView requires calling Mapbox.getInstance(Context context, String apiKey, "
            + "WellKnownTileServer wellKnownTileServer) before inflating or creating the view.");
  }

  /**
   * Creates a Mapbox configuration exception thrown by MapboxMap when the SDK hasn't been properly initialised.
   */
  public MapboxConfigurationException(@NonNull String message) {
    super(message);
  }
}
