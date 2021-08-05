package com.febrian.covidapp

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.net.wifi.WifiManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.get
import androidx.webkit.WebSettingsCompat
import com.febrian.covidapp.databinding.FragmentSettingBinding

class SettingFragment : Fragment() {

    private lateinit var binding: FragmentSettingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//            val mode =
//                context?.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)
//            when (mode) {
//                Configuration.UI_MODE_NIGHT_YES -> {
//                    binding.darkmodeActive.isChecked = true
//
//                }
//                Configuration.UI_MODE_NIGHT_NO -> {
//                    binding.darkmodeActive.isChecked = false
//
//                }
//            }


    }

    override fun onResume() {
        super.onResume()

        val sharedPref = view?.context?.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
        val value: String = sharedPref?.getString("KEY", "Follow By System").toString()
        val itemName = setOf(value, "Yes", "No", "Follow By System")
        val list: ArrayList<String> = ArrayList()

        for (i in itemName.indices) {
            list.add(itemName.elementAt(i))
        }

        val adapter =
            view?.let { ArrayAdapter<String>(it.context, android.R.layout.simple_spinner_dropdown_item, list) }
        binding.darkmodeActive.adapter = adapter

        binding.darkmodeActive.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if(sharedPref != null) {
                        when {
                            parent?.getItemAtPosition(position).toString() == "Yes" -> {
                                sharedPref.edit().clear().apply()
                                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                                sharedPref.edit().putString("KEY", "Yes").apply()
                            }
                            parent?.getItemAtPosition(position).toString() == "No" -> {
                                sharedPref.edit().clear().apply()
                                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                                sharedPref.edit().putString("KEY", "No").apply()
                            }
                            parent?.getItemAtPosition(position).toString() == "Follow By System" -> {
                                sharedPref.edit().clear().apply()
                                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                                sharedPref.edit().putString("KEY", "Follow By System").apply()
                            }
                        }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

            }

//        val mode = context?.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)
//        when(mode){
//            Configuration.UI_MODE_NIGHT_YES -> {
//                binding.darkmodeActive.isChecked = true
//            }
//            Configuration.UI_MODE_NIGHT_NO -> {
//                binding.darkmodeActive.isChecked = false
//            }
//        }
    }

}