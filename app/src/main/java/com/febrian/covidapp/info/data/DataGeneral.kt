package com.febrian.covidapp.info.data

import android.content.Context
import com.febrian.covidapp.R

object DataGeneral {
    fun getDataGeneral(context : Context): ArrayList<Model> {
        val list: ArrayList<Model> = ArrayList()
        list.add(
            Model(
                context.resources.getString(R.string.what_is_covid),
                context.resources.getString(R.string.covid_is)
            )
        )

        list.add(Model(
            context.resources.getString(R.string.what_happens),
            context.resources.getString(R.string.what_happens_info)
            ))

        list.add(Model(
            context.resources.getString(R.string.are_there_treatments),
            context.resources.getString(R.string.scientists_around)
            ))

        list.add(Model(
            context.resources.getString(R.string.are_there_long),
            context.resources.getString(R.string.some_people_who_have)
            ))

        list.add(Model(
            context.resources.getString(R.string.what_should_i_do),
            context.resources.getString(R.string.if_you_have_been)
        ))

        list.add(Model(
            context.resources.getString(R.string.how_can_we),
            context.resources.getString(R.string.stay_safe)
            ))

        return list
    }

}