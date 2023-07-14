package org.trackasia.android.utils;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.trackasia.android.constants.trackasiaConstants;
import org.trackasia.android.maps.MapFragment;
import org.trackasia.android.maps.SupportMapFragment;
import org.trackasia.android.maps.trackasiaMapOptions;

/**
 * MapFragment utility class.
 * <p>
 * Used to extract duplicate code between {@link MapFragment} and
 * {@link SupportMapFragment}.
 * </p>
 */
public class MapFragmentUtils {

  /**
   * Convert MapboxMapOptions to a bundle of fragment arguments.
   *
   * @param options The MapboxMapOptions to convert
   * @return a bundle of converted fragment arguments
   */
  @NonNull
  public static Bundle createFragmentArgs(trackasiaMapOptions options) {
    Bundle bundle = new Bundle();
    bundle.putParcelable(trackasiaConstants.FRAG_ARG_trackasiaMAPOPTIONS, options);
    return bundle;
  }

  /**
   * Convert a bundle of fragment arguments to MapboxMapOptions.
   *
   * @param context The context of the activity hosting the fragment
   * @param args    The fragment arguments
   * @return converted MapboxMapOptions
   */
  @Nullable
  public static trackasiaMapOptions resolveArgs(@NonNull Context context, @Nullable Bundle args) {
    trackasiaMapOptions options;
    if (args != null && args.containsKey(trackasiaConstants.FRAG_ARG_trackasiaMAPOPTIONS)) {
      options = args.getParcelable(trackasiaConstants.FRAG_ARG_trackasiaMAPOPTIONS);
    } else {
      // load default options
      options = trackasiaMapOptions.createFromAttributes(context);
    }
    return options;
  }
}
