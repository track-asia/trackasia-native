package com.trackasia.android.utils;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.trackasia.android.constants.TrackasiaConstants;
import com.trackasia.android.maps.MapFragment;
import com.trackasia.android.maps.SupportMapFragment;
import com.trackasia.android.maps.TrackasiaMapOptions;

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
  public static Bundle createFragmentArgs(TrackasiaMapOptions options) {
    Bundle bundle = new Bundle();
    bundle.putParcelable(TrackasiaConstants.FRAG_ARG_TrackasiaMapOPTIONS, options);
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
  public static TrackasiaMapOptions resolveArgs(@NonNull Context context, @Nullable Bundle args) {
    TrackasiaMapOptions options;
    if (args != null && args.containsKey(TrackasiaConstants.FRAG_ARG_TrackasiaMapOPTIONS)) {
      options = args.getParcelable(TrackasiaConstants.FRAG_ARG_TrackasiaMapOPTIONS);
    } else {
      // load default options
      options = TrackasiaMapOptions.createFromAttributes(context);
    }
    return options;
  }
}
