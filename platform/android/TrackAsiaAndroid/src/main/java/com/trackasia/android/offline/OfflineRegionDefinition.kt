package com.trackasia.android.offline

import android.os.Parcelable
import androidx.annotation.Keep
import com.trackasia.android.geometry.LatLngBounds

/**
 * This is the interface that all Offline Region definitions have to implement.
 *
 *
 * For the present, a tile pyramid is the only type of offline region.
 */
@Keep
interface OfflineRegionDefinition : Parcelable {
    /**
     * Gets the bounds of the OfflineRegion.
     *
     * @return the bounds of the OfflineRegion.
     */
    val bounds: LatLngBounds?

    /**
     * Returns the map style url of the OfflineRegion.
     *
     * @return The URL of the map style.
     */
    val styleURL: String?

    /**
     * Gets the minimum zoom level the OfflineRegion map can be displayed at.
     *
     * @return The minimum zoom level.
     */
    val minZoom: Double

    /**
     * Gets the maximum zoom level the OfflineRegion map can be displayed at.
     *
     * @return The maximum zoom level.
     */
    val maxZoom: Double

    /**
     * Gets the pixel ratio of the OfflineRegion map.
     *
     * @return The pixel ratio of the OfflineRegion map.
     */
    val pixelRatio: Float

    /**
     * Specifies whether to include ideographic glyphs in downloaded font data.
     * Ideographic glyphs make up the majority of downloaded font data, but
     * it is possible to configure the renderer to use locally installed fonts
     * instead of relying on fonts downloaded as part of the offline pack.
     *
     * Defaults to `true`
     *
     * @return true if offline region will include ideographic glyphs
     * @see TrackAsiaMapOptions.localIdeographFontFamily
     */
    val includeIdeographs: Boolean

    /**
     * Gets the type of the OfflineRegionDefinition ("tileregion", "shaperegion").
     *
     * @return The type of the OfflineRegionDefinition.
     */
    val type: String
}
