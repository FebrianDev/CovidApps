package com.febrian.covidapp.info.data

import android.content.Context
import com.febrian.covidapp.R

object DataPrevention {
    fun getPrevention(context : Context) : ArrayList<ModelList>{
        val list : ArrayList<ModelList> = ArrayList()
        list.add(
            ModelList(
                context.resources.getString(R.string.wash_your_hand),
                context.resources.getString(R.string.wet),
                R.drawable.wet
        ))

        list.add(ModelList(
            context.resources.getString(R.string.use_a_mask),
            context.resources.getString(R.string.have_two),
            R.drawable.mask
        ))

        list.add(
            ModelList(
                context.resources.getString(R.string.social_distancing),
                context.resources.getString(R.string.social_distancing_is),
                R.drawable.socialdistance
        ))

        return list
    }
}