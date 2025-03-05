package com.trackasia.android.testapp.model.annotations

import com.trackasia.android.annotations.Marker

class CityStateMarker(
    cityStateOptions: CityStateMarkerOptions?,
    val infoWindowBackgroundColor: String
) : Marker(cityStateOptions)
