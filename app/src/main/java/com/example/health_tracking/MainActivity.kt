package com.example.health_tracking


import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.health_tracking.activityTracking.ActivityLayout
import com.example.health_tracking.activityTracking.saveActivity
import com.example.health_tracking.databinding.ActivityBinding
import com.example.health_tracking.databinding.ActivityMainBinding
import com.example.health_tracking.databinding.ActivityRunningBinding
import com.example.health_tracking.databinding.ActivitySaveBinding
import com.example.health_tracking.nutritionTracking.NutritionTrackingActivity
import com.example.health_tracking.waterTracking.WaterTrackingActivity


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            val intent = Intent(this, WaterTrackingActivity::class.java)
            startActivity(intent)
        }

        binding.btnRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}