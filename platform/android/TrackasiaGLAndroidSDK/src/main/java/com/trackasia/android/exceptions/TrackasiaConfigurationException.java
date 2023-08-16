package com.trackasia.android.exceptions;

import com.trackasia.android.WellKnownTileServer;

import android.content.Context;

import androidx.annotation.NonNull;

/**
 * A TrackasiaConfigurationException is thrown by MapboxMap when the SDK hasn't been properly initialised.
 * <p>
 * This occurs either when {@link com.trackasia.android.Trackasia} is not correctly initialised or the provided apiKey
 * through {@link com.trackasia.android.Trackasia#getInstance(Context, String, WellKnownTileServer)} isn't valid.
 * </p>
 *
 * @see com.trackasia.android.Trackasia#getInstance(Context, String,  WellKnownTileServer)
 */
public class TrackasiaConfigurationException extends RuntimeException {

  /**
   * Creates a Mapbox configuration exception thrown by MapboxMap when the SDK hasn't been properly initialised.
   */
  public TrackasiaConfigurationException() {
    super("\nUsing MapView requires calling Trackasia.getInstance(Context context, String apiKey, "
            + "WellKnownTileServer wellKnownTileServer) before inflating or creating the view.");
  }

  /**
   * Creates a Mapbox configuration exception thrown by MapboxMap when the SDK hasn't been properly initialised.
   */
  public TrackasiaConfigurationException(@NonNull String message) {
    super(message);
  }
}
