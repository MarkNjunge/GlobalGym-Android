package com.marknkamau.globalgym.ui.fragment.sessionsList

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.marknkamau.globalgym.R
import com.marknkamau.globalgym.data.models.Session
import com.marknkamau.globalgym.utils.DateTime
import kotlinx.android.synthetic.main.item_session.view.*
import java.util.*

/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

class SessionListAdapter(private val onClick: (Session) -> Unit) : RecyclerView.Adapter<SessionListAdapter.ViewHolder>() {

    private var data: List<Session> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_session, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(data[position], onClick)

    fun setItems(data: List<Session>) {
        this.data = data
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(session: Session, onClick: (Session) -> Unit) = with(itemView) {
            tvSessionName.text = session.sessionName
            tvSessionTime.text = DateTime.fromUnix(session.dateTime).format("${DateTime.APP_TIME_FORMAT}, ${DateTime.APP_DATE_FORMAT}")

            layoutSession.setOnClickListener {
                onClick(session)
            }
        }
    }
}