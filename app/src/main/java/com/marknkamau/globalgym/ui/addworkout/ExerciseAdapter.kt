package com.marknkamau.globalgym.ui.addworkout

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.marknkamau.globalgym.R
import com.marknkamau.globalgym.data.models.Exercise
import kotlinx.android.synthetic.main.item_exercise.view.*
import java.util.*

/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

class ExerciseAdapter(private val onEditClick: (Exercise) -> Unit) : RecyclerView.Adapter<ExerciseAdapter.ViewHolder>() {

    private var data: List<Exercise> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_exercise, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(data[position], onEditClick)

    fun setItems(data: List<Exercise>) {
        this.data = data
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Exercise, onEditClick: (Exercise) -> Unit) = with(itemView) {
            tvExerciseTitle.text = item.title
            tvRepsSets.text = "${item.reps} x ${item.sets} sets"

            imgEditExercise.setOnClickListener {
                onEditClick(item)
            }
        }
    }
}