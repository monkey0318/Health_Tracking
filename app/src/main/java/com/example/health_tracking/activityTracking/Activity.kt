package com.example.health_tracking.activityTracking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.health_tracking.R
import com.example.health_tracking.databinding.ActivityBinding

class Activity : AppCompatActivity() {


    private lateinit var binding: ActivityBinding
    private lateinit var exButton: Button
    private lateinit var calButton: Button
    private lateinit var actButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        exButton = findViewById(R.id.exButton)
        calButton = findViewById(R.id.calButton)
        actButton = findViewById(R.id.actButton)

        exButton.setOnClickListener { launchActivity("running") }
        calButton.setOnClickListener { launchActivity("CalculateBMI") }
        actButton.setOnClickListener { launchActivity("saveActivity") }
    }

    private fun launchActivity(activityName: String) {
        val context = this

        val intent = when (activityName) {
            "running" -> Intent(context, running::class.java)
            "CalculateBMI" -> Intent(context, CalculateBMI::class.java)
            "saveActivity" -> Intent(context, saveActivity::class.java)
            else -> throw IllegalArgumentException("Unknown activity: $activityName")
        }

        context.startActivity(intent)
    }
}