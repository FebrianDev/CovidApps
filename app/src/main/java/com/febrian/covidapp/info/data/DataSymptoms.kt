package com.febrian.covidapp.info.data

import com.febrian.covidapp.R

object DataSymptoms {
    fun getSymptoms(): ArrayList<ModelList> {
        val list: ArrayList<ModelList> = ArrayList()

        list.add(
            ModelList(
                "Fever",
                "Fever is a common symptom of COVID-19, but it remains unclear exactly how common. A 2020 review in PLOS One with data from almost 25,000 adults found that fever was the most common symptom.\n" +
                        "\n" +
                        "A study in China found that only 44% of people who stayed in the hospital for COVID-19 had fever upon admission. However, just under 89% experienced fever at some point during the hospitalization.\n" +
                        "\n" +
                        "The symptoms of COVID-19 usually develop 2–14 daysTrusted Source after exposure to the SARS-CoV-2 virus\n",
                R.drawable.fever
            )
        )

        list.add(
            ModelList(
                "Dry Cough",
                "60–70% people who develop COVID-19 symptoms experience a dry cough as an initial symptom.\n" +
                        "If you have a dry cough, try to:\n" +
                        "stay hydrated – take small sips of a soft drink, one after the other. Try not to take large sips.\n" +
                        "inhale steam – pour hot water into a bowl, then put your head over the bowl. If comfortable, cover your head and bowl with a towel\n" +
                        "drink a warm drink, such as honey and lemon, as this can soothe your throat\n" +
                        "swallow repeatedly if you don’t have a drink near you. This can work in a similar way to sipping water.\n",
                R.drawable.dry_cough
            )
        )

        list.add(
            ModelList(
                "Fatigue",
                "Being unwell and recovering from an illness may make you feel tired. Fatigue is feeling tired all the time and is not relieved by sleep and rest.\n" +
                        "\n" +
                        "When recovering from COVID-19 (coronavirus), you may feel you need to sleep more or feel exhausted after only taking a short walk.\n" +
                        "\n" +
                        "Even simple things, like washing and dressing, can be exhausting.\n" +
                        "\n" +
                        "While you were ill, your muscles may not have had to do any work for a while. They need time to build their strength again.\n",
                R.drawable.fatigue
            )
        )

        list.add(ModelList(
            "Shortness of Breath",
            "Shortness of breath can make it hard to breathe. It can leave you gasping for air.\n" +
                    "\n" +
                    "Your chest may feel too tight to inhale or exhale fully. Each shallow breath takes greater effort and leaves you feeling winded. It can feel like you’re breathing through a straw.\n" +
                    "\n" +
                    "It may happen when you’re active or resting. It can come on gradually or suddenly.\n" +
                    "\n" +
                    "The Centers for Disease Control and Prevention (CDC)Trusted Source reports that 31 to 40 percent of people with confirmed cases of COVID-19 have experienced shortness of breath.\n",
            R.drawable.hardbreaath
        ))

        list.add(
            ModelList(
            "Loss of Appetite",
                "Losing your appetite is an early sign of COVID-19. For adults over 35, it lasts for an average of four days but can take a week or more to pass. In those younger than 35, it tends to only last two to three days and gets better within a week.\n" +
                        "\n" +
                        "Skipping meals can occur alongside a number of different clusters of symptoms. A loss of appetite alongside fever tends to be a sign of a mild case of COVID-19. Whereas, people with more severe cases will have a loss of appetite alongside confusion, or clustered with shortness of breath, diarrhoea and abdominal pain. \n",
                R.drawable.loss_appetite
        ))

        list.add(
            ModelList(
            "Loss of Smell / Taste",
                "Smell dysfunction is common and often the first symptom of a COVID-19 infection. Therefore, you should self-isolate and get tested for COVID-19 when you can. It is also common in other viral upper respiratory illness, such as the common cold, but rarely is it the only or first symptom in those cases.\n" +
                        "\n" +
                        "In most cases, smell dysfunction recovers quickly. However, it can take months. In a minority of cases, recovery can be incomplete with lasting impairment. While no proven treatment is available, olfactory training is recommended. Topical corticosteroid sprays also are often used in short-term treatment, but they are unlikely to help outside of the acute illness period. Clearly, the best treatment is prevention, such as wearing a mask, practicing social distancing and getting vaccinated for COVID-19.\n",
                R.drawable.loss_smell
        ))

        return list
    }
}