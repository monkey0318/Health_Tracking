package com.example.health_tracking

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.firestore

class RegisterActivity: AppCompatActivity() {

    lateinit var editNameText: EditText
    lateinit var editEmailText: EditText
    lateinit var editPasswordText: EditText

    private lateinit var mAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val db = Firebase.firestore

        editNameText= findViewById(R.id.editNameText)
        editEmailText= findViewById(R.id.editEmailText)
        editPasswordText= findViewById(R.id.editPasswordText)
        val name = editNameText.text.toString()
        val email = editEmailText.text.toString()
        val password = editPasswordText.text.toString()
        val registerBtn: ImageView = findViewById(R.id.register_arrow)
        val loginTextView: TextView = findViewById(R.id.loginTextView)

        registerBtn.setOnClickListener { checkEmpty(it) }
        loginTextView.setOnClickListener { loginAccount(it) }

        mAuth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference

    }

    private fun checkEmpty(view: View){
        val name = editNameText.text.toString()
        val email = editEmailText.text.toString()
        val password = editPasswordText.text.toString()

        if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
            // Store data
            val user = hashMapOf(
                "name" to name,
                "email" to email,
                "password" to password
            )


            val db = FirebaseFirestore.getInstance()

            // Add a new document to the "users" collection with auto-generated ID
            db.collection("users")
                .add(user)
                .addOnSuccessListener { documentReference ->

                    Toast.makeText(applicationContext, "Register Successfully", Toast.LENGTH_SHORT).show()
                    loginAccount(view)
                }
                .addOnFailureListener { e ->
                    Toast.makeText(applicationContext, "Something went wrong!!", Toast.LENGTH_SHORT).show()


                }

        } else {
            Toast.makeText(this, "Do not leave empty", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loginAccount(view: View) {
        val context = view.context
        val intent = Intent(context, LoginActivity::class.java)
        context.startActivity(intent)
    }
}