package com.example.health_tracking


import androidx.lifecycle.ViewModelProvider
import android.os.Bundle

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer

import java.util.concurrent.TimeUnit


class SleepTrackingFragment : Fragment() {

    private lateinit var viewModel: SleepTrackingViewModel


    private lateinit var textViewTime: TextView
    private lateinit var btnStart: ImageButton
    private lateinit var btnPause: ImageButton
    private lateinit var btnReset: ImageButton
    private lateinit var btnStop: ImageButton


    private var startTimeMillis: Long = 0
    private var isTimerRunning = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sleep_tracking, container, false)

        textViewTime = view.findViewById(R.id.textViewTime)
        btnStart = view.findViewById(R.id.btnStart)
        btnPause = view.findViewById(R.id.btnPause)
        btnStop = view.findViewById(R.id.btnStop)
        btnReset = view.findViewById(R.id.btnReset)

        btnStart.setOnClickListener {
            if (!isTimerRunning) {
                // Set the base time to the current time only if the timer is not running
                if (startTimeMillis == 0L) {
                    startTimeMillis = System.currentTimeMillis()
                }

                // Start updating the elapsed time
                textViewTime.post(updateTimeRunnable)

                isTimerRunning = true

                btnStart.visibility = View.GONE
                btnPause.visibility = View.VISIBLE

            }
        }

        btnPause.setOnClickListener {
            // Pause the  time
            textViewTime.removeCallbacks(updateTimeRunnable)
            isTimerRunning = false

            btnPause.visibility = View.GONE
            btnStart.visibility = View.VISIBLE
        }
        btnReset.setOnClickListener {
            textViewTime.removeCallbacks(updateTimeRunnable)
            isTimerRunning = false
            startTimeMillis = System.currentTimeMillis()
            updateTime()
            btnPause.visibility = View.GONE
            btnStart.visibility = View.VISIBLE

        }
        btnStop.setOnClickListener{


            textViewTime.removeCallbacks(updateTimeRunnable)
            isTimerRunning = false

            val elapsedTimeMillis = System.currentTimeMillis() - startTimeMillis
            val hours = TimeUnit.MILLISECONDS.toHours(elapsedTimeMillis)
            val minutes = TimeUnit.MILLISECONDS.toMinutes(elapsedTimeMillis) % 60
            val seconds = TimeUnit.MILLISECONDS.toSeconds(elapsedTimeMillis) % 60

            val timeMillis = String.format("%02d:%02d:%02d", hours, minutes, seconds)


            val ratingSleepTime = calculateSleepTimeRating(hours).toFloat()


            viewModel.saveTimeToFirestore(timeMillis, ratingSleepTime)

            startTimeMillis = System.currentTimeMillis()
            updateTime()

            btnPause.visibility = View.GONE
            btnStart.visibility = View.VISIBLE


            val sleepMoodFragment = SleepMoodFragment()
            val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
            val transaction: FragmentTransaction = fragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentContainer, sleepMoodFragment)
            transaction.commit()
        }


        return view
    }

    private fun calculateSleepTimeRating(hours: Long): Int {
        return when {
            hours >= 7 -> 5 // 5star over 7 hours
            hours >= 5 -> 4 // 4star  7 hours
            hours >= 3 -> 3 // 3star  3 to 5 hours
            hours >= 1 -> 2 // 2star  1 to 3 hours
            else -> 1 // 1star less than 1 hour
        }
    }

    private val updateTimeRunnable = object : Runnable {
        override fun run() {
            // Calculate the  time in hh::mm:ss
            val elapsedTimeMillis = System.currentTimeMillis() - startTimeMillis
            val hours = TimeUnit.MILLISECONDS.toHours(elapsedTimeMillis)
            val minutes = TimeUnit.MILLISECONDS.toMinutes(elapsedTimeMillis) % 60
            val seconds = TimeUnit.MILLISECONDS.toSeconds(elapsedTimeMillis) % 60


            val formattedTime = String.format("%02d:%02d:%02d", hours, minutes, seconds)


            textViewTime.text = formattedTime


            textViewTime.postDelayed(this, 1000)
        }
    }

    // Function to update the initial time
    private fun updateTime() {
        val elapsedTimeMillis = System.currentTimeMillis() - startTimeMillis
        val hours = TimeUnit.MILLISECONDS.toHours(elapsedTimeMillis)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(elapsedTimeMillis) % 60
        val seconds = TimeUnit.MILLISECONDS.toSeconds(elapsedTimeMillis) % 60


        val formattedTime = String.format("%02d:%02d:%02d", hours, minutes, seconds)

        // Set the initial time on the TextView
        textViewTime.text = formattedTime
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(SleepTrackingViewModel::class.java)


        viewModel.saveSuccess.observe(viewLifecycleOwner, Observer {
            Toast.makeText(requireContext(), "Saved Record", Toast.LENGTH_SHORT).show()
        })

        viewModel.saveFailure.observe(viewLifecycleOwner, Observer { errorMessage ->
            Toast.makeText(requireContext(), "Error: $errorMessage", Toast.LENGTH_SHORT).show()
        })
    }

}
