package com.febrian.covidapp.info.data

import android.content.Context

object DataPeopeAtRisk {
    fun getPeopleRisk(context : Context) : ArrayList<Model> {
        val list = ArrayList<Model>()

        list.add(Model(
            "Who is at risk of developing severe disease?",
            "Older people, and people of all ages with pre-existing medical conditions (such as diabetes, high blood pressure, heart disease, lung disease, or cancer) appear to develop serious illness more often than others."
        ))

        list.add(Model(
            "What precautions are necessary when visiting someone in a health and/or long-term care facility?",
            " - If you have been in contact with someone suspected or confirmed with COVID-19, or are feeling unwell, do not visit any health or long-term care facility.\n" +
                    "\n" +
                    " - Follow the facility guidelines on any visit requirements, including screening and wearing a mask.\n" +
                    "\n" +
                    " - Clean your hands before entering and try to keep at least a 1 metre distance from others.\n" +
                    "\n" +
                    " - If you are 60 or over, or have a chronic condition like heart disease, take extra precautions by wearing a medical mask during your visit."

        ))

        return list
    }
}