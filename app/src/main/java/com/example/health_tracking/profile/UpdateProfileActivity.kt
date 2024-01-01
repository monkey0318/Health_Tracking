package com.example.health_tracking.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.health_tracking.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.auth.User

class UpdateProfileActivity : AppCompatActivity() {
    data class User(val name:String, val email: String, val password: String){
        constructor() : this("","","")
    }private lateinit var auth: FirebaseAuth
    private lateinit var userRef: DatabaseReference

    private lateinit var editUsername: EditText
    private lateinit var editEmail: EditText
    private lateinit var editPassword: EditText
    private lateinit var btnUpdate: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_profile)

        auth = FirebaseAuth.getInstance()
        userRef = FirebaseDatabase.getInstance().getReference("users").child(auth.currentUser!!.uid)

        editUsername = findViewById(R.id.editUsername)
        editEmail = findViewById(R.id.editEmail)
        editPassword = findViewById(R.id.editPassword)
        btnUpdate = findViewById(R.id.btnUpdate)

        // Fetch current user data and populate UI
        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val currentUser = snapshot.getValue(User::class.java)

                // Populate UI elements with current user data
                editUsername.setText(currentUser?.name)
                editEmail.setText(currentUser?.email)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle errors
            }
    })
        btnUpdate.setOnClickListener {
            // Implement update logic here
            val newUsername = editUsername.text.toString()
            val newEmail = editEmail.text.toString()
            val newPassword = editPassword.text.toString()

            // Update profile information in the database
            userRef.child("name").setValue(newUsername)
            userRef.child("email").setValue(newEmail)

            // Update password if provided
            if (newPassword.isNotEmpty()) {
                auth.currentUser?.updatePassword(newPassword)
                    ?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Password updated successfully
                            Toast.makeText(
                                this@UpdateProfileActivity,
                                "Password updated successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            // Handle the error
                            Toast.makeText(
                                this@UpdateProfileActivity,
                                "Failed to update password. ${task.exception?.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }

            // Finish the activity or navigate to another screen if needed
            finish()
        }
    }
}