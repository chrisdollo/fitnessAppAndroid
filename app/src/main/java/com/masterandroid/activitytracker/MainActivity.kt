package com.masterandroid.activitytracker

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity


import com.masterandroid.activitytracker.databinding.ActivityMainBinding
import androidx.fragment.app.Fragment
import com.masterandroid.activitytracker.fragments.DashboardFragment
import com.masterandroid.activitytracker.fragments.HistoryFragment
import com.masterandroid.activitytracker.fragments.SettingsFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Load Dash board fragment by default
        loadFragment(DashboardFragment())


        // we set up listener for our menu
        binding.bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.dashboard -> loadFragment(DashboardFragment())
                R.id.history -> loadFragment(HistoryFragment())
                R.id.settings -> loadFragment(SettingsFragment())
            }
            true
        }
    }


    // function that loads the new fragment
    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}