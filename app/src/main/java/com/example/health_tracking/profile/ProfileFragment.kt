package com.example.health_tracking.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.health_tracking.R
import com.example.health_tracking.databinding.FragmentNutritionBinding
import com.example.health_tracking.databinding.FragmentProfileBinding
import com.google.firebase.firestore.FirebaseFirestore

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {
//    data class User(
//        val name: String,
//        val email: String,
//        val password: String
//    ){
//        constructor() : this("", "", "")
//    }

    private lateinit var db : FirebaseFirestore
    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = FirebaseFirestore.getInstance()

        fetchAndDisplayUserData()

        binding.buttonSave.setOnClickListener{
            saveUser(it)
        }
    }

    private fun fetchAndDisplayUserData() {
        // Fetch user data from Firestore
        db.collection("users")
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot.documents) {
                    val user = document.toObject(ProfileActivity.User::class.java)
                    displayUserData(user)
                }
            }
            .addOnFailureListener { e ->
                showToast("Error fetching user data: ${e.message}")
            }
    }

    private fun displayUserData(user: ProfileActivity.User?) {
        if (user != null) {
            binding.editTextName.setText(user.name)
            binding.editTextEmail.setText(user.email)
            binding.editTextPassword.setText(user.password)

            // Display Firestore data in the added TextView
            val userDataText = "\nName: ${user.name}\nEmail: ${user.email}\nPassword: ${user.password}"
            binding.textViewFirestoreData.text = userDataText
        }
    }

    private fun saveUser(view: View) {
        val name = binding.editTextName.text.toString().trim()
        val email = binding.editTextEmail.text.toString().trim()
        val password = binding.editTextPassword.text.toString().trim()

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showToast("Please enter name, email, and password.")
            return
        }

        val updatedUser = ProfileActivity.User(name, email, password)

        // Update user data in Firestore
        db.collection("users")
            .whereEqualTo("email", email)
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot.documents) {
                    document.reference.set(updatedUser)
                        .addOnSuccessListener {
                            showToast("Profile updated successfully")
                        }
                        .addOnFailureListener { e ->
                            showToast("Error updating profile: ${e.message}")
                        }
                    return@addOnSuccessListener
                }

                // If no matching document found, add a new one
                db.collection("users")
                    .add(updatedUser)
                    .addOnSuccessListener {
                        showToast("Profile updated successfully")
                    }
                    .addOnFailureListener { e ->
                        showToast("Error updating profile: ${e.message}")
                    }
            }
            .addOnFailureListener { e ->
                showToast("Error updating profile: ${e.message}")
            }
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}