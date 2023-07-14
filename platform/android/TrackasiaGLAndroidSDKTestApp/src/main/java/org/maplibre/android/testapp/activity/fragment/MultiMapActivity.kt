package org.trackasia.android.testapp.activity.fragment

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import org.trackasia.android.maps.trackasiaMap
import org.trackasia.android.maps.OnMapReadyCallback
import org.trackasia.android.maps.Style
import org.trackasia.android.maps.SupportMapFragment
import org.trackasia.android.testapp.R

/**
 * Test Activity showcasing using multiple static map fragments in one layout.
 */
class MultiMapActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multi_map)
        val fragmentManager = supportFragmentManager
        initFragmentStyle(fragmentManager, R.id.map1, Style.getPredefinedStyle("Streets"))
        initFragmentStyle(fragmentManager, R.id.map2, Style.getPredefinedStyle("Bright"))
        initFragmentStyle(fragmentManager, R.id.map3, Style.getPredefinedStyle("Satellite Hybrid"))
        initFragmentStyle(fragmentManager, R.id.map4, Style.getPredefinedStyle("Pastel"))
    }

    private fun initFragmentStyle(
        fragmentManager: FragmentManager,
        fragmentId: Int,
        styleId: String
    ) {
        (fragmentManager.findFragmentById(fragmentId) as SupportMapFragment?)
            ?.getMapAsync(OnMapReadyCallback { trackasiaMap: trackasiaMap -> trackasiaMap.setStyle(styleId) })
    }
}
