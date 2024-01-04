package com.example.health_tracking

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.health_tracking.activityTracking.ActivityTracking
import com.example.health_tracking.activityTracking.activityUI.CalculateBMIFragment
import com.example.health_tracking.activityTracking.activityUI.RunningFragment
import com.example.health_tracking.activityTracking.activityUI.CaloriesCalculatorFragment
import com.example.health_tracking.nutritionTracking.NutritionFragment
import com.example.health_tracking.profile.ProfileFragment
import com.example.health_tracking.ui.dashboard.DashboardFragment
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
                R.id.profile -> placeFragment(ProfileFragment(),it.title.toString())
                R.id.sleepTracking -> placeFragment(SleepTrackingFragment(),it.title.toString())
                R.id.nutritionTracking -> placeFragment(NutritionFragment(),it.title.toString())
                R.id.waterTracking -> placeFragment(WaterTrackingFragment(),it.title.toString())
                R.id.activityTracking -> startActivity()//
                R.id.bmiCalculator -> placeFragment(CalculateBMIFragment(),it.title.toString())
                R.id.runner -> placeFragment(RunningFragment(),it.title.toString())
                R.id.calCalculator -> placeFragment(CaloriesCalculatorFragment(),it.title.toString())
                R.id.logout -> logOutActivity()
            }
            true
        }
    }

    private fun logOutActivity() {
        // Display a Toast indicating successful logout
        Toast.makeText(this, "You have logged out successfully", Toast.LENGTH_SHORT).show()

        // Navigate to the login screen or perform any other post-logout actions
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun startActivity(){
        val intent = Intent(this, ActivityTracking::class.java)
        startActivity(intent)
    }
//    private fun calBMI(){
//        val intent = Intent(this, CalculateBMI::class.java)
//        startActivity(intent)
//    }
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