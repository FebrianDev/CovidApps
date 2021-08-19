package com.febrian.covidapp.screen.oboarding

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class ViewPagerAdapter(fragmentManager : FragmentManager) : FragmentPagerAdapter(fragmentManager) {

    var position : Int? = 0

    val list = listOf(
        Onboarding1(),
        OnBoarding2(),
        Onboarding3(),
        Onboarding4(),
        Onboarding5()

    )

    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Fragment {
        this.position = position
        return list[position]
    }

    @JvmName("getPosition1")
    fun getPosition() : Int? {
        return position
    }
}