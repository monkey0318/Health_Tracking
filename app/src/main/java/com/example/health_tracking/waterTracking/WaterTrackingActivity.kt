package com.example.health_tracking.waterTracking

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.health_tracking.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class WaterTrackingActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private var currentWaterIntake = 0
    private var goalWaterAmount = 0

    lateinit var recordWaterButton: Button
    lateinit var setGoalButton: Button
    private lateinit var userInputEditText: EditText
    private lateinit var goalInputEditText: EditText
    private lateinit var currentIntakeTextView: TextView
    private lateinit var goalTextView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_water_tracking)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        currentIntakeTextView = findViewById(R.id.currentIntakeTextView)
        goalTextView = findViewById(R.id.goalTextView)
        userInputEditText = findViewById(R.id.userInputEditText)
        goalInputEditText = findViewById(R.id.goalInputEditText)
        recordWaterButton = findViewById(R.id.recordWaterButton)
        setGoalButton = findViewById(R.id.setGoalButton)

        // Load saved data if available
        loadSavedData()

        // Set initial values
        updateUI()

        // Button click event
        recordWaterButton.setOnClickListener {
            recordWaterIntake()
            updateUI()
        }

        // Set goal button click event
        setGoalButton.setOnClickListener {
            setWaterGoal()
            updateUI()
        }
    }

    private fun recordWaterIntake() {
        val intakeAmount = userInputEditText.text.toString().toIntOrNull() ?: 0
        currentWaterIntake += intakeAmount


        // Update Firebase database
        val userId = auth.currentUser?.uid
        userId?.let {
            database.reference.child("users").child(it).child("waterIntake").setValue(currentWaterIntake)
        }
    }

    private fun setWaterGoal() {
        // Assuming the user inputs the water goal amount
        val goalAmount = goalInputEditText.text.toString().toIntOrNull() ?: 0
        goalWaterAmount += goalAmount

        // Update Firebase database
        val userId = auth.currentUser?.uid
        userId?.let {
            database.reference.child("users").child(it).child("dailyGoal").setValue(goalWaterAmount)
        }

        updateUI()
    }

    private fun updateUI() {
        // Load saved data to update the goalTextView
        loadSavedData()

        // Update other UI elements if needed
        currentIntakeTextView.text = getString(R.string.current_intake, currentWaterIntake)
        goalTextView.text = getString(R.string.daily_goal, goalWaterAmount)
    }

    private fun loadSavedData() {
        val userId = auth.currentUser?.uid
        userId?.let {
            val userRef = database.reference.child("users").child(it)

            // Read data from Firebase database
            userRef.child("dailyGoal").get().addOnSuccessListener { snapshot ->
                val goalAmount = snapshot.value as? Int ?: goalWaterAmount
                goalTextView.text = getString(R.string.daily_goal, goalAmount)

                // Debugging statement
                Log.d("FirebaseDebug", "Loaded goal amount: $goalAmount")
            }
        }
    }
}