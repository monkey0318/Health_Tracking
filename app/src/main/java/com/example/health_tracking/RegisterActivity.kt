package com.example.health_tracking

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity: AppCompatActivity() {

    lateinit var editNameText: EditText
    lateinit var editEmailText: EditText
    lateinit var editPasswordText: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        //data class User(val name:String, val email: String, val password: String)

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

    }

    private fun checkEmpty(view: View){
        val name = editNameText.text.toString()
        val email = editEmailText.text.toString()
        val password = editPasswordText.text.toString()

        if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
            //store data

            //val user= User(name,email,password)
            //  val database = FirebaseDatabase.getInstance()
            // val usersRef = database.getReference("users")
            // val userId = usersRef.push().key
            // usersRef.child(userId!!).setValue(user)

            loginAccount(view)

        }else {

            Toast.makeText(this, "Do not leave empty", Toast.LENGTH_SHORT).show()
        }

    }

    private fun loginAccount(view: View) {
        val context = view.context
        val intent = Intent(context, LoginActivity::class.java)
        context.startActivity(intent)
    }
}