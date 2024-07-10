package org.trackasia.android.testapp.model.annotations

import org.trackasia.android.annotations.Marker

class CityStateMarker(
    cityStateOptions: CityStateMarkerOptions?,
    val infoWindowBackgroundColor: String,
) : Marker(cityStateOptions)
