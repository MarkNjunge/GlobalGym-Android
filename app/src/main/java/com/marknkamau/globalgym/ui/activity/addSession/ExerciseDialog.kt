package com.marknkamau.globalgym.ui.activity.addSession

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import com.marknkamau.globalgym.R
import com.marknkamau.globalgym.data.models.Exercise
import com.marknkamau.globalgym.utils.trimmedText
import android.support.v7.app.AlertDialog
import android.widget.EditText

/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

class ExerciseDialog : DialogFragment() {

    companion object {
        const val EXERCISE_KEY = "exercise_key"
        const val ACTION_SAVE = 0
        const val ACTION_UPDATE = 1
        const val ACTION_DELETE = -1
    }

    lateinit var onComplete: (action: Int, exercise: Exercise) -> Unit
    private lateinit var etExerciseTitle: EditText
    private lateinit var etExerciseReps: EditText
    private lateinit var etExerciseSets: EditText

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = requireActivity().layoutInflater

        val view = inflater.inflate(R.layout.dialog_exercise, null)
        etExerciseTitle = view.findViewById(R.id.etExerciseTitle)
        etExerciseReps = view.findViewById(R.id.etExerciseReps)
        etExerciseSets = view.findViewById(R.id.etExerciseSets)

        val builder = AlertDialog.Builder(requireActivity(), R.style.AlertDialogCustom)
        builder.setView(view)
        builder.setPositiveButton("Save", { _, _ ->
            val exercise = Exercise(0, etExerciseTitle.trimmedText, etExerciseReps.trimmedText, etExerciseSets.trimmedText.toInt())
            onComplete(ACTION_SAVE, exercise)
        })

        arguments?.let {
            val exercise = it.getParcelable(EXERCISE_KEY) as Exercise

            etExerciseTitle.setText(exercise.title)
            etExerciseReps.setText(exercise.reps)
            etExerciseSets.setText(exercise.sets.toString())

            builder.setNegativeButton("Delete", { _, _ ->
                val newExercise = createExercise(exercise.stepIndex)
                onComplete(ACTION_DELETE, newExercise)
            })

            builder.setPositiveButton("Update", { _, _ ->
                val newExercise = createExercise(exercise.stepIndex)
                onComplete(ACTION_UPDATE, newExercise)
            })
        }


        return builder.create()
    }

    private fun createExercise(stepIndex: Int = 0): Exercise {
        return Exercise(stepIndex, etExerciseTitle.trimmedText, etExerciseReps.trimmedText, etExerciseSets.trimmedText.toInt())
    }

}