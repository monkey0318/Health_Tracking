package com.example.health_tracking.activityTracking.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.health_tracking.R
import com.example.health_tracking.activityTracking.data.Exercise


class ActivityAdapter (
    val fn: (ViewHolder, Exercise) -> Unit = { _, _ -> }
) : ListAdapter<Exercise, ActivityAdapter.ViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<Exercise>() {
        override fun areItemsTheSame(a: Exercise, b: Exercise)    = a.index == b.index
        override fun areContentsTheSame(a: Exercise, b: Exercise) = a == b
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val root = view
        val imgPhoto : ImageView = view.findViewById(R.id.imgPhoto)
        val txtId    : TextView = view.findViewById(R.id.txtId)
        val txtName  : TextView = view.findViewById(R.id.txtName)
        val txtAge   : TextView = view.findViewById(R.id.txtAge)
        val btnDelete: Button = view.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_exercise, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val exercise = getItem(position)

        holder.txtId.text   = exercise.index
        holder.txtName.text = exercise.exercise
        holder.txtAge.text  = exercise.calories.toString()

        // TODO: Photo (blob to bitmap)
        holder.imgPhoto.setImageBitmap(exercise.photo.toBitmap())

        fn(holder, exercise)
    }

}