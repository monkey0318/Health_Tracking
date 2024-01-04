package com.example.health_tracking.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.health_tracking.R
import com.example.health_tracking.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {

    private lateinit var textTitle: TextView
    private lateinit var newsListView: ListView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        textTitle = view.findViewById(R.id.textTitle)
        newsListView = view.findViewById(R.id.newsListView)


        textTitle.text = "Health Tracking News"

        val newsData = arrayOf(
            "Article 1: Important Health Update",
            "Article 2: New Fitness Trends",
            "Article 3: Healthy Eating Habits",
            // Add more dummy news articles as needed
        )

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, newsData)
        newsListView.adapter = adapter


        return view
    }
}