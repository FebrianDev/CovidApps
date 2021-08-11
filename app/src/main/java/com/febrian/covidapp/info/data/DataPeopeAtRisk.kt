package com.febrian.covidapp.info.data

import android.content.Context
import com.febrian.covidapp.R

object DataPeopeAtRisk {
    fun getPeopleRisk(context : Context) : ArrayList<Model> {
        val list = ArrayList<Model>()

        list.add(Model(
            context.resources.getString(R.string.who_is_at_risk),
            context.resources.getString(R.string.older_people)
            ))

        list.add(Model(
            context.resources.getString(R.string.what_precautions),
            context.resources.getString(R.string.if_you_have_been_in_contract)
        ))

        return list
    }
}