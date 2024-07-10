package org.trackasia.android.testapp.model.annotations

import org.trackasia.android.annotations.BaseMarkerOptions
import org.trackasia.android.annotations.Marker

class CountryMarker(
    baseMarkerOptions: BaseMarkerOptions<*, *>?,
    val abbrevName: String,
    val flagRes: Int,
) : Marker(baseMarkerOptions)
