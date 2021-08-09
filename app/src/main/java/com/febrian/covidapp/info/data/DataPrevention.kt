package com.febrian.covidapp.info.data

import android.content.Context
import com.febrian.covidapp.R

object DataPrevention {
    fun getPrevention(context : Context) : ArrayList<ModelList>{
        val list : ArrayList<ModelList> = ArrayList()
        list.add(
            ModelList(
            "Wash Your Hand",
            "Wet\n" +
                    "Wet your hands with clean, running water (warm or cold), turn off the tap, and apply soap.\n" +
                    "\n" +
                    "Lather\n" +
                    "Lather the backs of your hands, between your fingers, and under your nails.\n" +
                    "\n" +
                    "Scrub\n" +
                    "Scrub your hands for at least 20 seconds. Need a timer? Hum the “Happy Birthday” song twice.\n" +
                    "\n" +
                    "Rinse \n" +
                    "Rinse your hands well under clean, running water.\n" +
                    "\n" +
                    "Dry\n" +
                    "Dry your hands using a clean towel or air dry them.\n",
                R.drawable.wet
        ))

        list.add(ModelList(
            "Use a Mask that…",
            "Have two or more layers of washable, breathable fabric\n" +
                    "\n" +
                    "Completely cover your nose and mouth\n" +
                    "\n" +
                    "Fit snugly against the sides of your face and don’t have gaps\n" +
                    "\n" +
                    "Have a nose wire to prevent air from leaking out of the top of the mask\n" +
                    "\n",
            R.drawable.mask
        ))

        list.add(
            ModelList(
            "Social Distancing",
                "Social distancing is being used to reinforce the need to stay at least 6 feet from others, as well as wearing face masks.\n" +
                        "\n" +
                        "Go out only for necessary activities (shopping, working, etc)\n" +
                        "\n" +
                        "Stay away from crowds\n" +
                        "\n",
                R.drawable.socialdistance
        ))

        return list
    }
}