package com.example.health_tracking.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.health_tracking.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.auth.User


class ProfileActivity : AppCompatActivity() {
    data class User(val name:String, val email: String, val password: String){
        constructor() : this("","","")
    }
    private lateinit var txtUsername: TextView
    private lateinit var txtEmail: TextView
    private lateinit var txtPassword: TextView
    private lateinit var btnUpdate: Button

    private lateinit var auth: FirebaseAuth
    private lateinit var userRef: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser?.uid
        userRef = FirebaseDatabase.getInstance().getReference("users").child(userId.orEmpty())

        initializeViews()
        fetchUserData()

        btnUpdate.setOnClickListener {
            // Implement update logic here
            val intent = Intent(this@ProfileActivity,UpdateProfileActivity::class.java)
            startActivity(intent)
        }
    }
    private fun initializeViews() {
        txtUsername = findViewById(R.id.txtUsername)
        txtEmail = findViewById(R.id.txtEmail)
        txtPassword = findViewById(R.id.txtPassword)
        btnUpdate = findViewById(R.id.btnUpdate)
    }

    private fun fetchUserData() {
        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val currentUser = snapshot.getValue(User::class.java)
                    currentUser?.let {
                        txtUsername.text = it.name
                        txtEmail.text = it.email
                        txtPassword.text = it.password
                        // Password is not displayed for security reasons
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle errors, if any
            }
        })
    }
}