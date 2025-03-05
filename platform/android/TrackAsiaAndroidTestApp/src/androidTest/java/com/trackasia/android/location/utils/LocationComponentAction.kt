package com.trackasia.android.location.utils

import android.content.Context
import android.view.View
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import com.trackasia.android.location.LocationComponent
import com.trackasia.android.maps.TrackAsiaMap
import com.trackasia.android.maps.Style
import org.hamcrest.Matcher

class LocationComponentAction(
    private val trackasiaMap: TrackAsiaMap,
    private val onPerformLocationComponentAction: OnPerformLocationComponentAction
) : ViewAction {

    override fun getConstraints(): Matcher<View> {
        return isDisplayed()
    }

    override fun getDescription(): String {
        return javaClass.simpleName
    }

    override fun perform(uiController: UiController, view: View) {
        onPerformLocationComponentAction.onLocationComponentAction(
            trackasiaMap.locationComponent,
            trackasiaMap,
            trackasiaMap.style!!,
            uiController,
            view.context
        )
    }

    interface OnPerformLocationComponentAction {
        fun onLocationComponentAction(component: LocationComponent, trackasiaMap: TrackAsiaMap, style: Style, uiController: UiController, context: Context)
    }
}
