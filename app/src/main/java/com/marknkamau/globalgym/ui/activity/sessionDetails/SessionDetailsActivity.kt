package com.marknkamau.globalgym.ui.activity.sessionDetails

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import com.marknkamau.globalgym.App
import com.marknkamau.globalgym.R
import com.marknkamau.globalgym.data.models.Gym
import com.marknkamau.globalgym.data.models.Session
import com.marknkamau.globalgym.ui.activity.addSession.AddSessionActivity
import com.marknkamau.globalgym.utils.DateTime
import kotlinx.android.synthetic.main.activity_session_details.*

class SessionDetailsActivity : AppCompatActivity(), SessionDetailsView {
    private lateinit var presenter: SessionDetailsPresenter
    private var gym: Gym? = null

    companion object {
        const val SESSION_KEY = "session_key"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_session_details)

        presenter = SessionDetailsPresenter(this, App.apiService)

        val session = intent.getParcelableExtra<Session>(SESSION_KEY)

        supportActionBar?.run {
            title = session.sessionName
        }

        tvSessionName.text = session.sessionName
        val dateTime = DateTime.fromUnix(session.dateTime)
        tvSessionTime.text = dateTime.format("${DateTime.APP_TIME_FORMAT}, ${DateTime.APP_DATE_FORMAT}")
        if (session.completed) {
            btnCompleted.visibility = View.GONE
            btnRepeatSession.visibility = View.VISIBLE
        }
        btnCompleted.setOnClickListener {
            presenter.setSessionCompleted(session.sessionId)
        }
        btnDelete.setOnClickListener {
            presenter.deleteSession(session.sessionId)
        }
        btnRepeatSession.setOnClickListener {
            val intent = Intent(this, AddSessionActivity::class.java)
            intent.putExtra(AddSessionActivity.PREVIOUS_SESSION, session)
            gym?.let {
                intent.putExtra(AddSessionActivity.PREVIOUS_SESSION_GYM, gym)
            }
            startActivity(intent)
        }

        val adapter = SessionExercisesAdapter()

        rvExercises.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        rvExercises.addItemDecoration(DividerItemDecoration(this, LinearLayout.VERTICAL))
        rvExercises.adapter = adapter

        adapter.setItems(session.sessionSteps)

        presenter.getGym(session.gymId)
    }

    override fun onStop() {
        super.onStop()
        presenter.clearDisposables()
    }

    override fun displayMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    override fun onGymRetrieved(gym: Gym) {
        this.gym = gym
        tvSessionGym.text = gym.name
    }

    override fun onSessionCompleted() {
        btnCompleted.visibility = View.GONE
    }

    override fun onSessionDeleted() {
        displayMessage("Session deleted")
        finish()
    }
}
