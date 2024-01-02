package com.example.health_tracking

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.health_tracking.databinding.ActivityDrawerSlideBinding
import com.example.health_tracking.nutritionTracking.NutritionFragment

import com.example.health_tracking.ui.dashboard.DashboardFragment
import com.example.health_tracking.ui.notifications.NotificationsFragment
import com.google.android.material.navigation.NavigationView

class DrawerActivity  : AppCompatActivity() {


    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_drawer_slide)

        drawerLayout  = findViewById(R.id.drawerLayout)
        navView = findViewById(R.id.navigationView)

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open,
            R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener {

            it.isChecked = true
            when(it.itemId){

                R.id.home -> placeFragment(DashboardFragment(),it.title.toString())
              //  R.id.profile -> placeFragment(DashboardFragment(),it.title.toString())
               // R.id.sleepTracking -> placeFragment(DashboardFragment(),it.title.toString())
                R.id.nutritionTracking -> placeFragment(NutritionFragment(),it.title.toString())
              //  R.id.waterTracking -> placeFragment(DashboardFragment(),it.title.toString())
               // R.id.activityTracking -> placeFragment(DashboardFragment(),it.title.toString())
              //  R.id.bmiCalculator -> placeFragment(DashboardFragment(),it.title.toString())
              //  R.id.notification -> placeFragment(DashboardFragment(),it.title.toString())


            }
            true
        }



    }

    private fun placeFragment(fragment: Fragment,title:String){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer,fragment)
        transaction.commit()
        drawerLayout.closeDrawer(navView)
        setTitle(title)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return true
    }

}