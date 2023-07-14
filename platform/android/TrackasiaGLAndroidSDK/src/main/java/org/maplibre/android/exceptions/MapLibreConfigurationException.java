package org.trackasia.android.exceptions;

import org.trackasia.android.trackasia;
import org.trackasia.android.WellKnownTileServer;

import android.content.Context;

import androidx.annotation.NonNull;

/**
 * A MapboxConfigurationException is thrown by trackasiaMap when the SDK hasn't been properly initialised.
 * <p>
 * This occurs either when {@link trackasia} is not correctly initialised or the provided apiKey
 * through {@link trackasia#getInstance(Context, String, WellKnownTileServer)} isn't valid.
 * </p>
 *
 * @see trackasia#getInstance(Context, String,  WellKnownTileServer)
 */
public class trackasiaConfigurationException extends RuntimeException {

  /**
   * Creates a trackasia configuration exception thrown by MapboxMap when the SDK hasn't been properly initialised.
   */
  public trackasiaConfigurationException() {
    super("\nUsing MapView requires calling trackasia.getInstance(Context context, String apiKey, "
            + "WellKnownTileServer wellKnownTileServer) before inflating or creating the view.");
  }

  /**
   * Creates a trackasia configuration exception thrown by MapboxMap when the SDK hasn't been properly initialised.
   */
  public trackasiaConfigurationException(@NonNull String message) {
    super(message);
  }
}
