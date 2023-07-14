package org.trackasia.android

import org.trackasia.android.testapp.trackasiaApplication

class InstrumentationApplication : trackasiaApplication() {
    fun initializeLeakCanary(): Boolean {
        // do not initialize leak canary during instrumentation tests
        return true
    }
}
