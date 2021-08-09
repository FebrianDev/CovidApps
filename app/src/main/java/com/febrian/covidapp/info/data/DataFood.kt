package com.febrian.covidapp.info.data

import android.content.Context
import com.febrian.covidapp.R

object DataFood {
    fun getFood(context : Context) : ArrayList<ModelList>{
        val list : ArrayList<ModelList> = ArrayList()

        list.add(
            ModelList(
            "To Choose",
                " - Consume unsaturated fats (e.g. found in fish, avocado, nuts, olive oil, soy, canola, sunflower and corn oils) rather than saturated fats (e.g. found in fatty meat, butter, coconut oil, cream, cheese, ghee and lard).\n" +
                        " - Choose white meat (e.g. poultry) and fish, which are generally low in fat, rather than red meat.\n" +
                        " - Choose fresh fruits instead of sweet snacks such as cookies, cakes and chocolate.\n" +
                        " - Where possible, opt for low-fat or reduced-fat versions of milk and dairy products.\n",
                R.drawable.tochoose
        ))

        list.add(
            ModelList(
            "To Avoid",
                " - Avoid processed meats because they are high in fat and salt.\n" +
                        " - Avoid industrially produced trans fats. These are often found in processed food, fast food, snack food, fried food, frozen pizza, pies, cookies, margarines and spreads.\n" +
                        " - When cooking and preparing food, limit the amount of salt and high-sodium condiments (e.g. soy sauce and fish sauce).\n" +
                        " - Limit your daily salt intake to less than 5 g (approximately 1 teaspoon), and use iodized salt.\n" +
                        " - Avoid foods (e.g. snacks) that are high in salt and sugar.\n" +
                        " - Limit your intake of soft drinks or sodas and other drinks that are high in sugar (e.g. fruit juices, fruit juice concentrates and syrups, flavoured milks and yogurt drinks).\n" +
                        "\n",
                R.drawable.toavoidrev
        ))

        return list
    }
}