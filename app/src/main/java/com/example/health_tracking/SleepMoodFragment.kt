package com.example.health_tracking

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

class SleepMoodFragment : Fragment() {


    private lateinit var viewModel: SleepTrackingViewModel
    private lateinit var btnSubmit: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sleep_mood, container, false)
        btnSubmit = view.findViewById(R.id.btnSubmit)
        btnSubmit.setOnClickListener {




            val sleepTrackingFragment = SleepTrackingFragment()
            val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
            val transaction: FragmentTransaction = fragmentManager.beginTransaction()
            transaction.replace(
                R.id.fragmentContainer,
                sleepTrackingFragment
            )
            transaction.commit()
        }

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SleepTrackingViewModel::class.java)
        // TODO: Use the ViewModel
    }

}