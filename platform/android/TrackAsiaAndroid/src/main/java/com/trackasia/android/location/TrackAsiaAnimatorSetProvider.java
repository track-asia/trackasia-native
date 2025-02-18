package com.trackasia.android.location;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.view.animation.Interpolator;

import androidx.annotation.NonNull;

import java.util.List;

class TrackAsiaAnimatorSetProvider {
  private static TrackAsiaAnimatorSetProvider instance;

  private TrackAsiaAnimatorSetProvider() {
    // private constructor
  }

  static TrackAsiaAnimatorSetProvider getInstance() {
    if (instance == null) {
      instance = new TrackAsiaAnimatorSetProvider();
    }
    return instance;
  }

  void startAnimation(@NonNull List<Animator> animators, @NonNull Interpolator interpolator,
                      long duration) {
    AnimatorSet locationAnimatorSet = new AnimatorSet();
    locationAnimatorSet.playTogether(animators);
    locationAnimatorSet.setInterpolator(interpolator);
    locationAnimatorSet.setDuration(duration);
    locationAnimatorSet.start();
  }
}
