package com.trackasia.android.testapp.activity.fragment

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.trackasia.android.maps.TrackasiaMap
import com.trackasia.android.maps.Style
import com.trackasia.android.maps.SupportMapFragment
import com.trackasia.android.testapp.R
import com.trackasia.android.testapp.databinding.ActivityBackstackFragmentBinding
import com.trackasia.android.testapp.utils.NavUtils

/**
 * Test activity showcasing using the MapFragment API as part of a backstacked fragment.
 */
class FragmentBackStackActivity : AppCompatActivity() {

    companion object {
        private const val FRAGMENT_TAG = "map_fragment"
    }

    private lateinit var binding: ActivityBackstackFragmentBinding
    private lateinit var mapFragment: SupportMapFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBackstackFragmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            mapFragment = SupportMapFragment.newInstance()
            mapFragment.getMapAsync { initMap(it) }

            supportFragmentManager.beginTransaction().apply {
                add(R.id.container, mapFragment, FRAGMENT_TAG)
            }.commit()
        } else {
            supportFragmentManager.findFragmentByTag(FRAGMENT_TAG)?.also { fragment ->
                if (fragment is SupportMapFragment) {
                    fragment.getMapAsync { initMap(it) }
                }
            }
        }

        binding.button.setOnClickListener { handleClick() }
    }

    private fun initMap(mapboxMap: TrackasiaMap) {
        mapboxMap.setStyle("https://tiles.track-asia.com/tiles/v1/style-streets.json?key=public")
    }

    private fun handleClick() {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.container, NestedViewPagerActivity.ItemAdapter.EmptyFragment())
            addToBackStack("map_empty_fragment")
        }.commit()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 0) {
            // activity uses singleInstance for testing purposes
            // code below provides a default navigation when using the app
            NavUtils.navigateHome(this)
        } else {
            super.onBackPressed()
        }
    }
}
