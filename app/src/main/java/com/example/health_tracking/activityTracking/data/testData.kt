package com.example.health_tracking.activityTracking.data

import com.google.firebase.firestore.Blob
import com.google.firebase.firestore.DocumentId
import java.util.Date


// TODO: Specify document id
// TODO: Add date and photo
data class Exercise(
    @DocumentId
    var index: String = "",
    var exercise: String = "",
    var calories: Int = 0,
    var date: Date = Date(),
    var photo:Blob = Blob.fromBytes(ByteArray(0)),
)