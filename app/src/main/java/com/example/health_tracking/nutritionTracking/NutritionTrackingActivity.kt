package com.example.health_tracking.nutritionTracking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
//import com.example.health_tracking.nutritionTracking.databinding.ActivityNutritionTrackingBinding
import com.example.health_tracking.R


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

        addNutritionsButton.setOnClickListener{
            addNutrients()
        }
    }
    private fun addNutrients() {
        val proteinValue = proteinInput.text.toString().toFloatOrNull() ?: 0f
        val carbValue = carbInput.text.toString().toFloatOrNull() ?: 0f
        val fatValue = fatInput.text.toString().toFloatOrNull() ?: 0f

        totalProteinValue += proteinValue
        totalCarbValue += carbValue
        totalFatValue += fatValue

        updateTotalNutrients()
        clearInputFields()
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