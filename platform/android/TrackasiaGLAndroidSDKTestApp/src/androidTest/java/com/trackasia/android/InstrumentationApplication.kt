package com.trackasia.android

import com.trackasia.android.testapp.TrackAsiaApplication

class InstrumentationApplication : TrackAsiaApplication() {
    fun initializeLeakCanary(): Boolean {
        // do not initialize leak canary during instrumentation tests
        return true
    }
}
