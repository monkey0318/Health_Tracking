package com.example.health_tracking.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.health_tracking.R
import com.example.health_tracking.activityTracking.data.Exercise
import com.example.health_tracking.activityTracking.data.testActivityViewModel
import com.example.health_tracking.databinding.FragmentListBinding
import com.example.health_tracking.activityTracking.util.ActivityAdapter
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase

class ListFragment : Fragment() {

    private lateinit var binding: FragmentListBinding
    private val nav by lazy { findNavController() }
    private val vm: testActivityViewModel by activityViewModels ()

    private lateinit var adapter: ActivityAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListBinding.inflate(inflater, container, false)

        binding.btnInsert.setOnClickListener { nav.navigate(R.id.insertFragment) }
        binding.btnDeleteAll.setOnClickListener { deleteAll() }

        adapter = ActivityAdapter() { holder, exercise ->
            // Item click
            holder.root.setOnClickListener {
                nav.navigate(R.id.updateFragment, bundleOf("index" to exercise.index))
            }
            // Delete button click
            holder.btnDelete.setOnClickListener { delete(exercise.index) }
        }
        binding.rv.adapter = adapter
        binding.rv.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        // TODO: Load data
        Firebase.firestore
            .collection("Exercises")
            .get()
            .addOnSuccessListener { snap ->
                val list = snap.toObjects<Exercise>()
                adapter.submitList(list)
                binding.txtCount.text = "${list.size} exercises"
            }
        vm.getAll().observe(viewLifecycleOwner) {
            adapter.submitList(it)
            binding.txtCount.text = "${it.size} exercise(s)"
        }
        return binding.root
    }

    private fun deleteAll() {
        // TODO: Delete all
        vm.deleteAll()
    }

    private fun delete(id: String) {
        // TODO: Delete
        Firebase.firestore
            .collection("Exercises")
            .document(id)
            .delete()
        vm.delete(id)
    }

}