package com.trackasia.android.location;

class AnimatorListenerHolder {
  @TrackasiaAnimator.Type
  private final int animatorType;
  private final TrackasiaAnimator.AnimationsValueChangeListener listener;

  AnimatorListenerHolder(@TrackasiaAnimator.Type int animatorType,
                         TrackasiaAnimator.AnimationsValueChangeListener listener) {
    this.animatorType = animatorType;
    this.listener = listener;
  }

  @TrackasiaAnimator.Type
  public int getAnimatorType() {
    return animatorType;
  }

  public TrackasiaAnimator.AnimationsValueChangeListener getListener() {
    return listener;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    AnimatorListenerHolder that = (AnimatorListenerHolder) o;

    if (animatorType != that.animatorType) {
      return false;
    }
    return listener != null ? listener.equals(that.listener) : that.listener == null;
  }

  @Override
  public int hashCode() {
    int result = animatorType;
    result = 31 * result + (listener != null ? listener.hashCode() : 0);
    return result;
  }
}
