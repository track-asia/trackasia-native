package com.trackasia.android.location;

import android.animation.TypeEvaluator;

import androidx.annotation.NonNull;

import com.trackasia.android.geometry.LatLng;

class TrackasiaLatLngAnimator extends TrackasiaAnimator<LatLng> {

  TrackasiaLatLngAnimator(@NonNull LatLng[] values, @NonNull AnimationsValueChangeListener updateListener,
                         int maxAnimationFps) {
    super(values, updateListener, maxAnimationFps);
  }

  @NonNull
  @Override
  TypeEvaluator provideEvaluator() {
    return new LatLngEvaluator();
  }
}
