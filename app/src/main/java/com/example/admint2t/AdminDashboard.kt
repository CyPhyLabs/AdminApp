package com.example.admint2t

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.admint2t.databinding.ActivityDashboardBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class AdminDashboard : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bottomNavigation: BottomNavigationView = binding.navView

        // Load AdminDashboardFragment initially
        if (savedInstanceState == null) {
            loadFragment(AdminDashboardFragment())
        }

        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.dashboardFragment -> {
                    loadFragment(AdminDashboardFragment())
                    true
                }
                R.id.eventsFragment -> {
                    loadFragment(EventsFragment())
                    true
                }
                R.id.mirrorFragment -> {
                    loadFragment(MirrorFragment())
                    true
                }
                else -> false
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, fragment) // Match the ID in activity_dashboard.xml
            .commit()
    }
}
