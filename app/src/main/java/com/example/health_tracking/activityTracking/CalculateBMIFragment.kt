package com.example.health_tracking.activityTracking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.health_tracking.databinding.FragmentCalculateBMIBinding


class CalculateBMIFragment : Fragment() {

    private lateinit var binding: FragmentCalculateBMIBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCalculateBMIBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonCalculate.setOnClickListener { calculateBMI() }

    }
    private fun calculateBMI() {
        // Get user input
        val weight = binding.editTextWeight.text.toString().toFloatOrNull()
        val heightInCm = binding.editTextHeight.text.toString().toFloatOrNull()

        // Check if the input is valid
        if (weight != null && heightInCm != null && heightInCm > 0) {
            // Convert height from cm to meters
            val heightInMeters = heightInCm / 100

            // Calculate BMI
            val bmi = weight / (heightInMeters * heightInMeters)

            // Determine BMI category
            val category = when {
                bmi < 18.5 -> "Underweight"
                bmi < 24.9 -> "Normal Weight"
                bmi < 29.9 -> "Overweight"
                else -> "Obese"
            }

            // Display the result
            val resultText = String.format("BMI: %.2f\nCategory: %s", bmi, category)
            binding.textViewResult.text = resultText
        } else {
            // Display an error message if input is invalid
            binding.textViewResult.text = "Invalid input. Please enter valid weight and height."
        }
    }

}


