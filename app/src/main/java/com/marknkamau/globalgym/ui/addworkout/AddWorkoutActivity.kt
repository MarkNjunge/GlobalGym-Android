package com.marknkamau.globalgym.ui.addworkout

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.marknkamau.globalgym.R
import kotlinx.android.synthetic.main.activity_add_workout.*
import timber.log.Timber
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.widget.LinearLayout
import com.marknkamau.globalgym.data.models.Exercise
import com.marknkamau.globalgym.utils.DateTime
import java.util.*

class AddWorkoutActivity : AppCompatActivity() {

    private var dateTime = DateTime.now
    private val exercises = mutableListOf<Exercise>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_workout)

        tvDate.text = dateTime.format(DateTime.APP_DATE_FORMAT)
        tvTime.text = dateTime.format(DateTime.APP_TIME_FORMAT)

        val exerciseDialog = ExerciseDialog()

        rvExercises.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        rvExercises.addItemDecoration(DividerItemDecoration(this, LinearLayout.VERTICAL))
        val exerciseAdapter = ExerciseAdapter { exercise ->
            val args = Bundle()
            args.putParcelable(ExerciseDialog.EXERCISE_KEY, exercise)
            exerciseDialog.arguments = args
            exerciseDialog.show(supportFragmentManager, "exercise_dialog")
        }
        rvExercises.adapter = exerciseAdapter
        exerciseAdapter.setItems(exercises)

        exerciseDialog.onComplete = { action, exercise ->
            Timber.d(exercise.toString())

            when (action) {
                ExerciseDialog.ACTION_SAVE -> {
                    exercises.add(exercise)
                    refreshStepIndices()
                    exerciseAdapter.setItems(exercises)
                }
                ExerciseDialog.ACTION_UPDATE -> {
                    // TODO: Figure out why the index is wrong
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

        tvDate.setOnClickListener {
            showDatePicker()
        }

        tvTime.setOnClickListener {
            showTimePicker()
        }

        btnAddExercise.setOnClickListener {
            exerciseDialog.arguments = null
            exerciseDialog.show(supportFragmentManager, "exercise_dialog")
        }
    }

    private fun showDatePicker() {
        val listener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
            dateTime.year = year
            dateTime.month = month
            dateTime.dayOfMonth = day

            Timber.d(dateTime.format("DD:MMM:YYY"))
            tvDate.text = dateTime.format(DateTime.APP_DATE_FORMAT)
        }

        val calendar = GregorianCalendar(Locale.getDefault())
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, listener, year, month, day)
        datePickerDialog.datePicker.minDate = DateTime.now.timestamp

        datePickerDialog.show()
    }

    private fun showTimePicker() {
        val listener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
            dateTime.hourOfDay = hour
            dateTime.minute = minute

            Timber.d(dateTime.format(DateTime.APP_TIME_FORMAT))
            tvTime.text = dateTime.format(DateTime.APP_TIME_FORMAT)
        }

        val calendar = GregorianCalendar(Locale.getDefault())
        val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(this, listener, hourOfDay, minute, true)

        timePickerDialog.show()
    }

    private fun refreshStepIndices() {
        for (i in exercises.indices) {
            exercises[i].stepIndex = i
        }
    }

}
