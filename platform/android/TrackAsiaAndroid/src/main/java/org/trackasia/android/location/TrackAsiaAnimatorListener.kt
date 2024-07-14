package org.trackasia.android.location

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import org.trackasia.android.maps.TrackAsiaMap

internal class TrackAsiaAnimatorListener(cancelableCallback: TrackAsiaMap.CancelableCallback?) :
    AnimatorListenerAdapter() {
    private val cancelableCallback: TrackAsiaMap.CancelableCallback?

    init {
        this.cancelableCallback = cancelableCallback
    }

    override fun onAnimationCancel(animation: Animator) {
        cancelableCallback?.onCancel()
    }

    override fun onAnimationEnd(animation: Animator) {
        cancelableCallback?.onFinish()
    }
}