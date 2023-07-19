package com.trackasia.android.location;

import android.animation.ValueAnimator;
import android.view.animation.Interpolator;

import androidx.annotation.Nullable;

import com.trackasia.android.maps.TrackasiaMap;
import com.trackasia.android.geometry.LatLng;

final class TrackasiaAnimatorProvider {

  private static TrackasiaAnimatorProvider INSTANCE;

  private TrackasiaAnimatorProvider() {
    // private constructor
  }

  public static TrackasiaAnimatorProvider getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new TrackasiaAnimatorProvider();
    }
    return INSTANCE;
  }

  TrackasiaLatLngAnimator latLngAnimator(LatLng[] values, TrackasiaAnimator.AnimationsValueChangeListener updateListener,
                                        int maxAnimationFps) {
    return new TrackasiaLatLngAnimator(values, updateListener, maxAnimationFps);
  }

  TrackasiaFloatAnimator floatAnimator(Float[] values, TrackasiaAnimator.AnimationsValueChangeListener updateListener,
                                      int maxAnimationFps) {
    return new TrackasiaFloatAnimator(values, updateListener, maxAnimationFps);
  }

  TrackasiaCameraAnimatorAdapter cameraAnimator(Float[] values,
                                               TrackasiaAnimator.AnimationsValueChangeListener updateListener,
                                               @Nullable TrackasiaMap.CancelableCallback cancelableCallback) {
    return new TrackasiaCameraAnimatorAdapter(values, updateListener, cancelableCallback);
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
  PulsingLocationCircleAnimator pulsingCircleAnimator(TrackasiaAnimator.AnimationsValueChangeListener updateListener,
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
