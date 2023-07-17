package com.trackasia.android.testapp.utils

import android.app.Activity
import android.content.Intent
import com.trackasia.android.testapp.activity.FeatureOverviewActivity

object NavUtils {
    fun navigateHome(context: Activity) {
        context.startActivity(Intent(context, FeatureOverviewActivity::class.java))
        context.finish()
    }
}
