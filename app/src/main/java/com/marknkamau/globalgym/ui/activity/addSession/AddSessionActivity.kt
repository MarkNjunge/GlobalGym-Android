package com.marknkamau.globalgym.ui.activity.addSession

import android.app.Activity
import android.os.Bundle
import com.marknkamau.globalgym.R
import kotlinx.android.synthetic.main.activity_add_session.*
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.widget.LinearLayout
import android.widget.Toast
import com.marknkamau.globalgym.App
import com.marknkamau.globalgym.data.models.Exercise
import com.marknkamau.globalgym.data.models.Gym
import com.marknkamau.globalgym.data.models.Session
import com.marknkamau.globalgym.data.models.SuggestedSession
import com.marknkamau.globalgym.ui.activity.BaseActivity
import com.marknkamau.globalgym.ui.activity.selectGym.SelectGymActivity
import com.marknkamau.globalgym.utils.DateTime
import com.marknkamau.globalgym.utils.trimmedText
import java.util.*

class AddSessionActivity : BaseActivity(), AddSessionView {
    private var dateTime = DateTime.now
    private val exercises = mutableListOf<Exercise>()
    private var selectedGym: Gym? = null
    private lateinit var presenter: AddSessionPresenter
    private val LOCATION_REQUEST_ID = 1

    companion object {
        val PREVIOUS_SESSION = "previous_session"
        val PREVIOUS_SESSION_GYM = "previous_session_gym"
        val SUGGESTED_SESSION = "suggested_session"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_session)

        supportActionBar?.run {
            title = getString(R.string.add_session)
        }

        presenter = AddSessionPresenter(this, App.userRepository, App.sessionsRepository)

        val exerciseDialog = ExerciseDialog()

        // Create adapter for recyclerview
        val exerciseAdapter = ExerciseAdapter(this) { exercise ->
            // Listener for edit icon
            // Pass the current exercise as an argument
            val args = Bundle()
            args.putParcelable(ExerciseDialog.EXERCISE_KEY, exercise)
            exerciseDialog.arguments = args
            exerciseDialog.show(supportFragmentManager, "exercise_dialog")
        }

        // Set layout manager and adapter for recyclerview
        rvExercises.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        rvExercises.addItemDecoration(DividerItemDecoration(this, LinearLayout.VERTICAL))
        rvExercises.adapter = exerciseAdapter

        // Set action to be done when the user clicks save on the exercise dialog
        exerciseDialog.onComplete = { action, exercise ->
            when (action) {
                ExerciseDialog.ACTION_SAVE -> {
                    exercises.add(exercise)
                    refreshStepIndices()
                    exerciseAdapter.setItems(exercises)
                }
                ExerciseDialog.ACTION_UPDATE -> {
                    exercises.removeAt(exercise.stepIndex)
                    exercises.add(exercise.stepIndex, exercise)
                    refreshStepIndices()
                    exerciseAdapter.setItems(exercises)
                }
                ExerciseDialog.ACTION_DELETE -> {
                    exercises.remove(exercise)
                    refreshStepIndices()
                    exerciseAdapter.setItems(exercises)
                }
            }
        }

        tvDate.setOnClickListener { showDatePicker() }
        tvTime.setOnClickListener { showTimePicker() }
        btnSave.setOnClickListener { saveSession() }
        btnAddExercise.setOnClickListener {
            exerciseDialog.arguments = null
            exerciseDialog.show(supportFragmentManager, "exercise_dialog")
        }

        // Create an intent to let the user pick a location
        tvLocation.setOnClickListener {
            val i = Intent(this, SelectGymActivity::class.java)
            startActivityForResult(i, LOCATION_REQUEST_ID)
        }

        // Set default date as one day from now
        dateTime.dayOfMonth = dateTime.dayOfMonth + 1
        tvDate.text = dateTime.format(DateTime.APP_DATE_FORMAT)
        tvTime.text = dateTime.format(DateTime.APP_TIME_FORMAT)

        // If a previous session was passed in the intent,
        // set the name, exercises and gym
        val session = intent.getParcelableExtra<Session>(PREVIOUS_SESSION)
        if (session != null) {
            tvSessionName.setText(session.sessionName)
            exerciseAdapter.setItems(session.sessionSteps)
            exercises.addAll(session.sessionSteps)
            refreshStepIndices()
        }
        val gym = intent.getParcelableExtra<Gym>(PREVIOUS_SESSION_GYM)
        if (gym != null){
            selectedGym = gym
            tvLocation.text = gym.name
        }

        // If a suggestion was passed in the intent,
        // set the name, exercises and gym
        val suggested = intent.getParcelableExtra<SuggestedSession>(SUGGESTED_SESSION)
        if (suggested != null) {
            tvSessionName.setText(suggested.name)
            exerciseAdapter.setItems(suggested.exercises)
            exercises.addAll(suggested.exercises)
            refreshStepIndices()
        }
    }

    override fun onStop() {
        super.onStop()
        presenter.clearDisposables()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == LOCATION_REQUEST_ID) {
            val gym = data?.getParcelableExtra<Gym>(SelectGymActivity.SELECTED_GYM)!!
            selectedGym = gym
            tvLocation.text = gym.name
        }
    }

    override fun displayMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onSessionAdded() {
        Toast.makeText(this, "Session added!", Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun showDatePicker() {
        val listener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
            dateTime.year = year
            dateTime.month = month
            dateTime.dayOfMonth = day

            tvDate.text = dateTime.format(DateTime.APP_DATE_FORMAT)
        }

        val year = dateTime.year
        val month = dateTime.month
        val day = dateTime.dayOfMonth

        val datePickerDialog = DatePickerDialog(this, listener, year, month, day)
        datePickerDialog.datePicker.minDate = DateTime.now.timestamp

        datePickerDialog.show()
    }

    private fun showTimePicker() {
        val listener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
            dateTime.hourOfDay = hour
            dateTime.minute = minute

            tvTime.text = dateTime.format(DateTime.APP_TIME_FORMAT)
        }

        val hourOfDay =  dateTime.hourOfDay
        val minute = dateTime.minute

        val timePickerDialog = TimePickerDialog(this, listener, hourOfDay, minute, true)

        timePickerDialog.show()
    }

    private fun saveSession() {
        val sessionName = tvSessionName.trimmedText

        if (sessionName.isNotEmpty() && exercises.isNotEmpty() && selectedGym != null) {
            presenter.addSession(sessionName, dateTime.timestamp, selectedGym!!, exercises)
        }
    }

    private fun refreshStepIndices() {
        for (i in exercises.indices) {
            exercises[i].stepIndex = i
        }
    }

}
