package com.example.health_tracking

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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

    private fun loginCheckSuccess(view: View, email: String, password: String) {
        if (!isValidEmail(email)) {
            Toast.makeText(this, "Invalid Email Format", Toast.LENGTH_SHORT).show()
            return
        }

        if (password.length < 8) {
            Toast.makeText(this, "Password must be at least 8 characters", Toast.LENGTH_SHORT).show()
            return
        }

        db.collection("users")
            .whereEqualTo("email", email)
            .whereEqualTo("password", password)
            .get()
            .addOnSuccessListener { result ->
                if (result.isEmpty) {
                    Toast.makeText(this, "Please fills in the blanks", Toast.LENGTH_SHORT).show()
                } else {
                    // Successfully found a matching user in Firestore
                    Toast.makeText(applicationContext, "Login Successfully", Toast.LENGTH_SHORT).show()

                    val context = view.context
                    val intent = Intent(context, DrawerActivity::class.java)
                    intent.putExtra("email", email)
                    startActivity(intent)
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(applicationContext, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }


    private fun registerAccount(view: View) {
        val context = view.context
        val intent = Intent(context, RegisterActivity::class.java)
        context.startActivity(intent)
    }
}