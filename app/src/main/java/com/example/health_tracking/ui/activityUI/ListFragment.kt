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
import com.example.health_tracking.data.Friend
import com.example.health_tracking.data.ActivityViewModel
import com.example.health_tracking.databinding.FragmentListBinding
import com.example.health_tracking.util.FriendAdapter
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase

class ListFragment : Fragment() {

    private lateinit var binding: FragmentListBinding
    private val nav by lazy { findNavController() }
    private val vm: ActivityViewModel by activityViewModels()

    private lateinit var adapter: FriendAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListBinding.inflate(inflater, container, false)

        binding.btnInsert.setOnClickListener { nav.navigate(R.id.insertFragment) }
        binding.btnDeleteAll.setOnClickListener { deleteAll() }

        adapter = FriendAdapter() { holder, friend ->
            // Item click
            holder.root.setOnClickListener {
                nav.navigate(R.id.updateFragment, bundleOf("id" to friend.id))
            }
            // Delete button click
            holder.btnDelete.setOnClickListener { delete(friend.id) }
        }
        binding.rv.adapter = adapter
        binding.rv.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        // TODO: Load data
        Firebase.firestore
            .collection("friends")
            .get()
            .addOnSuccessListener { snap ->
                val list = snap.toObjects<Friend>()
                adapter.submitList(list)
                binding.txtCount.text = "${list.size} friends"
            }
        vm.getAll().observe(viewLifecycleOwner) {
            adapter.submitList(it)
            binding.txtCount.text = "${it.size} friend(s)"
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
            .collection("friends")
            .document(id)
            .delete()
        vm.delete(id)
    }

}