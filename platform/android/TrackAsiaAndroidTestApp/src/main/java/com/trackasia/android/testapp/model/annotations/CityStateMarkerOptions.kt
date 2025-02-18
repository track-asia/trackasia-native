package com.trackasia.android.testapp.model.annotations

import android.graphics.Bitmap
import android.os.Parcel
import android.os.Parcelable
import com.trackasia.android.annotations.BaseMarkerOptions
import com.trackasia.android.annotations.IconFactory
import com.trackasia.android.geometry.LatLng

class CityStateMarkerOptions : BaseMarkerOptions<CityStateMarker?, CityStateMarkerOptions?> {
    private var infoWindowBackgroundColor: String? = null

    fun infoWindowBackground(color: String?): CityStateMarkerOptions {
        infoWindowBackgroundColor = color
        return getThis()
    }

    constructor()

    private constructor(`in`: Parcel) {
        position(`in`.readParcelable<Parcelable>(LatLng::class.java.classLoader) as LatLng)
        snippet(`in`.readString())
        val iconId = `in`.readString()
        val iconBitmap = `in`.readParcelable<Bitmap>(Bitmap::class.java.classLoader)
        val icon = iconBitmap?.let { IconFactory.recreate(iconId.toString(), it) }
        icon(icon)
        title(`in`.readString())
    }

    override fun getThis(): CityStateMarkerOptions = this

    override fun getMarker(): CityStateMarker? = infoWindowBackgroundColor?.let { CityStateMarker(this, it) }

    override fun describeContents(): Int = 0

    override fun writeToParcel(
        out: Parcel,
        flags: Int,
    ) {
        out.writeParcelable(position, flags)
        out.writeString(snippet)
        out.writeString(icon.id)
        out.writeParcelable(icon.bitmap, flags)
        out.writeString(title)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<CityStateMarkerOptions?> =
            object : Parcelable.Creator<CityStateMarkerOptions?> {
                override fun createFromParcel(`in`: Parcel): CityStateMarkerOptions = CityStateMarkerOptions(`in`)

                override fun newArray(size: Int): Array<CityStateMarkerOptions?> = arrayOfNulls(size)
            }
    }
}
