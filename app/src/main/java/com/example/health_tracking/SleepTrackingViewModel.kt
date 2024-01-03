package com.example.health_tracking
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class SleepTrackingViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()


    private var recordIdCount = 0

    val saveSuccess = MutableLiveData<Unit>()
    val saveFailure = MutableLiveData<String>()



    fun saveTimeToFirestore(timeMillis: String, ratingSleepTime:Float ) {

        val timeEntry = hashMapOf(
            "id" to generateUniqueId(),
            "title" to String.format("TestingRecordTracking%07d", recordIdCount),
            "time" to timeMillis,
            "date" to getCurrentDate(),
            "ratingSleepTime" to ratingSleepTime,

        )



        db.collection("timesMillis")
            .add(timeEntry)
            .addOnSuccessListener { documentReference ->
                saveSuccess.value = Unit



            }
            .addOnFailureListener { e ->
                saveFailure.value = e.message
            }
    }

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