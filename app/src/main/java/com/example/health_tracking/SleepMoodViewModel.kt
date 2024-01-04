package com.example.health_tracking

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SleepMoodViewModel:ViewModel() {
    private val db = FirebaseFirestore.getInstance()


    private var recordIdCount = 0

    val saveSuccess = MutableLiveData<Unit>()
    val saveFailure = MutableLiveData<String>()

    fun saveTimeToFirestore(timeMillis: String, ratingSleepTime:Float , moodBeforeRating:Float,  moodAfterRating:Float,disturbanceRating:Float, editTextComment:String) {

        val invertedDisturbanceRating = 5 - disturbanceRating

        val totalRating = (ratingSleepTime + moodBeforeRating + moodAfterRating + invertedDisturbanceRating) / 4.0
        val timeEntry = hashMapOf(
            "id" to generateUniqueId(),
            "title" to String.format("RecordTracking%07d", recordIdCount),
            "time" to timeMillis,
            "date" to getCurrentDate(),
            "ratingSleepTime" to ratingSleepTime,
            "ratingBarMoodBefore" to moodBeforeRating,
            "ratingBarMoodAfter" to moodAfterRating,
            "ratingBarDisturbance" to disturbanceRating,
            "comment" to editTextComment,
            "totalRating" to totalRating

        )

        db.collection("timesMillis")
            .add(timeEntry)
            .addOnSuccessListener { documentReference ->
                saveSuccess.value = Unit
                Log.d("Success", "Success")
            }
            .addOnFailureListener { e ->
                saveFailure.value = e.message
                Log.d("Error", "Error")
            }
    }

    // at this page you need a button to redirect it back to the page
    // if user left swipe means back
    // when back action occur, the activity will destroy
    // the view model will destroy when the correspond activtiy is destroy
    // so, every the view model will recreate again
    // therefore, the id will always 0
    // need to redirect to the activity or fragment to make sure
    // user did swipe left to back and destroy the activtiy and view-model

    private fun generateUniqueId(): String {
        Log.d("Testing", recordIdCount.toString())
        return "T" + String.format("%07d", ++recordIdCount)
    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(Date())
    }
    override fun onCleared() {
        super.onCleared()
        Log.d("Model", "onCleared()")

    }


}