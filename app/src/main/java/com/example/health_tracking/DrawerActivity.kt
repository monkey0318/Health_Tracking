package com.example.health_tracking

import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.health_tracking.databinding.ActivityDrawerSlideBinding
import com.example.health_tracking.nutritionTracking.NutritionFragment

import com.example.health_tracking.ui.dashboard.DashboardFragment
import com.example.health_tracking.ui.notifications.NotificationsFragment
import com.example.health_tracking.waterTracking.WaterTrackingFragment
import com.google.android.material.navigation.NavigationView
import com.google.firebase.firestore.FirebaseFirestore

class DrawerActivity  : AppCompatActivity() {


    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var db: FirebaseFirestore
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

        db = FirebaseFirestore.getInstance()

        // Retrieve user email  from LoginActivity
        val currentUserEmail = intent.getStringExtra("email")

        // Get the NavigationView from the layout
        val navView: NavigationView = findViewById(R.id.navigationView)

        // Inflate the header layout to access its views
        val headerView = navView.getHeaderView(0)

        val nameTextView: TextView = headerView.findViewById(R.id.nameTextView)

        // Retrieve the username from Firestore based on the user's email
        if (currentUserEmail != null) {
            db.collection("users")
                .whereEqualTo("email", currentUserEmail)
                .get()
                .addOnSuccessListener { result ->
                    if (result.isEmpty) {
                        Toast.makeText(applicationContext, "user not found!!", Toast.LENGTH_SHORT).show()
                    } else {
                        // Successfully found the user in Firestore
                        val name = result.documents[0].getString("name")
                        nameTextView.text = "$name"
                    }
                }
                .addOnFailureListener { exception ->

                    Toast.makeText(applicationContext, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        }
        else{Toast.makeText(applicationContext, "Error:No user info", Toast.LENGTH_SHORT).show()}




        startFirstFragment(DashboardFragment())
        navView.setNavigationItemSelectedListener {

            it.isChecked = true
            when(it.itemId){

                R.id.home -> placeFragment(DashboardFragment(),it.title.toString())
              //  R.id.profile -> placeFragment(DashboardFragment(),it.title.toString())
               R.id.sleepTracking -> placeFragment(SleepTrackingFragment(),it.title.toString())
                R.id.nutritionTracking -> placeFragment(NutritionFragment(),it.title.toString())
                R.id.waterTracking -> placeFragment(WaterTrackingFragment(),it.title.toString())
               // R.id.activityTracking -> placeFragment(DashboardFragment(),it.title.toString())
              //  R.id.bmiCalculator -> placeFragment(DashboardFragment(),it.title.toString())
              //  R.id.notification -> placeFragment(DashboardFragment(),it.title.toString())


            }
            true
        }



    }

    private fun startFirstFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer,fragment)
        transaction.commit()


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