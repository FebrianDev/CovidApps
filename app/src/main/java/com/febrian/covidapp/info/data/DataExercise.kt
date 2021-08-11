package com.febrian.covidapp.info.data

import android.content.Context
import com.febrian.covidapp.R

object DataExercise {
    fun getExercise(context : Context) : ArrayList<ModelList>{
        val list : ArrayList<ModelList> = ArrayList()

        list.add(
            ModelList(
                context.resources.getString(R.string.deep_breathing_while_on_your_back),
                context.resources.getString(R.string.lie_on_your_back_and_bend_your_knees_so_that_the_bottom_of_your_feet_are_resting_on_the_bed_place_your_hands_on_top_of_your_stomach_or_wrap_them_around_the_sides_of_your_stomach_a_close_your_lips_and_place_your_tongue_on_the_roof_of_your_mouth_breathe_in_through_the_nose_and_pull_air_down_into_your_stomach_where_your_hands_are_try_to_spread_your_fingers_apart_with_your_breath_slowly_exhale_your_breath_through_the_nose_repeat_deep_breaths_for_one_minute),
                R.drawable.on_back
            ))

        list.add(
            ModelList(
                context.resources.getString(R.string.deep_breathing_stomach),
                context.resources.getString(R.string.deep_breathing_stomach_info),
                R.drawable.laying_down
        ))

        list.add(
            ModelList(
                context.resources.getString(R.string.deep_breathing_standing),
                context.resources.getString(R.string.deep_breathing_standing_info),
                R.drawable.standing
        ))

        return list
    }
}