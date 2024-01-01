package com.example.health_tracking

import android.app.Notification
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.health_tracking.databinding.ActivityNavigationBarBinding
import com.example.health_tracking.ui.dashboard.DashboardFragment
import com.example.health_tracking.ui.notifications.NotificationsFragment

class NavActivity  : AppCompatActivity() {

    private lateinit var binding : ActivityNavigationBarBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNavigationBarBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_navigation_bar)
        setContentView(binding.root)
        placeFragment(DashboardFragment())

        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home -> placeFragment(DashboardFragment())
                //R.id.healthTracker -> placeFragment(DashboardFragment())
                R.id.notification -> placeFragment(NotificationsFragment())
               // R.id.profile-> placeFragment(DashboardFragment())
                else->{}
            }
            true
        }
    }
    private fun placeFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer,fragment)
        transaction.commit()
    }

}