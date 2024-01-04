package com.example.health_tracking.activityTracking.activityUI

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.example.health_tracking.R
import com.example.health_tracking.activityTracking.data.CaloriesCalculatorViewModel
import com.example.health_tracking.databinding.FragmentCaloriesCalculatorBinding

class CaloriesCalculatorFragment : Fragment() {

    private val viewModel = CaloriesCalculatorViewModel()
    private lateinit var binding: FragmentCaloriesCalculatorBinding

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

        } else {
            binding.caloriesResult.text = "Please enter duration!!"
            binding.caloriesResult.error = "Invalid duration. Please enter a valid duration."
        }
    }

}



