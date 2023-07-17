package com.trackasia.android

import com.trackasia.android.testapp.MapLibreApplication

class InstrumentationApplication : MapLibreApplication() {
    fun initializeLeakCanary(): Boolean {
        // do not initialize leak canary during instrumentation tests
        return true
    }
}
