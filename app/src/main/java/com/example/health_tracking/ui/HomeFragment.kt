package com.example.health_tracking.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.health_tracking.R
import com.example.health_tracking.activityTracking.data.Exercise
import com.example.health_tracking.databinding.FragmentHomeBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val nav by lazy { findNavController() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.btnFriend.setOnClickListener { nav.navigate(R.id.listFragment) }
        binding.btnRead.setOnClickListener { read() }
        binding.btnSet.setOnClickListener { set() }
        binding.btnUpdate.setOnClickListener { update() }
        binding.btnDelete.setOnClickListener { delete() }

        return binding.root
    }

//    private fun read() {
//        // TODO
//        Firebase.firestore
//            .collection("friends")
//            .get()
//            .addOnSuccessListener { snap ->
//                val list = snap.toObjects<Friend>()
//                var result = ""
//                list.forEach { f -> result += "${f.id} ${f.name} ${f.age}\n" }
//                binding.txtResult.text = result
//
//            }

        private fun read() {
            // TODO
            Firebase.firestore
                .collection("Exercises")
                .get()
                .addOnSuccessListener { snap ->
                    val list = snap.toObjects<Exercise>()
                    var result = ""
                    list.forEach { e -> result += "${e.index} ${e.exercise} ${e.calories}\n" }
                    binding.txtResult.text = result

                }
    }

    //Use this when u want to update all fields
    private fun set() {
        // TODO
        val e = Exercise("2", "SampleExercise", 200)

        Firebase.firestore
            .collection("Exercises")
            .document(e.index)
            .set(e)
            .addOnSuccessListener {
                toast("Record inserted!")
            }
    }

    private fun update() {
        // TODO
        Firebase.firestore
            .collection("Exercises")
            .document("2")
            .update("calories",3000)
            .addOnSuccessListener {
                toast("Record updated!")
            }
    }

    private fun delete() {
        // TODO
        Firebase.firestore
            .collection("Exercises")
            .document("2")
            .delete()
            .addOnSuccessListener {
                toast("Record deleted!")
            }

    }

    private fun toast(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

}