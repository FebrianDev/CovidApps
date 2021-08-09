package com.febrian.covidapp.info.data

import android.content.Context
import com.febrian.covidapp.R

object DataCovidTest {
    fun getCovidTest(context : Context) : ArrayList<Model>{
        val list = ArrayList<Model>()

        list.add(Model(
            context.resources.getString(R.string.who_should),
            context.resources.getString(R.string.people_who)
        ))

        list.add(Model(
            context.resources.getString(R.string.how_to_get_tested),
            context.resources.getString(R.string.people_who)
        ))

        list.add(Model(
            context.resources.getString(R.string.how_to_use_results),
            context.resources.getString(R.string.if_you)
            ))

        list.add(Model(
            context.resources.getString(R.string.what_are),
            context.resources.getString(R.string.polymerase_chain)
            ))

        list.add(Model(
            context.resources.getString(R.string.what_is_PCR),
            context.resources.getString(R.string.polymerase_chain)
            ))

        list.add(Model(
            context.resources.getString(R.string.how_about_lateral),
            context.resources.getString(R.string.lft)
            ))

        list.add(Model(
            context.resources.getString(R.string.what_is_antibody),
            context.resources.getString(R.string.antibody_tests)
            ))

        return list
    }
}