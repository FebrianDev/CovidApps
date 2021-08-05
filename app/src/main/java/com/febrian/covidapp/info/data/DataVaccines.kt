package com.febrian.covidapp.info.data

object DataVaccines {
    fun getVaccines() : ArrayList<Model>{
        val list : ArrayList<Model> = ArrayList()

        list.add(
            Model(
            "Is there a vaccine for COVID-19?",
                "Yes. The first mass vaccination programme started in early December 2020 and the number of vaccination doses administered is updated on a daily basis here. At least 13 different vaccines (across 4 platforms) have been administered. Campaigns have started in 206 economies."))

        list.add(Model(
            "Will COVID-19 vaccines provide long-term protection?",
            "Because COVID vaccines have only been developed in the past months, it’s too early to know the duration of protection of COVID-19 vaccines. Research is ongoing to answer this question. However, it’s encouraging that available data suggest that most people who recover from COVID-19 develop an immune response that provides at least some period of protection against reinfection – although we’re still learning how strong this protection is, and how long it lasts."
        ))

        list.add(Model(
            "What are the benefits of getting vaccinated?",
            "The COVID-19 vaccines produce protection against the disease, as a result of developing an immune response to the SARS-Cov-2 virus.  Developing immunity through vaccination means there is a reduced risk of developing the  illness and its consequences. This immunity helps you fight the virus if exposed. Getting vaccinated may also protect people around you, because if you are protected from getting infected and from disease, you are less likely to infect someone else. This is particularly important to protect people at increased risk for severe illness from COVID-19, such as healthcare providers, older or elderly adults, and people with other medical conditions."
        ))

        list.add(Model(
            "Who should get the COVID-19 vaccines?",
            "The COVID-19 vaccines are safe for most people 18 years and older, including those with pre-existing conditions of any kind, including auto-immune disorders. These conditions include: hypertension, diabetes, asthma, pulmonary, liver and kidney disease, as well as chronic infections that are stable and controlled. \n" +
                    "If supplies are limited in your area, discuss your situation with your care provider if you:\n" +
                    " - Have a compromised immune system\n" +
                    " - Are pregnant or nursing your baby\n" +
                    " - Have a history of severe allergies, particularly to a vaccine (or any of the ingredients in the vaccine)\n" +
                    " - Are severely frail "
        ))

        list.add(Model(
            "Can I have the second dose with a different vaccine than the first dose?",
            "Clinical trials in some countries are looking at whether you can have a first dose from one vaccine and a second dose from a different vaccine. There isn't enough data yet to recommend this type of combination. "
        ))

        return list
    }
}