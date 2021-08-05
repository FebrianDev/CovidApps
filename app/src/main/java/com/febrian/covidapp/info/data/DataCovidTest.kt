package com.febrian.covidapp.info.data

object DataCovidTest {
    fun getCovidTest() : ArrayList<Model>{
        val list = ArrayList<Model>()

        list.add(Model(
            "Who should get tested for current infection?",
            " - People who have symptoms of COVID-19.\n" +
                    " - Most people who have had close contact (within 6 feet for a total of 15 minutes or more over a 24-hour period) with someone with confirmed COVID-19.\n" +
                    " - Unvaccinated people who have taken part in activities that put them at higher risk for COVID-19 because they cannot physically distance as needed to avoid exposure, such as travel, attending large social or mass gatherings, or being in crowded or poorly-ventilated indoor settings.\n" +
                    " - People who have been asked or referred to get tested by their healthcare provider, or state, tribal, localexternal icon, or territorial health department."
        ))

        list.add(Model(
            "How to get tested for current COVID-19 infection?",
            "Contact your healthcare provider or visit your state, tribal, localexternal icon, and territorial health department’s website to find the latest local information on testing. The type of viral COVID-19 tests offered may differ by location."
        ))

        list.add(Model(
            "How to use results of viral tests?",
            " - If you test positive, know what protective steps to take to prevent others from getting sick.\n" +
                    " - If you test negative, you probably were not infected at the time your sample was collected. The test result only means that you did not have COVID-19 at the time of testing. Continue to take steps to protect yourself."
        ))

        list.add(Model(
            "What are the different types of Covid-19 test?",
            " - Polymerase chain reaction (PCR) tests are sent away to a lab to diagnose disease\n" +
                    " - Lateral flow tests (LFTs) can diagnose Covid-19 on the spot, but aren’t as accurate as PCR tests\n" +
                    " - Antibody (or serology) tests can’t diagnose active infection, but they can help to tell if a person has immunity to Covid-19"
        ))

        list.add(Model(
            "What is PCR testing?",
            "PCR tests are used to directly screen for the presence of viral RNA, which will be detectable in the body before antibodies form or symptoms of the disease are present. This means the tests can tell whether or not someone has the virus very early on in their illness.\n" +
                    "\n" +
                    "By scaling PCR testing to screen vast swathes of nasopharyngeal swab samples from within a population, public health officials can get a clearer picture of the spread of a disease like Covid-19.\n" +
                    "\n" +
                    "However, PCR still has its caveats. These types of Covid-19 test need to be sent away to a laboratory for analysis, meaning it can take days for people to find out their results."
        ))

        list.add(Model(
            "How about a lateral flow test?",
            "LFTs are similar to PCR tests, in that they’re both types of antigen test, designed to pick up active Covid-19 infection rather than antibodies to the disease. With a Covid-19 LFT, a nasopharyngeal sample is placed on a small absorbent pad, which is then drawn along the pad via a capillary line to a strip coated in antibodies, which bind to SARS-Cov-2 proteins. If these proteins are present, this will show as a coloured line on the test, indicating infection."
        ))

        list.add(Model(
            "What is antibody testing?",
            "Antibody tests are being used to evaluate the immune responses in people who have been vaccinated against Covid-19. Researchers don’t yet know how long vaccine-induced immunity will last or if booster shots will be needed. There has been some indication that Covid-19 variants are making certain vaccines less effective, but thus far they still appear to generally provide enough protection to guard against severe or fatal disease."
        ))

        return list
    }
}