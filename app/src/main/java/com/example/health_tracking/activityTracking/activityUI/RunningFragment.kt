package com.example.health_tracking.activityTracking.activityUI

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.health_tracking.databinding.FragmentRunningBinding
import java.util.concurrent.TimeUnit


class RunningFragment : Fragment() {

    private lateinit var binding: FragmentRunningBinding
    private var startTimeMillis: Long = 0
    private var endTimeMillis: Long = 0

    //StopWatch
    private lateinit var handler: Handler
    private var isRunning = false
    private var elapsedTimeMillis: Long = 0

    // Constants
    private val caloriesBurnRatePerMinute = 5.0 // Example burn rate in calories per minute
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRunningBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        handler = Handler(Looper.getMainLooper())

        binding.startButton.setOnClickListener {
            // Capture the start time
            startTimeMillis = System.currentTimeMillis()
            updateUI()
            refresh()
            if (isRunning) {
                stopStopwatch()
            } else {
                startStopwatch()
            }
        }

        binding.endButton.setOnClickListener {
            // Capture the end time
            endTimeMillis = System.currentTimeMillis()
            updateUI()
            resetStopwatch()
        }
    }
    private fun updateUI() {
        // Display the captured times and calculate the time difference
        val startTimeText = "Start Time: ${formatTime(startTimeMillis)}"

        // Reset endTimeMillis and endTimeText to 00:00:00 when the user clicks the button again
        if (isRunning) {
            val endTimeText = "End Time: ${formatTime(endTimeMillis)}"
            binding.timeText.text = "$startTimeText\n$endTimeText"
        } else {
            endTimeMillis = 0L
            binding.timeText.text = startTimeText
        }

        if (startTimeMillis != 0L && endTimeMillis != 0L) {
            val durationMinutes = calculateTimeDifferenceMinutes(startTimeMillis, endTimeMillis)
            val duration = calculateTimeDifference(startTimeMillis, endTimeMillis)
            val caloriesBurnt = calculateCaloriesBurnt(durationMinutes)

            binding.caloriesText.text = "Calories Burnt: $caloriesBurnt calories"
            binding.durationText.text = "You have exercised: $duration"
        }
    }
    private fun formatTime(timeMillis: Long): String {
        val formatter = java.text.SimpleDateFormat("HH:mm:ss")
        return formatter.format(timeMillis)
    }
    private fun calculateTimeDifferenceMinutes(startMillis: Long, endMillis: Long): Long {
        val durationMillis = endMillis - startMillis
        return durationMillis / (1000 * 60) // Convert to minutes
    }
    private fun calculateCaloriesBurnt(durationMinutes: Long): Double {
        return caloriesBurnRatePerMinute * durationMinutes
    }
    private fun calculateTimeDifference(startMillis: Long, endMillis: Long): String {
        val durationMillis = endMillis - startMillis
        val hours = TimeUnit.MILLISECONDS.toHours(durationMillis)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(durationMillis) % 60
        val seconds = TimeUnit.MILLISECONDS.toSeconds(durationMillis) % 60

        val formattedTime = String.format("%02d:%02d:%02d", hours, minutes, seconds)
        return formattedTime
    }
    private fun refresh(){
        binding.durationText.text = ""
    }
    private fun startStopwatch() {
        isRunning = true
        updateStopwatchPeriodically()
    }
    private fun stopStopwatch() {
        isRunning = false
        handler.removeCallbacksAndMessages(null)
    }
    private fun resetStopwatch() {
        stopStopwatch()
        elapsedTimeMillis = 0
        updateStopwatchText()

    }
    private fun updateStopwatchPeriodically() {
        handler.post(object : Runnable {
            override fun run() {
                elapsedTimeMillis += 1000
                updateStopwatchText()
                if (isRunning) {
                    handler.postDelayed(this, 1000)
                }
            }
        })
    }

    private fun updateStopwatchText() {
        val seconds = (elapsedTimeMillis / 1000) % 60
        val minutes = (elapsedTimeMillis / (1000 * 60)) % 60
        val hours = elapsedTimeMillis / (1000 * 60 * 60)

        binding.timeTV.text = String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }
}


