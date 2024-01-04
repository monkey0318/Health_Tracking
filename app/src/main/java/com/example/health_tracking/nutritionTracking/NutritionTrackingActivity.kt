package com.example.health_tracking.nutritionTracking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.example.health_tracking.R
import com.example.health_tracking.databinding.ActivityNutritionTrackingBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class NutritionTrackingActivity : AppCompatActivity() {
    data class NutrientData(
        val protein: Double = 0.0,
        val carbs: Double = 0.0,
        val fats: Double = 0.0
    )
//    private lateinit var proteinEditText: EditText
//    private lateinit var carbsEditText: EditText
//    private lateinit var fatsEditText: EditText
//    private lateinit var totalCaloriesTextView: TextView

//    private lateinit var mAuth: FirebaseAuth
//    private lateinit var firestore: FirebaseFirestore
//
//    private lateinit var binding: ActivityNutritionTrackingBinding
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityNutritionTrackingBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
////        proteinEditText = findViewById(R.id.proteinEditText)
////        carbsEditText = findViewById(R.id.carbsEditText)
////        fatsEditText = findViewById(R.id.fatsEditText)
////        totalCaloriesTextView = findViewById(R.id.totalCaloriesTextView)
//
////        mAuth = FirebaseAuth.getInstance()
//        firestore = FirebaseFirestore.getInstance()
//    }
//
//    fun calculateTotalCalories(view: View) {
//        val protein = parseEditText(binding.proteinEditText)
//        val carbs = parseEditText(binding.carbsEditText)
//        val fats = parseEditText(binding.fatsEditText)
//
//        val totalCalories = calculateCalories(protein, carbs, fats)
//
//        binding.totalCaloriesTextView.text = String.format("Total Calories: %.2f", totalCalories)
//
//        saveNutrientValues(protein, carbs, fats)
//        saveNutrientHistory(protein, carbs, fats)
//    }
//
//    private fun parseEditText(editText: EditText): Double {
//        val value = editText.text.toString()
//        return if (value.isEmpty()) 0.0 else value.toDouble()
//    }
//
//    private fun calculateCalories(protein: Double, carbs: Double, fats: Double): Double {
//        val caloriesPerGram = 4.0
//        return (protein + carbs + fats) * caloriesPerGram
//    }
//    private fun saveNutrientValues(protein: Double, carbs: Double, fats: Double) {
//        val nutrientData = hashMapOf(
//            "protein" to protein,
//            "carbs" to carbs,
//            "fats" to fats
//        )
//
//        firestore
//            .collection("nutrientData")
//            .document("current")
//            .set(nutrientData)
//            .addOnSuccessListener {
//                // Saved successfully
//                Log.d("NutrientData", "Current nutrient data saved successfully")
//            }
//            .addOnFailureListener { e ->
//                // Handle the error
//                Log.e("NutrientData", "Error saving current nutrient data: ${e.message}", e)
//            }
//    }
//
//    private fun saveNutrientHistory(protein: Double, carbs: Double, fats: Double) {
//        val nutrientData = hashMapOf(
//            "protein" to protein,
//            "carbs" to carbs,
//            "fats" to fats
//        )
//
//        firestore
//            .collection("nutrientDataHistory")
//            .add(nutrientData)
//            .addOnSuccessListener {
//                // Saved successfully
//                Log.d("NutrientData", "Nutrient data added to history successfully")
//            }
//            .addOnFailureListener { e ->
//                // Handle the error
//                Log.e("NutrientData", "Error adding nutrient data to history: ${e.message}", e)
//            }
//    }
//
//    fun loadNutrientValues(view: View) {
//        firestore
//            .collection("nutrientData")
//            .document("current")
//            .get()
//            .addOnSuccessListener { documentSnapshot ->
//                if (documentSnapshot.exists()) {
//                    val nutrientData = documentSnapshot.toObject(NutrientData::class.java)
//
//                    nutrientData?.let {
//                        binding.proteinEditText.setText(nutrientData.protein.toString())
//                        binding.carbsEditText.setText(nutrientData.carbs.toString())
//                        binding.fatsEditText.setText(nutrientData.fats.toString())
//
//                        val totalCalories =
//                            calculateCalories(nutrientData.protein, nutrientData.carbs, nutrientData.fats)
//                        binding.totalCaloriesTextView.text =
//                            String.format("Total Calories: %.2f", totalCalories)
//
//                        Log.d("NutrientData", "Loaded current nutrient data: $nutrientData")
//                    }
//                } else {
//                    // Handle the case where there is no current nutrient data for the user
//                    Log.d("NutrientData", "No current nutrient data found for the user")
//                }
//            }
//            .addOnFailureListener { e ->
//                // Handle errors
//                Log.e("NutrientData", "Error loading current nutrient data: ${e.message}", e)
//            }
//    }
//
//    fun loadNutrientHistory(view: View) {
//        firestore
//            .collection("nutrientDataHistory")
//            .get()
//            .addOnSuccessListener { querySnapshot ->
//                if (!querySnapshot.isEmpty) {
//                    // Clear previous history
//                    binding.historyTextView.text = ""
//
//                    for (document in querySnapshot.documents) {
//                        val nutrientData = document.toObject(NutrientData::class.java)
//
//                        nutrientData?.let {
//                            // Log or display each entry in the history
//                            Log.d("NutrientData", "History entry: $nutrientData")
//
//                            // Append history to TextView (this is just an example, you might want to display it differently)
//                            binding.historyTextView.append(
//                                "Protein:${nutrientData.protein}, Carbs: ${nutrientData.carbs}, Fats: ${nutrientData.fats}\n"
//                            )
//                        }
//                    }
//                } else {
//                    // Handle the case where there is no history for the user
//                    Log.d("NutrientData", "No nutrient history found for the user")
//                }
//            }
//            .addOnFailureListener { e ->
//                // Handle errors
//                Log.e("NutrientData", "Error loading nutrient history: ${e.message}", e)
//            }
//    }
//    private fun saveNutrientValues(protein: Double, carbs: Double, fats: Double) {
//        val user: FirebaseUser? = mAuth.currentUser
//        user?.let {
//            val nutrientData = hashMapOf(
//                "protein" to protein,
//                "carbs" to carbs,
//                "fats" to fats
//            )
//
//            firestore
//                .collection("nutrientData")
//                .document("current")
//                .set(nutrientData)
//                .addOnSuccessListener {
//                    // Saved successfully
//                    Log.d("NutrientData", "Current nutrient data saved successfully")
//                }
//                .addOnFailureListener { e ->
//                    // Handle the error
//                    Log.e("NutrientData", "Error saving current nutrient data: ${e.message}", e)
//                }
//        }
//    }
//
//    private fun saveNutrientHistory(protein: Double, carbs: Double, fats: Double) {
//        val user: FirebaseUser? = mAuth.currentUser
//        user?.let {
//            val nutrientData = hashMapOf(
//                "protein" to protein,
//                "carbs" to carbs,
//                "fats" to fats
//            )
//
//            firestore
//                .collection("nutrientDataHistory")
//                .add(nutrientData)
//                .addOnSuccessListener {
//                    // Saved successfully
//                    Log.d("NutrientData", "Nutrient data added to history successfully")
//                }
//                .addOnFailureListener { e ->
//                    // Handle the error
//                    Log.e("NutrientData", "Error adding nutrient data to history: ${e.message}", e)
//                }
//        }
//    }
//
//    fun loadNutrientValues(view: View) {
//        val user: FirebaseUser? = mAuth.currentUser
//        user?.let {
//            firestore
//                .collection("nutrientData")
//                .document("current")
//                .get()
//                .addOnSuccessListener { documentSnapshot ->
//                    if (documentSnapshot.exists()) {
//                        val nutrientData = documentSnapshot.toObject(NutrientData::class.java)
//
//                        nutrientData?.let {
//                            binding.proteinEditText.setText(nutrientData.protein.toString())
//                            binding.carbsEditText.setText(nutrientData.carbs.toString())
//                            binding.fatsEditText.setText(nutrientData.fats.toString())
//
//                            val totalCalories =
//                                calculateCalories(nutrientData.protein, nutrientData.carbs, nutrientData.fats)
//                            binding.totalCaloriesTextView.text =
//                                String.format("Total Calories: %.2f", totalCalories)
//
//                            Log.d("NutrientData", "Loaded current nutrient data: $nutrientData")
//                        }
//                    } else {
//                        // Handle the case where there is no current nutrient data for the user
//                        Log.d("NutrientData", "No current nutrient data found for the user")
//                    }
//                }
//                .addOnFailureListener { e ->
//                    // Handle errors
//                    Log.e("NutrientData", "Error loading current nutrient data: ${e.message}", e)
//                }
//        }
//    }
//
//    fun loadNutrientHistory(view: View) {
//        val user: FirebaseUser? = mAuth.currentUser
//        user?.let {
//            firestore
//                .collection("nutrientDataHistory")
//                .get()
//                .addOnSuccessListener { querySnapshot ->
//                    if (!querySnapshot.isEmpty) {
//                        // Clear previous history
//                        binding.totalCaloriesTextView.text = ""
//
//                        for (document in querySnapshot.documents) {
//                            val nutrientData = document.toObject(NutrientData::class.java)
//
//                            nutrientData?.let {
//                                // Log or display each entry in the history
//                                Log.d("NutrientData", "History entry: $nutrientData")
//
//                                // Append history to TextView (this is just an example, you might want to display it differently)
//                                binding.totalCaloriesTextView.append(
//                                    "Protein: \n${nutrientData.protein}, Carbs: ${nutrientData.carbs}, Fats: ${nutrientData.fats}"
//                                )
//                            }
//                        }
//                    } else {
//                        // Handle the case where there is no history for the user
//                        Log.d("NutrientData", "No nutrient history found for the user")
//                    }
//                }
//                .addOnFailureListener { e ->
//                    // Handle errors
//                    Log.e("NutrientData", "Error loading nutrient history: ${e.message}", e)
//                }
//        }
//    }
}