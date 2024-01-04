package com.example.health_tracking

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.health_tracking.databinding.ActivityRegisterBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

//import com.example.health_tracking.AppDatabase

class RegisterActivity: AppCompatActivity() {

//    lateinit var editNameText: EditText
//    lateinit var editEmailText: EditText
//    lateinit var editPasswordText: EditText

    private lateinit var mAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var binding: ActivityRegisterBinding
//    private lateinit var localDb: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        localDb = Room.databaseBuilder(
//            applicationContext,
//           AppDatabase::class.java, "app_database"
//        ).fallbackToDestructiveMigration().build()


        val db = Firebase.firestore

//        editNameText= findViewById(R.id.editNameText)
//        editEmailText= findViewById(R.id.editEmailText)
//        editPasswordText= findViewById(R.id.editPasswordText)
//        val name = editNameText.text.toString()
//        val email = editEmailText.text.toString()
//        val password = editPasswordText.text.toString()
//        val registerBtn: ImageView = findViewById(R.id.register_arrow)
//        val loginTextView: TextView = findViewById(R.id.loginTextView)

        binding.registerArrow.setOnClickListener { checkEmpty(it) }
        binding.loginTextView.setOnClickListener { loginAccount(it) }

        mAuth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference

    }

    private fun checkEmpty(view: View) {
        val name = binding.editNameText.text.toString()
        val email = binding.editEmailText.text.toString()
        val password = binding.editPasswordText.text.toString()

        if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {

            if (isValidEmail(email)) {

                if (isValidPassword(password)) {

                    if (isValidName(name)) {
                        // Store data
                        val user = hashMapOf(
                            "name" to name,
                            "email" to email,
                            "password" to password
                        )

                        val db = FirebaseFirestore.getInstance()


                        db.collection("users")
                            .add(user)
                            .addOnSuccessListener { documentReference ->
                                Toast.makeText(
                                    applicationContext,
                                    "Register Successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
//                                val localUsers = Users(name = name, email = email, password = password)
//                                localDb.userDao().insertUsers(localUsers)
                                loginAccount(view)

                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(applicationContext, "Something went wrong!!", Toast.LENGTH_SHORT).show()
                            }
                    } else {
                        Toast.makeText(this, "Invalid name format", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Invalid password format (minimum 8 characters)", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidPassword(password: String): Boolean {
        return password.length >= 8
    }

    private fun isValidName(name: String): Boolean {

        return name.isNotEmpty()
    }


    private fun loginAccount(view: View) {
        val context = view.context
        val intent = Intent(context, LoginActivity::class.java)
        context.startActivity(intent)
    }

}