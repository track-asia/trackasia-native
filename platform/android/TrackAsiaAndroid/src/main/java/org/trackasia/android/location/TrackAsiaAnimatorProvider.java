package org.trackasia.android.location;

import android.animation.ValueAnimator;
import android.view.animation.Interpolator;

import androidx.annotation.Nullable;

import org.trackasia.android.maps.TrackAsiaMap;
import org.trackasia.android.geometry.LatLng;

final class TrackAsiaAnimatorProvider {

  private static TrackAsiaAnimatorProvider INSTANCE;

  private TrackAsiaAnimatorProvider() {
    // private constructor
  }

  public static TrackAsiaAnimatorProvider getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new TrackAsiaAnimatorProvider();
    }
    return INSTANCE;
  }

  TrackAsiaLatLngAnimator latLngAnimator(LatLng[] values, TrackAsiaAnimator.AnimationsValueChangeListener updateListener,
                                        int maxAnimationFps) {
    return new TrackAsiaLatLngAnimator(values, updateListener, maxAnimationFps);
  }

  TrackAsiaFloatAnimator floatAnimator(Float[] values, TrackAsiaAnimator.AnimationsValueChangeListener updateListener,
                                      int maxAnimationFps) {
    return new TrackAsiaFloatAnimator(values, updateListener, maxAnimationFps);
  }

  TrackAsiaCameraAnimatorAdapter cameraAnimator(Float[] values,
                                               TrackAsiaAnimator.AnimationsValueChangeListener updateListener,
                                               @Nullable TrackAsiaMap.CancelableCallback cancelableCallback) {
    return new TrackAsiaCameraAnimatorAdapter(values, updateListener, cancelableCallback);
  }

  TrackAsiaPaddingAnimator paddingAnimator(double[][] values,
                                        TrackAsiaAnimator.AnimationsValueChangeListener<double[]> updateListener,
                                        @Nullable TrackAsiaMap.CancelableCallback cancelableCallback) {
    return new TrackAsiaPaddingAnimator(values, updateListener, cancelableCallback);
  }

  /**
   * This animator is for the LocationComponent pulsing circle.
   *
   * @param updateListener the listener that is found in the {@link LocationAnimatorCoordinator}'s
   *                       listener array.
   * @param maxAnimationFps the max frames per second of the pulsing animation
   * @param pulseSingleDuration the number of milliseconds it takes for the animator to create
   *                            a single pulse.
   * @param pulseMaxRadius the max radius when the circle is finished with a single pulse.
   * @param pulseInterpolator the type of Android-system interpolator to use for
   *                                       the pulsing animation (linear, accelerate, bounce, etc.)
   * @return a built {@link PulsingLocationCircleAnimator} object.
   */
  PulsingLocationCircleAnimator pulsingCircleAnimator(TrackAsiaAnimator.AnimationsValueChangeListener updateListener,
                                                      int maxAnimationFps,
                                                      float pulseSingleDuration,
                                                      float pulseMaxRadius,
                                                      Interpolator pulseInterpolator) {
    PulsingLocationCircleAnimator pulsingLocationCircleAnimator =
        new PulsingLocationCircleAnimator(updateListener, maxAnimationFps, pulseMaxRadius);
    pulsingLocationCircleAnimator.setDuration((long) pulseSingleDuration);
    pulsingLocationCircleAnimator.setRepeatMode(ValueAnimator.RESTART);
    pulsingLocationCircleAnimator.setRepeatCount(ValueAnimator.INFINITE);
    pulsingLocationCircleAnimator.setInterpolator(pulseInterpolator);
    return pulsingLocationCircleAnimator;
  }
}
