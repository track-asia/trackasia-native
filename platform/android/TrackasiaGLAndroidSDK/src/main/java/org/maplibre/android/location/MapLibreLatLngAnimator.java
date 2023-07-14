package org.trackasia.android.location;

import android.animation.TypeEvaluator;

import androidx.annotation.NonNull;

import org.trackasia.android.geometry.LatLng;

class trackasiaLatLngAnimator extends trackasiaAnimator<LatLng> {

  trackasiaLatLngAnimator(@NonNull LatLng[] values, @NonNull AnimationsValueChangeListener updateListener,
                         int maxAnimationFps) {
    super(values, updateListener, maxAnimationFps);
  }

  @NonNull
  @Override
  TypeEvaluator provideEvaluator() {
    return new LatLngEvaluator();
  }
}
