package com.example.health_tracking.nutritionTracking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.example.health_tracking.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class TestingActivity : AppCompatActivity() {
    data class NutrientData(
        val protein: Double = 0.0,
        val carbs: Double = 0.0,
        val fats: Double = 0.0
    )
    private lateinit var proteinEditText: EditText
    private lateinit var carbsEditText: EditText
    private lateinit var fatsEditText: EditText
    private lateinit var totalCaloriesTextView: TextView

    private lateinit var mAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_testing)

        proteinEditText = findViewById(R.id.proteinEditText)
        carbsEditText = findViewById(R.id.carbsEditText)
        fatsEditText = findViewById(R.id.fatsEditText)
        totalCaloriesTextView = findViewById(R.id.totalCaloriesTextView)

        mAuth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference
    }

    fun calculateTotalCalories(view: View) {
        val protein = parseEditText(proteinEditText)
        val carbs = parseEditText(carbsEditText)
        val fats = parseEditText(fatsEditText)

        val totalCalories = calculateCalories(protein, carbs, fats)

        totalCaloriesTextView.text = String.format("Total Calories: %.2f", totalCalories)

        saveNutrientValues(protein, carbs, fats)
        saveNutrientHistory(protein, carbs, fats)
    }

    private fun parseEditText(editText: EditText): Double {
        val value = editText.text.toString()
        return if (value.isEmpty()) 0.0 else value.toDouble()
    }

    private fun calculateCalories(protein: Double, carbs: Double, fats: Double): Double {
        val caloriesPerGram = 4.0
        return (protein + carbs + fats) * caloriesPerGram
    }

    private fun saveNutrientValues(protein: Double, carbs: Double, fats: Double) {
        val user: FirebaseUser? = mAuth.currentUser
        user?.let {
            val nutrientData = NutrientData(protein, carbs, fats)
            val userReference: DatabaseReference = databaseReference.child("users").child(user.uid)

            userReference.setValue(nutrientData).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Saved successfully
                    println("Nutrient data saved successfully")
                } else {
                    // Handle the error
                    println("Error saving nutrient data: ${task.exception?.message}")
                }
            }
        }
    }

    private fun saveNutrientHistory(protein: Double, carbs: Double, fats: Double) {
        val user: FirebaseUser? = mAuth.currentUser
        user?.let {
            val nutrientData = NutrientData(protein, carbs, fats)
            val historyReference: DatabaseReference = databaseReference.child("history").child(user.uid).push()

            historyReference.setValue(nutrientData)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Saved successfully
                        println("Nutrient data added to history successfully")
                    } else {
                        // Handle the error
                        println("Error adding nutrient data to history: ${task.exception?.message}")
                    }
                }
        }
    }

    fun loadNutrientValues(view: View) {
        val user: FirebaseUser? = mAuth.currentUser
        user?.let {
            val userReference: DatabaseReference = databaseReference.child("users").child(user.uid)

            userReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        val nutrientData: NutrientData? = dataSnapshot.getValue(NutrientData::class.java)

                        nutrientData?.let {
                            proteinEditText.setText(nutrientData.protein.toString())
                            carbsEditText.setText(nutrientData.carbs.toString())
                            fatsEditText.setText(nutrientData.fats.toString())

                            val totalCalories = calculateCalories(nutrientData.protein, nutrientData.carbs, nutrientData.fats)
                            totalCaloriesTextView.text = String.format("Total Calories: %.2f", totalCalories)
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle errors
                }
            })
        }
    }

    fun loadNutrientHistory(view: View) {
        val user: FirebaseUser? = mAuth.currentUser
        user?.let {
            val historyReference: DatabaseReference = databaseReference.child("history").child(user.uid)

            historyReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Clear previous history
                        totalCaloriesTextView.text = ""

                        for (historySnapshot in dataSnapshot.children) {
                            val nutrientData: NutrientData? = historySnapshot.getValue(NutrientData::class.java)

                            nutrientData?.let {
                                // Log or display each entry in the history
                                println("History entry: $nutrientData")

                                // Append history to TextView (this is just an example, you might want to display it differently)
                                totalCaloriesTextView.append("\n${nutrientData.protein}, ${nutrientData.carbs}, ${nutrientData.fats}")
                            }
                        }
                    } else {
                        // Handle the case where there is no history for the user
                        println("No nutrient history found for the user")
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle errors
                    println("Error loading nutrient history: ${databaseError.message}")
                }
            })
        }
    }
}