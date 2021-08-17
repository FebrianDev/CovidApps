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
            "Contact your healthcare provider or visit your state, tribal, localexternal icon, and territorial health department’s website to find the latest local information on testing. The type of viral COVID-19 tests offered may differ by location."
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
            "PCR tests are used to directly screen for the presence of viral RNA, which will be detectable in the body before antibodies form or symptoms of the disease are present. This means the tests can tell whether or not someone has the virus very early on in their illness.\n" +
                    "\n" +
                    "By scaling PCR testing to screen vast swathes of nasopharyngeal swab samples from within a population, public health officials can get a clearer picture of the spread of a disease like Covid-19.\n" +
                    "\n" +
                    "However, PCR still has its caveats. These types of Covid-19 test need to be sent away to a laboratory for analysis, meaning it can take days for people to find out their results."
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