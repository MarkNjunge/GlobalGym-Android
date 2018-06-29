package com.marknkamau.globalgym.ui.fragment.sessionsList

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import com.marknkamau.globalgym.App

import com.marknkamau.globalgym.R
import com.marknkamau.globalgym.data.models.Session
import com.marknkamau.globalgym.ui.activity.addworkout.AddSessionActivity
import kotlinx.android.synthetic.main.fragment_workout.*

class SessionsListFragment : Fragment(), SessionsListView {
    private lateinit var presenter: SessionsListPresenter

    private lateinit var adapter: SessionListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter = SessionsListPresenter(this, App.paperService, App.apiService)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_workout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fabAddWorkout.setOnClickListener {
            startActivity(Intent(requireContext(), AddSessionActivity::class.java))
        }

        adapter = SessionListAdapter { }

        rvSessions.layoutManager = LinearLayoutManager(requireContext(), LinearLayout.VERTICAL, false)
        rvSessions.addItemDecoration(DividerItemDecoration(requireContext(), LinearLayout.VERTICAL))
        rvSessions.adapter = adapter

        presenter.getSessions()
    }

    override fun onStop() {
        super.onStop()
        presenter.dispose()
    }

    override fun displayMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun showLoading() {
        pbLoading.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        pbLoading.visibility = View.GONE
    }

    override fun onSessionsRetrieved(sessions: List<Session>) {
        adapter.setItems(sessions)
    }
}
