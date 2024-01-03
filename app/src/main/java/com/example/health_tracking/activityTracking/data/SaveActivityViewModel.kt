package com.example.health_tracking.activityTracking.data

import androidx.lifecycle.ViewModel

class saveActivityViewModel : ViewModel() {

    fun calculateCalories(activity: String, duration: Float): Int {
        val caloriesPerMinute = when (activity) {
            "Badminton" -> 5
            "Running" -> 10
            "Gym" -> 8
            "Swimming" -> 6
            else -> 0
        }

        return (caloriesPerMinute * duration).toInt()
    }
}
