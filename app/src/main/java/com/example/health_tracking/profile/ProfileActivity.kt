package com.example.health_tracking.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.health_tracking.R
import com.google.firebase.firestore.FirebaseFirestore

class ProfileActivity : AppCompatActivity() {

    data class User(
        val name: String,
        val email: String,
        val password: String
    ){
        constructor() : this("", "", "")
    }

    private lateinit var editTextName: EditText
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var buttonSave: Button
    private lateinit var textViewFirestoreData: TextView
    private val db = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        editTextName = findViewById(R.id.editTextName)
        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
        buttonSave = findViewById(R.id.buttonSave)
        textViewFirestoreData = findViewById(R.id.textViewFirestoreData)

        // Fetch and display user data when this activity starts
        fetchAndDisplayUserData()

        buttonSave.setOnClickListener {
            saveUser()
        }
    }

    private fun fetchAndDisplayUserData() {
        // Fetch user data from Firestore
        db.collection("users")
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot.documents) {
                    val user = document.toObject(User::class.java)
                    displayUserData(user)
                }
            }
            .addOnFailureListener { e ->
                showToast("Error fetching user data: ${e.message}")
            }
    }

    private fun displayUserData(user: User?) {
        if (user != null) {
            editTextName.setText(user.name)
            editTextEmail.setText(user.email)

            // Display Firestore data in the added TextView
            val userDataText = "\nName: ${user.name}\nEmail: ${user.email}"
            textViewFirestoreData.text = userDataText
        }
    }

    private fun saveUser() {
        val name = editTextName.text.toString().trim()
        val email = editTextEmail.text.toString().trim()
        val password = editTextPassword.text.toString().trim()

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showToast("Please enter name, email, and password.")
            return
        }

        val updatedUser = User(name, email, password)

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
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}