package com.marknkamau.globalgym.ui.addworkout

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.marknkamau.globalgym.R
import com.marknkamau.globalgym.data.models.Exercise
import com.marknkamau.globalgym.utils.trimmedText
import android.content.DialogInterface
import android.support.v4.app.FragmentManager
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

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = requireActivity().layoutInflater

        val view = inflater.inflate(R.layout.dialog_exercise, null)
        val etExerciseTitle = view.findViewById<EditText>(R.id.etExerciseTitle)
        val etExerciseReps = view.findViewById<EditText>(R.id.etExerciseReps)
        val etExerciseSets = view.findViewById<EditText>(R.id.etExerciseSets)

        val builder = AlertDialog.Builder(requireActivity(), R.style.AlertDialogCustom)
        builder.setView(view)
        builder.setPositiveButton("Save", { _, _ ->
            val exercise = Exercise(0, etExerciseTitle.trimmedText, etExerciseReps.trimmedText, etExerciseSets.trimmedText.toInt())
            onComplete(ACTION_SAVE, exercise)
        })

        arguments?.let {
            builder.setNegativeButton("Delete", { _, _ ->
                val exercise = Exercise(0, etExerciseTitle.trimmedText, etExerciseReps.trimmedText, etExerciseSets.trimmedText.toInt())
                onComplete(ACTION_DELETE, exercise)
            })

            builder.setPositiveButton("Update", { _, _ ->
                val exercise = Exercise(0, etExerciseTitle.trimmedText, etExerciseReps.trimmedText, etExerciseSets.trimmedText.toInt())
                onComplete(ACTION_UPDATE, exercise)
            })

            val exercise = it.getParcelable(EXERCISE_KEY) as Exercise
            etExerciseTitle.setText(exercise.title)
            etExerciseReps.setText(exercise.reps)
            etExerciseSets.setText(exercise.sets.toString())
        }


        return builder.create()
    }

}