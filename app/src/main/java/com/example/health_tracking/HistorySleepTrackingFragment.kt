package com.example.health_tracking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HistorySleepTrackingFragment : Fragment() {

    private lateinit var viewModel: HistorySleepTrackingViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SleepAdapterClass
    private lateinit var backButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_history_sleep_tracking, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backButton = view.findViewById(R.id.backButton)
        recyclerView = view.findViewById(R.id.recyclerView)
        adapter = SleepAdapterClass()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel = ViewModelProvider(this).get(HistorySleepTrackingViewModel::class.java)
        viewModel.sleepData.observe(viewLifecycleOwner, Observer { sleepDataList ->
            // Update the UI with the new data
            adapter.setData(sleepDataList)
        })

        viewModel.getDataFromFirestore()
        val sleepTrackingFragment= SleepTrackingFragment()
        backButton.setOnClickListener {

            val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
            val transaction: FragmentTransaction = fragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentContainer, sleepTrackingFragment)
            transaction.commit()
        }

    }


    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }


}
