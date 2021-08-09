package com.febrian.covidapp

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
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
import androidx.core.app.ActivityCompat.recreate
import androidx.core.view.get
import androidx.webkit.WebSettingsCompat
import com.febrian.covidapp.databinding.FragmentSettingBinding
import java.util.*
import kotlin.collections.ArrayList

class SettingFragment : Fragment() {

    private lateinit var binding: FragmentSettingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @SuppressLint("ResourceAsColor")
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

        binding.info.setOnClickListener {
            val builder = AlertDialog.Builder(view.context)
            val l_view = LayoutInflater.from(view.context).inflate(R.layout.alert_dialog_about,null)
            builder.setView(l_view)

            val dialog = builder.create()
            dialog.show()
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        }

        val sharedPref = view.context?.getSharedPreferences("Settings", Context.MODE_PRIVATE)
        val lang = sharedPref?.getString("Language", "en")
        val index = sharedPref?.getInt("Index", 0)
        val value = sharedPref?.getString("Value", "English")
        setLocale(lang.toString(), value.toString(), index!!)

        binding.languageValue.text = value
        binding.language.setOnClickListener {

            val index2 = sharedPref.getInt("Index", 0)
            val lang2 = sharedPref?.getString("Language", "en")
            val value2 = sharedPref?.getString("Value", "English")
//            setLocale(lang2.toString(), value2.toString(), index2)
            Log.d("INDEX", index2.toString())

            val list = arrayOf("English", "Indonesia", "Malaysia", "Philippines", "Hindi", "China")
            val builder = AlertDialog.Builder(view.context)
            builder.setTitle("Choose Language...")
            builder.setSingleChoiceItems(list, index2, DialogInterface.OnClickListener { dialog, which ->
                if(which == 0){
                    binding.languageValue.text = "English"
                    setLocale("en", "English", 0)
                }else if(which == 1){
                    binding.languageValue.text = "Indonesia"
                    setLocale("in", "Indonesia", 1)
                }else if(which == 2){
                    binding.languageValue.text = "Malaysia"
                    setLocale("ms", "Malaysia", 2)
                }else if(which == 3){
                    binding.languageValue.text = "Philippines"
                    setLocale("tl", "Philippines", 3)
                }else if(which == 4){
                    binding.languageValue.text = "Hindi"
                    setLocale("th", "Hindi", 4)
                }else if(which == 5){
                    binding.languageValue.text = "China"
                    setLocale("zh", "China", 5)
                }


                dialog.dismiss()
            })

            val dialog = builder.create()
            dialog.window?.setBackgroundDrawableResource(R.color.bgColorPrimary)
            dialog.window?.setTitleColor(R.color.colorPrimary)
            dialog.show()

        }

    }

    @SuppressLint("CommitPrefEdits")
    private fun setLocale(s: String, lang : String, i : Int) {
        val locale : Locale = Locale(s)
        Locale.setDefault(locale)

        val configuration = Configuration()
        configuration.locale = locale
        view?.context?.resources?.updateConfiguration(configuration, context?.resources?.displayMetrics)

        val sharedPref = view?.context?.getSharedPreferences("Settings", Context.MODE_PRIVATE)?.edit()
        sharedPref?.putString("Language", s)
        sharedPref?.putString("Value", lang)
        sharedPref?.putInt("Index", i)
        sharedPref?.apply()
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