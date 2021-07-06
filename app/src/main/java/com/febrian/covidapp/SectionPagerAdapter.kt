package com.febrian.covidapp

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.febrian.covidapp.news.fragment.TerbaruFragment
import com.febrian.covidapp.news.fragment.TrendingFragment

class SectionPagerAdapter(activity : AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when(position){
            0 -> fragment = TerbaruFragment()
            1 -> fragment = TrendingFragment()
            2 -> fragment = TerbaruFragment()
        }

        return fragment as Fragment
    }
}