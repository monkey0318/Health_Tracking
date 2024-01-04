package com.example.health_tracking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

class SleepMoodFragment : Fragment() {

    private lateinit var viewModel: SleepMoodViewModel
    private lateinit var btnSubmit: Button
    private lateinit var ratingBarMoodBefore: RatingBar
    private lateinit var ratingBarMoodAfter: RatingBar
    private lateinit var ratingBarDisturbance: RatingBar
    private lateinit var editTextComment: EditText



    private var moodBeforeRating: Float = 0.0f
    private var moodAfterRating: Float = 0.0f
    private var disturbanceRating: Float = 0.0f
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sleep_mood, container, false)

        btnSubmit = view.findViewById(R.id.btnSubmit)
        ratingBarMoodBefore = view.findViewById(R.id.ratingBarMoodBeforeSleep)
        ratingBarMoodAfter = view.findViewById(R.id.ratingBarMoodAfterSleep)
        ratingBarDisturbance = view.findViewById(R.id.ratingBarDisturbance)
        editTextComment = view.findViewById(R.id.editTextComment)

        val ratingSleepTime = arguments?.getFloat("ratingSleepTime", 0.0f) ?: 0.0f



        btnSubmit.setOnClickListener{
            moodBeforeRating = ratingBarMoodBefore.rating
            moodAfterRating = ratingBarMoodAfter.rating
            disturbanceRating = ratingBarDisturbance.rating


            val timeMillis:String? = arguments?.getString("timeMillis")
            val editTextComment = editTextComment.text.toString()
            if (timeMillis != null) {
                viewModel.saveTimeToFirestore(timeMillis, ratingSleepTime, moodBeforeRating, moodAfterRating, disturbanceRating, editTextComment)

                val HistorySleepTrackingFragment=HistorySleepTrackingFragment()
                val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
                val transaction: FragmentTransaction = fragmentManager.beginTransaction()
                transaction.replace(R.id.fragmentContainer, HistorySleepTrackingFragment)
                transaction.commit()

            } else {
                Toast.makeText(requireContext(), "No time millis", Toast.LENGTH_SHORT).show()
            }

        }

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(SleepMoodViewModel::class.java)
        viewModel.saveSuccess.observe(viewLifecycleOwner, Observer {
            Toast.makeText(requireContext(), "Saved Record", Toast.LENGTH_SHORT).show()
        })

        viewModel.saveFailure.observe(viewLifecycleOwner, Observer { errorMessage ->
            Toast.makeText(requireContext(), "Error: $errorMessage", Toast.LENGTH_SHORT).show()
        })
    }

}

