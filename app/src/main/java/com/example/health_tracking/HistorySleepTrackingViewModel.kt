package com.example.health_tracking

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore

class HistorySleepTrackingViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    // LiveData to observe sleep data changes
    val sleepData = MutableLiveData<List<SleepDataClass>>()

    // Function to fetch data from Firestore
    fun getDataFromFirestore() {
        db.collection("timesMillis")
            .get()
            .addOnSuccessListener { result ->
                val sleepDataList = result.documents.mapNotNull { document ->
                    document.toObject(SleepDataClass::class.java)
                }
                sleepData.value = sleepDataList
            }
            .addOnFailureListener { e ->

            }
    }


//     Function to delete an item from Firestore

}
