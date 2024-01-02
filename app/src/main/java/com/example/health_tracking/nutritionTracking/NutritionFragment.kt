package com.example.health_tracking.nutritionTracking

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.example.health_tracking.R
import com.example.health_tracking.databinding.ActivityNutritionTrackingBinding
import com.example.health_tracking.databinding.FragmentNutritionBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [NutritionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NutritionFragment : Fragment() {
    data class NutrientData(
        val protein: Double = 0.0,
        val carbs: Double = 0.0,
        val fats: Double = 0.0
    )
//    private lateinit var proteinEditText: EditText
//    private lateinit var carbsEditText: EditText
//    private lateinit var fatsEditText: EditText
//    private lateinit var totalCaloriesTextView: TextView

    private lateinit var mAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    private lateinit var binding: FragmentNutritionBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNutritionBinding.inflate(inflater, container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view,savedInstanceState)
//        binding = ActivityNutritionTrackingBinding.inflate(layoutInflater)
//        setContentView(R.layout.fragment_nutrition)

//        proteinEditText = findViewById(R.id.proteinEditText)
//        carbsEditText = findViewById(R.id.carbsEditText)
//        fatsEditText = findViewById(R.id.fatsEditText)
//        totalCaloriesTextView = findViewById(R.id.totalCaloriesTextView)

        mAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        binding.calculateButton.setOnClickListener {
            calculateTotalCalories(it)
        }

        binding.loadButton.setOnClickListener {
            loadNutrientValues(it)
        }

        binding.loadHistoryButton.setOnClickListener {
            loadNutrientHistory(it)
        }
    }

     private fun calculateTotalCalories(view: View) {
        val protein = parseEditText(binding.proteinEditText)
        val carbs = parseEditText(binding.carbsEditText)
        val fats = parseEditText(binding.fatsEditText)

        val totalCalories = calculateCalories(protein, carbs, fats)

        binding.totalCaloriesTextView.text = String.format("Total Calories: %.2f", totalCalories)

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
            val nutrientData = hashMapOf(
                "protein" to protein,
                "carbs" to carbs,
                "fats" to fats
            )

            firestore
//                .collection("users")
//                .document(user.uid)
                .collection("nutrientData")
                .document("current")
                .set(nutrientData)
                .addOnSuccessListener {
                    // Saved successfully
                    println("Current nutrient data saved successfully")
                }
                .addOnFailureListener { e ->
                    // Handle the error
                    println("Error saving current nutrient data: ${e.message}")
                }
        }
    }


    private fun saveNutrientHistory(protein: Double, carbs: Double, fats: Double) {
        val user: FirebaseUser? = mAuth.currentUser
        user?.let {
            val nutrientData = hashMapOf(
                "protein" to protein,
                "carbs" to carbs,
                "fats" to fats
            )

            firestore
//                .collection("users")
//                .document(user.uid)
                .collection("nutrientDataHistory")
                .add(nutrientData)
                .addOnSuccessListener {
                    // Saved successfully
                    println("Nutrient data added to history successfully")
                }
                .addOnFailureListener { e ->
                    // Handle the error
                    println("Error adding nutrient data to history: ${e.message}")
                }
        }
    }


     private fun loadNutrientValues(view: View) {
        val user: FirebaseUser? = mAuth.currentUser
        user?.let {
            firestore
//                .collection("users")
//                .document(user.uid)
                .collection("nutrientData")
                .document("current")
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        val nutrientData = documentSnapshot.toObject(NutrientData::class.java)

                        nutrientData?.let {
                            binding.proteinEditText.setText(nutrientData.protein.toString())
                            binding.carbsEditText.setText(nutrientData.carbs.toString())
                            binding.fatsEditText.setText(nutrientData.fats.toString())

                            val totalCalories =
                                calculateCalories(nutrientData.protein, nutrientData.carbs, nutrientData.fats)
                            binding.totalCaloriesTextView.text = String.format("Total Calories: %.2f", totalCalories)
                        }
                    } else {
                        // Handle the case where there is no current nutrient data for the user
                        println("No current nutrient data found for the user")
                    }
                }
                .addOnFailureListener { e ->
                    // Handle errors
                    println("Error loading current nutrient data: ${e.message}")
                }
        }
    }


     private fun loadNutrientHistory(view: View) {
        val user: FirebaseUser? = mAuth.currentUser
        user?.let {
            firestore
//                .collection("users")
//                .document(user.uid)
                .collection("nutrientDataHistory")
                .get()
                .addOnSuccessListener { querySnapshot ->
                    if (!querySnapshot.isEmpty) {
                        // Clear previous history
                        binding.historyTextView.text = ""

                        for (document in querySnapshot.documents) {
                            val nutrientData = document.toObject(NutrientData::class.java)

                            nutrientData?.let {
                                // Log or display each entry in the history
                                println("History entry: $nutrientData")

                                // Append history to TextView (this is just an example, you might want to display it differently)
                                binding.historyTextView.append("Protein: \n${nutrientData.protein}, Carbs: ${nutrientData.carbs}, Fats: ${nutrientData.fats}")
                            }
                        }
                    } else {
                        // Handle the case where there is no history for the user
                        println("No nutrient history found for the user")
                    }
                }
                .addOnFailureListener { e ->
                    // Handle errors
                    println("Error loading nutrient history: ${e.message}")
                }
        }
    }
}