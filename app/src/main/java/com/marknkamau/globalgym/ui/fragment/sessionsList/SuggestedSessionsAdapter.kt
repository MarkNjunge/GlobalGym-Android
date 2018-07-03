package com.marknkamau.globalgym.ui.fragment.sessionsList

import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.github.florent37.expansionpanel.ExpansionLayout
import com.marknkamau.globalgym.R
import com.marknkamau.globalgym.data.models.SuggestedSession
import kotlinx.android.synthetic.main.item_suggested_session.view.*
import java.util.*
import com.github.florent37.expansionpanel.viewgroup.ExpansionLayoutCollection


/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

class SuggestedSessionsAdapter(private val onClick: (SuggestedSession) -> Unit) : RecyclerView.Adapter<SuggestedSessionsAdapter.ViewHolder>() {

    private val viewPool = RecyclerView.RecycledViewPool()
    private val expansionsCollection = ExpansionLayoutCollection()

    init {
        expansionsCollection.openOnlyOne(true)
    }

    private var data: List<SuggestedSession> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(viewPool,
                LayoutInflater.from(parent.context).inflate(R.layout.item_suggested_session, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position], onClick)

        expansionsCollection.add(holder.expansion)
    }

    fun setItems(data: List<SuggestedSession>) {
        this.data = data
        notifyDataSetChanged()
    }

    class ViewHolder(private val viewPool: RecyclerView.RecycledViewPool, itemView: View) : RecyclerView.ViewHolder(itemView) {

        lateinit var expansion: ExpansionLayout

        fun bind(item: SuggestedSession, onClick: (SuggestedSession) -> Unit) = with(itemView) {
            expansion = expansionLayout

            val adapter = SuggestedSessionsExerciseAdapter()
            adapter.setItems(item.exercises)

            rvExercises.layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
            rvExercises.addItemDecoration(DividerItemDecoration(context, LinearLayout.VERTICAL))
//            rvExercises.recycledViewPool = viewPool
            rvExercises.adapter = adapter

            tvSessionName.text = item.name
            btnUseExercise.setOnClickListener { onClick(item) }

        }
    }
}