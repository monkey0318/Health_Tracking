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
import com.example.health_tracking.nutritionTracking.TestingActivity
import com.example.health_tracking.waterTracking.WaterTrackingActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        db = FirebaseFirestore.getInstance()

        val emailLogin: EditText = findViewById(R.id.editEmailLoginText)
        val passwordLogin: EditText = findViewById(R.id.editPasswordLoginText)
        val loginBtn: ImageView = findViewById(R.id.login_arrow)
        val registerTextView: TextView = findViewById(R.id.registerTextView)

        loginBtn.setOnClickListener { loginCheckSuccess(it,emailLogin.text.toString(), passwordLogin.text.toString()) }
        registerTextView.setOnClickListener { registerAccount(it) }
    }

    private fun loginCheckSuccess(view: View,email: String, password: String) {
        db.collection("users")
            .whereEqualTo("email", email)
            .whereEqualTo("password", password)
            .get()
            .addOnSuccessListener { result ->
                if (result.isEmpty) {
                    Toast.makeText(this, "Email or Password incorrect", Toast.LENGTH_SHORT).show()
                } else {
                    // Successfully found a matching user in Firestore
                    Toast.makeText(applicationContext, "Login Successfully", Toast.LENGTH_SHORT).show()
                    val context = view.context
                    val intent = Intent(context, NavActivity::class.java)
                    startActivity(intent)
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(applicationContext, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()

            }
    }


    private fun registerAccount(view: View) {
        val context = view.context
        val intent = Intent(context, RegisterActivity::class.java)
        context.startActivity(intent)
    }
}