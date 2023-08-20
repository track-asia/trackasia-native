package com.trackasia.android.utils;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.trackasia.android.constants.TrackasiaConstants;
import com.trackasia.android.maps.TrackasiaMapOptions;

/**
 * MapFragment utility class.
 * <p>
 * Used to extract duplicate code between {@link com.trackasia.android.maps.MapFragment} and
 * {@link com.trackasia.android.maps.SupportMapFragment}.
 * </p>
 */
public class MapFragmentUtils {

  /**
   * Convert TrackasiaMapOptions to a bundle of fragment arguments.
   *
   * @param options The TrackasiaMapOptions to convert
   * @return a bundle of converted fragment arguments
   */
  @NonNull
  public static Bundle createFragmentArgs(TrackasiaMapOptions options) {
    Bundle bundle = new Bundle();
    bundle.putParcelable(TrackasiaConstants.FRAG_ARG_MAPBOXMAPOPTIONS, options);
    return bundle;
  }

  /**
   * Convert a bundle of fragment arguments to TrackasiaMapOptions.
   *
   * @param context The context of the activity hosting the fragment
   * @param args    The fragment arguments
   * @return converted TrackasiaMapOptions
   */
  @Nullable
  public static TrackasiaMapOptions resolveArgs(@NonNull Context context, @Nullable Bundle args) {
    TrackasiaMapOptions options;
    if (args != null && args.containsKey(TrackasiaConstants.FRAG_ARG_MAPBOXMAPOPTIONS)) {
      options = args.getParcelable(TrackasiaConstants.FRAG_ARG_MAPBOXMAPOPTIONS);
    } else {
      // load default options
      options = TrackasiaMapOptions.createFromAttributes(context);
    }
    return options;
  }
}
