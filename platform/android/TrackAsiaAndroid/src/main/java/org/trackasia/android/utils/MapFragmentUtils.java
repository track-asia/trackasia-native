package org.trackasia.android.utils;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.trackasia.android.constants.TrackAsiaConstants;
import org.trackasia.android.maps.MapFragment;
import org.trackasia.android.maps.SupportMapFragment;
import org.trackasia.android.maps.TrackAsiaMapOptions;

/**
 * MapFragment utility class.
 * <p>
 * Used to extract duplicate code between {@link MapFragment} and
 * {@link SupportMapFragment}.
 * </p>
 */
public class MapFragmentUtils {

  /**
   * Convert TrackAsiaMapOptions to a bundle of fragment arguments.
   *
   * @param options The TrackAsiaMapOptions to convert
   * @return a bundle of converted fragment arguments
   */
  @NonNull
  public static Bundle createFragmentArgs(TrackAsiaMapOptions options) {
    Bundle bundle = new Bundle();
    bundle.putParcelable(TrackAsiaConstants.FRAG_ARG_TRACKASIAMAPOPTIONS, options);
    return bundle;
  }

  /**
   * Convert a bundle of fragment arguments to TrackAsiaMapOptions.
   *
   * @param context The context of the activity hosting the fragment
   * @param args    The fragment arguments
   * @return converted TrackAsiaMapOptions
   */
  @Nullable
  public static TrackAsiaMapOptions resolveArgs(@NonNull Context context, @Nullable Bundle args) {
    TrackAsiaMapOptions options;
    if (args != null && args.containsKey(TrackAsiaConstants.FRAG_ARG_TRACKASIAMAPOPTIONS)) {
      options = args.getParcelable(TrackAsiaConstants.FRAG_ARG_TRACKASIAMAPOPTIONS);
    } else {
      // load default options
      options = TrackAsiaMapOptions.createFromAttributes(context);
    }
    return options;
  }
}
