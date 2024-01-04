package com.example.health_tracking.waterTracking

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.health_tracking.R
import com.example.health_tracking.databinding.FragmentNutritionBinding
import com.example.health_tracking.databinding.FragmentWaterTrackingBinding
import com.google.firebase.firestore.FirebaseFirestore

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [WaterTrackingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class WaterTrackingFragment : Fragment() {
    private lateinit var firestore: FirebaseFirestore

    private var currentWaterIntake = 0
    private lateinit var binding : FragmentWaterTrackingBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWaterTrackingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        // Update Firestore document
        val userId = "defaultUser"
        val goalsCollection = firestore.collection("waterData").document(userId).collection("goals")
        val goalDoc = goalsCollection.document("daily_goal")

        goalDoc.update("waterIntake", currentWaterIntake)

        // Check if the daily goal is reached
        val goalAmount = binding.goalInputEditText.text.toString().toIntOrNull() ?: 0
        if (currentWaterIntake >= goalAmount) {
            notifyUserGoalAchieved()
        }

        // Update UI
        updateUI()
    }

    private fun setWaterGoal() {
        // Assuming the user inputs the water goal amount
        val goalAmount = binding.goalInputEditText.text.toString().toIntOrNull() ?: 0

        // Update Firestore document
        val userId = "defaultUser"
        val goalsCollection = firestore.collection("waterData").document(userId).collection("goals")
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

    private fun updateUI() {
        binding.currentIntakeTextView.text = getString(R.string.current_intake, currentWaterIntake)
    }

    private fun loadSavedData() {
        val userId = "defaultUser"
        val goalsCollection = firestore.collection("waterData").document(userId).collection("goals")
        val goalDoc = goalsCollection.document("daily_goal")

        // Read data from Firestore document
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


    private fun notifyUserGoalAchieved() {
        // Display a toast notification
        Toast.makeText(context, "Congratulations! You've reached your daily water goal!", Toast.LENGTH_SHORT).show()
    }


}