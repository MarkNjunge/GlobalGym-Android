package com.marknkamau.globalgym.ui.gymDetail

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.marknkamau.globalgym.R
import com.marknkamau.globalgym.data.models.Instructor
import com.marknkamau.globalgym.utils.GlideApp
import kotlinx.android.synthetic.main.item_instructor.view.*
import java.util.*

/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

class InstructorsAdapter(private val context: Context, private val onClick: (Instructor) -> Unit) : RecyclerView.Adapter<InstructorsAdapter.ViewHolder>() {

    private var data: List<Instructor> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_instructor, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(context, data[position], onClick)

    fun setItems(data: List<Instructor>) {
        this.data = data
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bind(context: Context, item: Instructor, onClick: (Instructor) -> Unit) = with(itemView) {
            GlideApp.with(context)
                    .load(item.profilePhoto)
                    .into(imgInstructorPhoto)
            tvInstructorName.text = "${item.firstName} ${item.lastName}"
            tvInstructorEmail.text = item.email
            tvInstructorPhone.text = item.phone
        }
    }
}