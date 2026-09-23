package com.ramdigital.ramwalls.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.ramdigital.ramwalls.R
import com.ramdigital.ramwalls.ads.InterstitialAdController
import com.ramdigital.ramwalls.databinding.ActivityMainBinding
import com.ramdigital.ramwalls.ui.favorites.FavoritesFragment
import com.ramdigital.ramwalls.ui.home.HomeFragment
import com.ramdigital.ramwalls.ui.settings.SettingsFragment

/**
 * Single-activity host with a [com.google.android.material.bottomnavigation.BottomNavigationView]
 * that swaps between the Home, Favorites and Settings fragments.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        InterstitialAdController.preload(this)

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            val fragment: Fragment = when (item.itemId) {
                R.id.nav_home -> HomeFragment()
                R.id.nav_favorites -> FavoritesFragment()
                R.id.nav_settings -> SettingsFragment()
                else -> return@setOnItemSelectedListener false
            }
            supportFragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.nav_host_container, fragment)
                .commit()
            true
        }

        // Restore happens automatically on config change; only seed on first launch.
        if (savedInstanceState == null) {
            binding.bottomNavigation.selectedItemId = R.id.nav_home
        }
    }
}
