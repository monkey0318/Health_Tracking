package com.example.health_tracking

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class SleepAdapterClass : RecyclerView.Adapter<SleepAdapterClass.ViewHolderClass>()  {
    private var dataList: List<SleepDataClass> = ArrayList()

    inner class ViewHolderClass(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewTitle: TextView = itemView.findViewById(R.id.textViewTitle)
        val ratingBarTotal: RatingBar = itemView.findViewById(R.id.ratingBarTotal)
        val textViewDate: TextView = itemView.findViewById(R.id.textViewDate)
        val textViewId: TextView = itemView.findViewById(R.id.textViewId)
        val textViewTime: TextView = itemView.findViewById(R.id.textViewTime)
        val editTextComment: EditText = itemView.findViewById(R.id.editTextComment)

//


    }

    // Method to set new data and update the UI
    fun setData(newData: List<SleepDataClass>) {
        dataList = newData
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return ViewHolderClass(view)
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val currentItem = dataList[position]
        holder.textViewTitle.text = currentItem.title
        holder.ratingBarTotal.rating = currentItem.totalRating
        holder.textViewDate.text = currentItem.date
        holder.textViewId.text = currentItem.id
        holder.textViewTime.text = currentItem.time
        holder.editTextComment.setText(currentItem.comment)



    }

    override fun getItemCount(): Int {
        return dataList.size
    }


}
