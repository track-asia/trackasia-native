package org.trackasia.android.location;

import android.animation.ValueAnimator;
import android.view.animation.Interpolator;

import androidx.annotation.Nullable;

import org.trackasia.android.maps.trackasiaMap;
import org.trackasia.android.geometry.LatLng;

final class trackasiaAnimatorProvider {

  private static trackasiaAnimatorProvider INSTANCE;

  private trackasiaAnimatorProvider() {
    // private constructor
  }

  public static trackasiaAnimatorProvider getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new trackasiaAnimatorProvider();
    }
    return INSTANCE;
  }

  trackasiaLatLngAnimator latLngAnimator(LatLng[] values, trackasiaAnimator.AnimationsValueChangeListener updateListener,
                                        int maxAnimationFps) {
    return new trackasiaLatLngAnimator(values, updateListener, maxAnimationFps);
  }

  trackasiaFloatAnimator floatAnimator(Float[] values, trackasiaAnimator.AnimationsValueChangeListener updateListener,
                                      int maxAnimationFps) {
    return new trackasiaFloatAnimator(values, updateListener, maxAnimationFps);
  }

  trackasiaCameraAnimatorAdapter cameraAnimator(Float[] values,
                                               trackasiaAnimator.AnimationsValueChangeListener updateListener,
                                               @Nullable trackasiaMap.CancelableCallback cancelableCallback) {
    return new trackasiaCameraAnimatorAdapter(values, updateListener, cancelableCallback);
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
  PulsingLocationCircleAnimator pulsingCircleAnimator(trackasiaAnimator.AnimationsValueChangeListener updateListener,
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
