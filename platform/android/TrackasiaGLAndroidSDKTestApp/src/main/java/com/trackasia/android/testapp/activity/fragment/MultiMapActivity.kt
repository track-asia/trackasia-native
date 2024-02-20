package com.trackasia.android.testapp.activity.fragment

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.trackasia.android.maps.TrackasiaMap
import com.trackasia.android.maps.OnMapReadyCallback
import com.trackasia.android.maps.Style
import com.trackasia.android.maps.SupportMapFragment
import com.trackasia.android.testapp.R

/**
 * Test Activity showcasing using multiple static map fragments in one layout.
 */
class MultiMapActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multi_map)
        val fragmentManager = supportFragmentManager
        initFragmentStyle(fragmentManager, R.id.map1, "https://tiles.track-asia.com/tiles/v1/style-streets.json?key=public")
        initFragmentStyle(fragmentManager, R.id.map2, "https://tiles.track-asia.com/tiles/v1/style-streets.json?key=public")
        initFragmentStyle(fragmentManager, R.id.map3, "https://tiles.track-asia.com/tiles/v1/style-streets.json?key=public")
        initFragmentStyle(fragmentManager, R.id.map4, "https://tiles.track-asia.com/tiles/v1/style-streets.json?key=public")
    }

    private fun initFragmentStyle(
        fragmentManager: FragmentManager,
        fragmentId: Int,
        styleId: String
    ) {
        (fragmentManager.findFragmentById(fragmentId) as SupportMapFragment?)
            ?.getMapAsync(OnMapReadyCallback { mapboxMap: TrackasiaMap -> mapboxMap.setStyle(styleId) })
    }
}
