package com.febrian.covidapp.info.data

import com.febrian.covidapp.R

object DataExercise {
    fun getExercise() : ArrayList<ModelList>{
        val list : ArrayList<ModelList> = ArrayList()

        list.add(
            ModelList(
            "Deep Breathing While On Your Back",
                " - Lie on your back and bend your knees so that the bottom of your feet are resting on the bed.\n" +
                        " - Place your hands on top of your stomach or wrap them around the sides of your stomach.\n" +
                        " - Close your lips and place your tongue on the roof of your mouth.\n" +
                        " - Breathe in through the nose and pull air down into your stomach where your hands are. Try to spread your fingers apart with your breath.\n" +
                        " - Slowly exhale your breath through the nose.\n" +
                        " - Repeat deep breaths for one minute.",
                R.drawable.on_back
            ))

        list.add(
            ModelList(
            "Deep Breathing While on Your Stomach",
                " - Lie on your stomach and rest your head on your hands to allow room to breathe.\n" +
                        "\n" +
                        " - Close your lips and place your tongue on the roof of your mouth.\n" +
                        "\n" +
                        " - Breathe in through your nose and pull air down into your stomach. Try to focus on your stomach pushing into the mattress as you breathe.\n" +
                        "\n" +
                        " - Slowly exhale your breath through your nose.\n" +
                        "\n" +
                        " - Repeat deep breaths for one minute.\n",
                R.drawable.laying_down
        ))

        list.add(
            ModelList(
            "Deep Breathing While Standing",
                " - Stand upright and place your hands around the sides of your stomach.\n" +
                        "\n" +
                        " - Close your lips and place your tongue on the roof of your mouth.\n" +
                        "\n" +
                        " - Breathe in through your nose and pull air down into your stomach where your hands are. Try to spread your fingers apart with your breath.\n" +
                        "\n" +
                        " - Slowly exhale your breath* through your nose.\n" +
                        "\n" +
                        " - Repeat deep breaths for one minute.\n",
                R.drawable.standing
        ))

        return list
    }
}