package com.example.health_tracking.waterTracking

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.health_tracking.R
import com.example.health_tracking.databinding.ActivityWaterTrackingBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class WaterTrackingActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    private var currentWaterIntake = 0
    private lateinit var binding : ActivityWaterTrackingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWaterTrackingBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_water_tracking)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Load saved data if available
        loadSavedData()

        // Set initial values
        updateUI()

        // Button click event
        binding.recordWaterButton.setOnClickListener {
            recordWaterIntake()
            updateUI()
        }

        // Set goal button click event
        binding.setGoalButton.setOnClickListener {
            setWaterGoal()
            updateUI()
        }
    }

    private fun recordWaterIntake() {
        // Assuming the user inputs the water intake amount
        val intakeAmount = binding.userInputEditText.text.toString().toIntOrNull() ?: 0
        currentWaterIntake += intakeAmount

        // Update Firestore document in the "goals" subcollection
        val userId = auth.currentUser?.uid
        userId?.let {
            val goalsCollection = firestore.collection("waterData").document(it).collection("goals")
            val goalDoc = goalsCollection.document("daily_goal")

            goalDoc.update("waterIntake", currentWaterIntake)

            // Check if the daily goal is reached
            val goalAmount = binding.goalInputEditText.text.toString().toIntOrNull() ?: 0
            if (currentWaterIntake >= goalAmount) {
                notifyUserGoalAchieved()
            }
        }

        // Update UI
        updateUI()
    }

    private fun setWaterGoal() {
        // Assuming the user inputs the water goal amount
        val goalAmount = binding.goalInputEditText.text.toString().toIntOrNull() ?: 0

        // Update Firestore document in the "goals" subcollection
        val userId = auth.currentUser?.uid
        userId?.let {
            val goalsCollection = firestore.collection("waterData").document(it).collection("goals")
            val goalDoc = goalsCollection.document("daily_goal")

            goalDoc.set(mapOf("dailyGoal" to goalAmount))
                .addOnSuccessListener {
                    // Update UI after successfully setting the goal
                    updateUI()
                }
                .addOnFailureListener { e ->
                    // Handle failure, if needed
                    Log.e("Firestore", "Error updating goal", e)
                }
        }
    }

    private fun updateUI() {
        binding.currentIntakeTextView.text = getString(R.string.current_intake, currentWaterIntake)
    }

    private fun loadSavedData() {
        val userId = auth.currentUser?.uid
        userId?.let {
            val goalsCollection = firestore.collection("waterData").document(it).collection("goals")
            val goalDoc = goalsCollection.document("daily_goal")

            // Read data from Firestore document in the "goals" subcollection
            goalDoc.get().addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val goalAmount = documentSnapshot.getLong("dailyGoal")?.toInt() ?: 2000
                    binding.goalTextView.text = getString(R.string.daily_goal, goalAmount)

                    // Update UI after successfully loading the goal
                    updateUI()
                }
            }
                .addOnFailureListener { e ->
                    // Handle failure, if needed
                    Log.e("Firestore", "Error loading goal", e)
                }
        }
    }

    private fun notifyUserGoalAchieved() {
        // Display a toast notification
        Toast.makeText(this, "Congratulations! You've reached your daily water goal!", Toast.LENGTH_SHORT).show()
    }
}