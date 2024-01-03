package com.example.health_tracking.activityTracking.data

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase

class testActivityViewModel : ViewModel() {
    // TODO: Initialization
    private val col = Firebase.firestore.collection("Exercises")
    private val exercises = MutableLiveData<List<Exercise>>()

    init {
        col.addSnapshotListener { snap, _ ->
            exercises.value = snap?.toObjects()
        }
    }

//    suspend fun get(id: String): Friend? {
//        // TODO
//        return col.document(id).get().await().toObject<Friend>()
//    }

    fun get(index: String): Exercise? {
        return exercises.value?.find { e -> e.index == index }
    }

    fun getAll() = exercises

    fun delete(index: String) {
        // TODO
        col.document(index).delete()
    }

    fun deleteAll() {
        // TODO
//        col.get().addOnSuccessListener { snap ->
//            snap.documents.forEach { doc ->
//                delete(doc.id)
//            }
//        }
        exercises.value?.forEach { e -> delete(e.index.toString()) }

    }

    fun set(e: Exercise) {
        // TODO

    }

    //----------------------------------------------------------------------------------------------

    //    private suspend fun idExists(id: String): Boolean {
//        // TODO: Duplicated id?
//        return col.document(id).get().await().exists()
//    }
    private fun idExists(index: String): Boolean {
        // TODO: Duplicated id?
        return exercises.value?.any { e -> e.index == index } ?: false
    }

//    private suspend fun nameExists(name: String): Boolean {
//        // TODO: Duplicated name?
//        return col.whereEqualTo("name", name).get().await().size() > 0
//    }

    private fun nameExists(exercise: String): Boolean {
        // TODO: Duplicated name?
        return exercises.value?.any { e -> e.exercise == exercise } ?: false
    }

    fun validate(e: Exercise, insert: Boolean = true): String {
//        val regexId = Regex("""^[0-9]{2}$""")
        var n = ""

        if (insert) {
            n += if (e.index == "") "- Index is required.\n"
//            else if (!e.index.matches(regexId)) "- Index format is invalid.\n"
            else if (idExists(e.index)) "- Index is duplicated.\n"
            else ""
        }

        n += if (e.exercise == "") "- Exercise is required.\n"
//        else if (e.exercise.length < 3) "- Exercise is too short.\n"
        // else if (nameExists(f.name)) "- Name is duplicated.\n"
        else ""

        n += if (e.calories == 0) "- calories is required.\n"
//        else if (e.calories < 18) "- Underage.\n"
        else ""

        n += if (e.photo.toBytes().isEmpty()) "- Photo is required.\n"
        else ""

        return n
    }
}