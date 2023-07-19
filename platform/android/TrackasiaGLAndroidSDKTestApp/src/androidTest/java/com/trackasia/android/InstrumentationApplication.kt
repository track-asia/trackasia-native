package com.trackasia.android

import com.trackasia.android.testapp.TrackasiaApplication

class InstrumentationApplication : TrackasiaApplication() {
    fun initializeLeakCanary(): Boolean {
        // do not initialize leak canary during instrumentation tests
        return true
    }
}
