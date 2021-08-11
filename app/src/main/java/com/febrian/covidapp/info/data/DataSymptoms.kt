package com.febrian.covidapp.info.data

import android.content.Context
import com.febrian.covidapp.R

object DataSymptoms {
    fun getSymptoms(context : Context): ArrayList<ModelList> {
        val list: ArrayList<ModelList> = ArrayList()

        list.add(
            ModelList(
                context.resources.getString(R.string.fever),
                context.resources.getString(R.string.fever_info),
                R.drawable.fever
            )
        )

        list.add(
            ModelList(
                context.resources.getString(R.string.dry_cough),
                context.resources.getString(R.string.dry_cough_info),
                R.drawable.dry_cough
            )
        )

        list.add(
            ModelList(
                context.resources.getString(R.string.fatigue),
                context.resources.getString(R.string.fatigue_info),
                R.drawable.fatigue
            )
        )

        list.add(ModelList(
            context.resources.getString(R.string.shortness_of_breath),
            context.resources.getString(R.string.shortness_of_breath_info),
            R.drawable.hardbreaath
        ))

        list.add(
            ModelList(
                context.resources.getString(R.string.loss_of_appetite),
                context.resources.getString(R.string.losing_your_appetite),
                R.drawable.loss_appetite
        ))

        list.add(
            ModelList(
                context.resources.getString(R.string.loss_of),
                context.resources.getString(R.string.smell_dysfunction),
                R.drawable.loss_smell
        ))

        return list
    }
}