package com.febrian.covidapp.info.data

import android.content.Context

object DataGeneral {
    fun getDataGeneral(context : Context): ArrayList<Model> {
        val list: ArrayList<Model> = ArrayList()
        list.add(
            Model(
                "What is COVID-19?",
                "COVID-19 is the disease caused by a new coronavirus called SARS-CoV-2. " +
                        "WHO first learned of this new virus on 31 December 2019, following a report of a cluster of cases of " +
                        "‘viral pneumonia’ in Wuhan, People’s Republic of China."
            )
        )

        list.add(Model(
            "What happens to people who get " +
                    "COVID-19?",
            "Among those who develop symptoms, most (about 80%) recover from the disease without needing hospital treatment. About 15% become seriously ill and require oxygen and 5% become critically ill and need intensive care."
        ))

        list.add(Model(
            "Are there treatments for " +
                    "COVID-19?",
            "Scientists around the world are working to find and develop treatments for COVID-19.\n" +
                    "\n" +
                    "Optimal supportive care includes oxygen for severely ill patients and those who are at risk for severe disease and more advanced respiratory support such as ventilation for patients who are critically ill.\n" +
                    "\n" +
                    "WHO does not recommend self-medication with any medicines, including antibiotics, as a prevention or cure for COVID-19. WHO is coordinating efforts to develop treatments for COVID-19 and will continue to provide new information as it becomes available."
        ))

        list.add(Model(
            "Are there long-term effects of COVID-19?",
            "Some people who have had COVID-19, whether they have needed hospitalization or not, continue to experience symptoms, including fatigue, respiratory and neurological symptoms.\n" +
                    "\n" +
                    "WHO is working with our Global Technical Network for Clinical Management of COVID-19, researchers and patient groups around the world to design and carry out studies of patients beyond the initial acute course of illness to understand the proportion of patients who have long term effects, how long they persist, and why they occur.  These studies will be used to develop further guidance for patient care.  \n"
        ))

        list.add(Model(
            "What should I do if I have been exposed to someone who has COVID-19?",
            "If you have been exposed to someone with COVID-19, you may become infected, even if you feel well. After exposure to someone who has COVID-19, do the following:\n" +
                    "\n" +
                    "- Call your health care provider or COVID-19 hotline to find out where and when to get a test.\n" +
                    "- Cooperate with contact-tracing procedures to stop the spread of the virus.\n" +
                    "- If testing is not available, stay home and away from others for 14 days.\n" +
                    "- While you are in quarantine, do not go to work, to school or to public places. Ask someone to bring you supplies.\n" +
                    "- Keep at least a 1-metre distance from others, even from your family members.\n" +
                    "- Wear a medical mask to protect others, including if/when you need to seek medical care.\n" +
                    "- Clean your hands frequently.\n" +
                    "- Stay in a separate room from other family members, and if not possible, wear a medical mask.\n" +
                    "- Keep the room well-ventilated.\n" +
                    "- If you share a room, place beds at least 1 metre apart.\n" +
                    "- Monitor yourself for any symptoms for 14 days. \n" +
                    "- Stay positive by keeping in touch with loved ones by phone or online, and by exercising at home."
        ))

        list.add(Model(
            "How can we protect others and ourselves if we don't know who is infected?",
            "Stay safe by taking some simple precautions, such as physical distancing, wearing a mask, especially when distancing cannot be maintained, keeping rooms well ventilated, avoiding crowds and close contact, regularly cleaning your hands, and coughing into a bent elbow or tissue. Check local advice where you live and work. Do it all!"
        ))

        return list
    }

}