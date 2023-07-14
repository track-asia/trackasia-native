package org.trackasia.android.location;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.view.animation.Interpolator;

import androidx.annotation.NonNull;

import java.util.List;

class trackasiaAnimatorSetProvider {
  private static trackasiaAnimatorSetProvider instance;

  private trackasiaAnimatorSetProvider() {
    // private constructor
  }

  static trackasiaAnimatorSetProvider getInstance() {
    if (instance == null) {
      instance = new trackasiaAnimatorSetProvider();
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
