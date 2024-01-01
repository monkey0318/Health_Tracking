package com.example.health_tracking

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.health_tracking.activityTracking.Activity
import com.example.health_tracking.activityTracking.running
import com.example.health_tracking.activityTracking.saveActivity
import com.example.health_tracking.nutritionTracking.TestingActivity

class MainActivity : AppCompatActivity() {

    private lateinit var btnLogin :Button
    private lateinit var btnRegister: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnLogin = findViewById(R.id.btnLogin)
        btnRegister = findViewById(R.id.btnRegister)



        btnLogin.setOnClickListener {
            val intent = Intent(this, Activity::class.java)
            startActivity(intent)
        }

        btnRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}