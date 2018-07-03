package com.marknkamau.globalgym.ui.fragment.gyms

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.marknkamau.globalgym.R
import com.marknkamau.globalgym.data.models.Gym
import kotlinx.android.synthetic.main.item_gym_search.view.*
import java.util.*

/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

class GymSearchAdapter(private val onClick: (Gym) -> Unit) : RecyclerView.Adapter<GymSearchAdapter.ViewHolder>() {

    private var data: List<Gym> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_gym_search, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(data[position], onClick)

    fun setItems(data: List<Gym>) {
        this.data = data
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Gym, onClick: (Gym) -> Unit) = with(itemView) {
            tvGymName.text = item.name
            sceneRoot.setOnClickListener { onClick(item) }
        }
    }
}