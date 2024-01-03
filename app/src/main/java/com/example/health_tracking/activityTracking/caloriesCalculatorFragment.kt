package com.example.health_tracking.activityTracking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.example.health_tracking.R
import com.example.health_tracking.activityTracking.data.saveActivityViewModel
import com.example.health_tracking.databinding.FragmentCaloriesCalculatorBinding
import com.google.firebase.firestore.FirebaseFirestore


class caloriesCalculatorFragment : Fragment() {

    private val viewModel = saveActivityViewModel()
    private lateinit var binding: FragmentCaloriesCalculatorBinding

    // Initialize Firestore
    val db = FirebaseFirestore.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCaloriesCalculatorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.activitySpinner.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            resources.getStringArray(R.array.activity_array)
        )

        binding.submitButton.setOnClickListener { calculateCalories(it) }
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



