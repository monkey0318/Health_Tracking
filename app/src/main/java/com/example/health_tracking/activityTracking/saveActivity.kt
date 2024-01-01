package com.example.health_tracking.activityTracking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import com.example.health_tracking.R
import com.example.health_tracking.databinding.ActivitySaveBinding
import com.google.firebase.firestore.FirebaseFirestore

class saveActivity : AppCompatActivity() {

    private val viewModel = saveActivityViewModel()
    private lateinit var binding: ActivitySaveBinding
    private lateinit var subButton: Button

    // Initialize Firestore
    val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySaveBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        subButton = findViewById(R.id.submitButton)


        binding.activitySpinner.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            resources.getStringArray(R.array.activity_array)
        )

        subButton.setOnClickListener { calculateCalories(it) }

    }

    fun calculateCalories(view: View) {
        val selectedActivity = binding.activitySpinner.selectedItem.toString()
        val duration = binding.durationEditText.text.toString().toFloatOrNull() ?: 0f

        if (duration > 0) {
            val caloriesBurnt = viewModel.calculateCalories(selectedActivity, duration)
            binding.caloriesResult.text = "Calories burnt: $caloriesBurnt kcal"
            binding.caloriesResult.error = null // Clear any previous error

            // Save data to Firestore
            saveDataToFirestore(selectedActivity, duration, caloriesBurnt.toFloat())
        } else {
            binding.caloriesResult.text = "Please enter duration!!"
            binding.caloriesResult.error = "Invalid duration. Please enter a valid duration."
        }
    }

    private fun saveDataToFirestore(activity: String, duration: Float, caloriesBurnt: Float) {
        // Create a Map to represent the data
        val data = hashMapOf(
            "activity" to activity,
            "duration" to duration,
            "caloriesBurnt" to caloriesBurnt
        )

        // Add a new document with a generated ID
        db.collection("exercises") // Replace with your Firestore collection name
            .add(data)
            .addOnSuccessListener { documentReference ->
                // Document added successfully
                println("DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                // Error adding document
                println("Error adding document: $e")
            }
    }
}
