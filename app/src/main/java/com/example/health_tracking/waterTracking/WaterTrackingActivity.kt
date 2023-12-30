package com.example.health_tracking.waterTracking

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.health_tracking.R

class WaterTrackingActivity : AppCompatActivity() {

    lateinit var recordButton: Button
    lateinit var setGoalButton: Button

    lateinit var currentWaterIntakeTextView : TextView
    lateinit var dailyWaterGoalTextView : TextView
    private var currentWaterIntake = 0
    private var dailyWaterGoal = 2000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_water_tracking)

        currentWaterIntake = getSavedWaterIntake()
        dailyWaterGoal = getSavedDailyWaterGoal()
        updateUI()

        recordButton.setOnClickListener {
            showRecordWaterDialog()
        }

        setGoalButton.setOnClickListener {
            showSetGoalDialog()
        }
    }

    private fun showRecordWaterDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Record Water Intake")

        val input = EditText(this)
        input.hint = "Enter water amount in ml"
        input.inputType = InputType.TYPE_CLASS_NUMBER
        builder.setView(input)

        builder.setPositiveButton("Record") { _, _ ->
            val waterAmount = input.text.toString().toIntOrNull() ?: 0
            recordWaterIntake(waterAmount)
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }

    private fun showSetGoalDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Set Daily Water Goal")

        val input = EditText(this)
        input.hint = "Enter daily water goal in ml"
        input.inputType = InputType.TYPE_CLASS_NUMBER
        builder.setView(input)

        builder.setPositiveButton("Set Goal") { _, _ ->
            val goal = input.text.toString().toIntOrNull() ?: 0
            setDailyWaterGoal(goal)
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }

    private fun recordWaterIntake(amount: Int) {
        currentWaterIntake += amount
        saveWaterIntake(currentWaterIntake)
        updateUI()
    }

    private fun setDailyWaterGoal(goal: Int) {
        dailyWaterGoal = goal
        saveDailyWaterGoal(dailyWaterGoal)
        updateUI()
    }

    private fun updateUI() {
        currentWaterIntakeTextView.text = getString(R.string.current_water_intake, currentWaterIntake)
        dailyWaterGoalTextView.text = getString(R.string.daily_water_goal, dailyWaterGoal)
    }

    private fun saveWaterIntake(intake: Int) {
        val preferences = getPreferences(Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putInt("water_intake", intake)
        editor.apply()
    }

    private fun getSavedWaterIntake(): Int {
        val preferences = getPreferences(Context.MODE_PRIVATE)
        return preferences.getInt("water_intake", 0)
    }

    private fun saveDailyWaterGoal(goal: Int) {
        val preferences = getPreferences(Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putInt("daily_water_goal", goal)
        editor.apply()
    }

    private fun getSavedDailyWaterGoal(): Int {
        val preferences = getPreferences(Context.MODE_PRIVATE)
        return preferences.getInt("daily_water_goal", 0)
    }
}