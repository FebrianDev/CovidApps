package com.febrian.covidapp.info.data

import android.content.Context
import com.febrian.covidapp.R

object DataVaccines {
    fun getVaccines(context : Context) : ArrayList<Model>{
        val list : ArrayList<Model> = ArrayList()

        list.add(
            Model(
                context.resources.getString(R.string.is_there_a_vaccine),
                context.resources.getString(R.string.yes)
            ))
        list.add(Model(
            context.resources.getString(R.string.will_covid_vaccines),
            context.resources.getString(R.string.because_covid_vaccines)
        ))

        list.add(Model(
            context.resources.getString(R.string.what_are_the_benefits),
            context.resources.getString(R.string.the_covid_vaccines_produce)
            ))

        list.add(Model(
            context.resources.getString(R.string.who_should_get),
            context.resources.getString(R.string.the_covid_vaccines)
        ))

        list.add(Model(
            context.resources.getString(R.string.can_i),
            context.resources.getString(R.string.clinical_trials)
            ))

        return list
    }
}