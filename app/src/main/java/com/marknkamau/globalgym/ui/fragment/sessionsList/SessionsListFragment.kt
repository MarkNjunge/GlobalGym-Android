package com.marknkamau.globalgym.ui.fragment.sessionsList

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import com.marknkamau.globalgym.App

import com.marknkamau.globalgym.R
import com.marknkamau.globalgym.data.local.SuggestionsProvider
import com.marknkamau.globalgym.data.models.Session
import com.marknkamau.globalgym.ui.activity.addSession.AddSessionActivity
import com.marknkamau.globalgym.ui.activity.sessionDetails.SessionDetailsActivity
import kotlinx.android.synthetic.main.fragment_session.*

class SessionsListFragment : Fragment(), SessionsListView {
    private lateinit var presenter: SessionsListPresenter

    private lateinit var adapter: SessionListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter = SessionsListPresenter(this, App.dataRepository)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_session, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fabAddSession.setOnClickListener {
            startActivity(Intent(requireContext(), AddSessionActivity::class.java))
        }

        adapter = SessionListAdapter { session ->
            val intent = Intent(requireContext(), SessionDetailsActivity::class.java)
            intent.putExtra(SessionDetailsActivity.SESSION_KEY, session)
            startActivity(intent)
        }

        rvSessions.layoutManager = LinearLayoutManager(requireContext(), LinearLayout.VERTICAL, false)
        rvSessions.adapter = adapter

        val suggestedSessionsAdapter = SuggestedSessionsAdapter { suggestedSession ->
            val intent = Intent(requireContext(), AddSessionActivity::class.java)
            intent.putExtra(AddSessionActivity.SUGGESTED_SESSION, suggestedSession)
            startActivity(intent)
        }
        rvSuggestedSessions.layoutManager = LinearLayoutManager(requireContext(), LinearLayout.VERTICAL, false)
        rvSuggestedSessions.adapter = suggestedSessionsAdapter
        suggestedSessionsAdapter.setItems(SuggestionsProvider.get())
    }

    override fun onResume() {
        super.onResume()
        presenter.getSessions()
    }

    override fun onStop() {
        super.onStop()
        presenter.clearDisposables()
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
