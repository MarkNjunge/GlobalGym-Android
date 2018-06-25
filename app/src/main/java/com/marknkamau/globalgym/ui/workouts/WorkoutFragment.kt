package com.marknkamau.globalgym.ui.workouts

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.marknkamau.globalgym.R
import com.marknkamau.globalgym.ui.addworkout.AddWorkoutActivity
import kotlinx.android.synthetic.main.fragment_workout.*

class WorkoutFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_workout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fabAddWorkout.setOnClickListener {
            startActivity(Intent(requireContext(), AddWorkoutActivity::class.java))
        }
    }
}
