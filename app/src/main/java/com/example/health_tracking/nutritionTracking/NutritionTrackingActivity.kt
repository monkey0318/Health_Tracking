package com.example.health_tracking.nutritionTracking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
//import com.example.health_tracking.nutritionTracking.databinding.ActivityNutritionTrackingBinding
import com.example.health_tracking.R
import com.google.firebase.database.*

data class NutrientData(
    val protein: Float = 0f,
    val carb: Float = 0f,
    val fat: Float = 0f
)
class NutritionTrackingActivity : AppCompatActivity() {
//    private lateinit var binding: ActivityNutritionTrackingBinding

    lateinit var carbInput: EditText
    lateinit var proteinInput: EditText
    lateinit var fatInput: EditText

    lateinit var fat_TV: TextView
    lateinit var carb_TV: TextView
    lateinit var protein_TV: TextView
    lateinit var totalProtein: TextView
    lateinit var totalFat: TextView
    lateinit var totalCarb: TextView

    lateinit var addNutritionsButton: Button

    private var totalProteinValue = 0f
    private var totalFatValue = 0f
    private var totalCarbValue = 0f

    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nutrition_tracking)

        proteinInput = findViewById(R.id.proteinInput)
        carbInput = findViewById(R.id.carbInput)
        fatInput = findViewById(R.id.fatInput)

        fat_TV = findViewById(R.id.fat_TV)
        carb_TV = findViewById(R.id.carb_TV)
        protein_TV = findViewById(R.id.protein_TV)
        totalProtein = findViewById(R.id.totalProtein)
        totalFat = findViewById(R.id.totalFat)
        totalCarb = findViewById(R.id.totalCarb)

        addNutritionsButton = findViewById(R.id.addNutrientsButton)

        databaseReference = FirebaseDatabase.getInstance().reference.child("nutrients")

        addNutritionsButton.setOnClickListener{
            addNutrients()
        }

        databaseReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                totalProteinValue = 0f
                totalCarbValue = 0f
                totalFatValue = 0f

                for(dataSnapshot in snapshot.children) {
                    val nutrientData = dataSnapshot.getValue(NutrientData::class.java)
                    nutrientData?.let {
                        totalProteinValue += it.protein
                        totalCarbValue += it.carb
                        totalFatValue += it.fat
                    }
                }

                updateTotalNutrients()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseDatabase","Error reading from database: ${error.message}")
                Toast.makeText(applicationContext,"Error reading from database",Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun addNutrients() {
        val proteinValue = proteinInput.text.toString().toFloatOrNull() ?: 0f
        val carbValue = carbInput.text.toString().toFloatOrNull() ?: 0f
        val fatValue = fatInput.text.toString().toFloatOrNull() ?: 0f

        updateFirebaseDatabase(proteinValue, carbValue, fatValue)

        clearInputFields()
    }

    private fun updateFirebaseDatabase(protein: Float, carb: Float, fat: Float) {
        val nutrientData = NutrientData(protein, carb, fat)
        // Push data to Firebase Realtime Database
        databaseReference.push().setValue(nutrientData)
    }
    private fun updateTotalNutrients() {
        totalProtein.text = "Total Protein: $totalProteinValue g"
        totalCarb.text = "Total Carbs: $totalCarbValue g"
        totalFat.text = "Total Fat: $totalFatValue g"
    }

    private fun clearInputFields() {
        proteinInput.text.clear()
        carbInput.text.clear()
        fatInput.text.clear()
    }
}