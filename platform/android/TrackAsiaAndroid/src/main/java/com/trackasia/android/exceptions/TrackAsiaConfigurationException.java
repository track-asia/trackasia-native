package com.trackasia.android.exceptions;

import com.trackasia.android.TrackAsia;
import com.trackasia.android.WellKnownTileServer;

import android.content.Context;

import androidx.annotation.NonNull;

/**
 * A MapboxConfigurationException is thrown by TrackAsiaMap when the SDK hasn't been properly initialised.
 * <p>
 * This occurs either when {@link TrackAsia} is not correctly initialised or the provided apiKey
 * through {@link TrackAsia#getInstance(Context, String, WellKnownTileServer)} isn't valid.
 * </p>
 *
 * @see TrackAsia#getInstance(Context, String,  WellKnownTileServer)
 */
public class TrackAsiaConfigurationException extends RuntimeException {

  /**
   * Creates a TrackAsia configuration exception thrown by TrackAsiaMap when the SDK hasn't been properly initialised.
   */
  public TrackAsiaConfigurationException() {
    super("\nUsing MapView requires calling TrackAsia.getInstance(Context context, String apiKey, "
            + "WellKnownTileServer wellKnownTileServer) before inflating or creating the view.");
  }

  /**
   * Creates a TrackAsia configuration exception thrown by TrackAsiaMap when the SDK hasn't been properly initialised.
   */
  public TrackAsiaConfigurationException(@NonNull String message) {
    super(message);
  }
}
