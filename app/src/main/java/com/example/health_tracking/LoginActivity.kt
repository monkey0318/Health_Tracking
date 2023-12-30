package com.example.health_tracking

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        //FirebaseApp.initializeApp(this)
        setContentView(R.layout.activity_login)
        val emailLogin: EditText = findViewById(R.id.editEmailLoginText)
        val passwordLogin: EditText = findViewById(R.id.editPasswordLoginText)
        val loginBtn: ImageView = findViewById(R.id.login_arrow)
        val registerTextView: TextView = findViewById(R.id.registerTextView)

        loginBtn.setOnClickListener { loginCheckSuccess(it,emailLogin.text.toString(), passwordLogin.text.toString()) }
        registerTextView.setOnClickListener { registerAccount(it) }
    }

    private fun loginCheckSuccess(view: View, email: String, password: String) {
        // auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this)
        // { task: Task<AuthResult> ->
        // if (task.isSuccessful) {
        val context = view.context
        //haven't know where to send
        //   val intent = Intent(context, Register::class.java)
        context.startActivity(intent)
        // }
        //else{
        Toast.makeText(view.context, "Email/Password wrong", Toast.LENGTH_SHORT).show()
        //  }
        //  }
    }


    private fun registerAccount(view: View) {
        val context = view.context
        val intent = Intent(context, RegisterActivity::class.java)
        context.startActivity(intent)
    }
}