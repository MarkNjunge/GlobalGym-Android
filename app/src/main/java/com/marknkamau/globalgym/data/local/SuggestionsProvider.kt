package com.marknkamau.globalgym.data.local

import com.marknkamau.globalgym.data.models.Exercise
import com.marknkamau.globalgym.data.models.SuggestedSession

/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

object SuggestionsProvider {
    fun get(): MutableList<SuggestedSession> {
        return mutableListOf(
                SuggestedSession("Gravity", listOf(
                        Exercise(0, "Push up", "4", 3),
                        Exercise(0, "Wide grip", "4", 3),
                        Exercise(0, "Reverse hands", "4", 3),
                        Exercise(0, "Diamond", "4", 3),
                        Exercise(0, "Stacked feet", "4", 3)
                )),
                SuggestedSession("Abs on Fire", listOf(
                        Exercise(0, "Windshield Vipers", "10", 3),
                        Exercise(0, "Long arm crunches", "10", 3),
                        Exercise(0, "Reverse crunches", "10", 3),
                        Exercise(0, "Bicycle crunches", "10", 3),
                        Exercise(0, "Heel touches", "10", 3)
                )),
                SuggestedSession("Arrow", listOf(
                        Exercise(0, "Box jumps", "20", 3),
                        Exercise(0, "Leg raises", "20", 3),
                        Exercise(0, "Squats", "20", 3),
                        Exercise(0, "Sitting twists", "20", 3),
                        Exercise(0, "Lunges", "20", 3)
                )),
                SuggestedSession("Assassin's", listOf(
                        Exercise(0, "Jumping Ts", "40", 3),
                        Exercise(0, "Box Jumps", "30", 3),
                        Exercise(0, "High knees", "40", 3),
                        Exercise(0, "Wall sit", "30 sec", 3),
                        Exercise(0, "Mountain climbers", "10", 3)
                ))
        )
    }
}