package com.trackasia.android.testapp.model.annotations

import com.trackasia.android.annotations.BaseMarkerOptions
import com.trackasia.android.annotations.Marker

class CountryMarker(
    baseMarkerOptions: BaseMarkerOptions<*, *>?,
    val abbrevName: String,
    val flagRes: Int
) : Marker(baseMarkerOptions)
