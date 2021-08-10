package com.febrian.covidapp.info.data

import android.content.Context
import com.febrian.covidapp.R

object DataFood {
    fun getFood(context : Context) : ArrayList<ModelList>{
        val list : ArrayList<ModelList> = ArrayList()

        list.add(
            ModelList(
                context.resources.getString(R.string.to_choose),
                context.resources.getString(R.string.to_choose_info),
                R.drawable.tochoose
        ))

        list.add(
            ModelList(
                context.resources.getString(R.string.to_avoid),
                context.resources.getString(R.string.to_avoid_info),
                R.drawable.toavoidrev
        ))

        return list
    }
}