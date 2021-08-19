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
import com.febrian.covidapp.news.utils.SendNotification
import com.huawei.hms.analytics.HiAnalytics
import com.huawei.hms.analytics.HiAnalyticsInstance
import com.huawei.hms.analytics.HiAnalyticsTools
import com.huawei.hms.analytics.type.ReportPolicy
import java.util.*
import kotlin.collections.ArrayList

class SettingFragment : Fragment() {

    private lateinit var binding: FragmentSettingBinding

    private lateinit var c: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)

        c = context
    }

    companion object {
        const val NOTIFICATION = "Notification"
    }

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


        HiAnalyticsTools.enableLog()
        val instance: HiAnalyticsInstance = HiAnalytics.getInstance(c)
        instance.setAnalyticsEnabled(true)
        instance.setUserProfile("userKey", "value")
        instance.setAutoCollectionEnabled(true)
        instance.regHmsSvcEvent()
        val launch: ReportPolicy = ReportPolicy.ON_APP_LAUNCH_POLICY
        val report: MutableSet<ReportPolicy> = HashSet<ReportPolicy>()

        report.add(launch)

        instance.setReportPolicies(report)

        val bundle = Bundle()
        bundle.putString("Setting", "Setting")
        instance.onEvent("Setting", bundle)

        binding.info.setOnClickListener {
            val builder = AlertDialog.Builder(c)
            val l_view = LayoutInflater.from(c).inflate(R.layout.alert_dialog_about, null)
            builder.setView(l_view)

            val dialog = builder.create()
            dialog.show()
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        }

        val sharedPreferences = c.getSharedPreferences(NOTIFICATION, Context.MODE_PRIVATE)
        val check = sharedPreferences.getBoolean(NOTIFICATION, false)
        binding.notificationActive.isChecked = check
        binding.notificationActive.setOnCheckedChangeListener { buttonView, isChecked ->
            sharedPreferences.edit().putBoolean(NOTIFICATION, isChecked).apply()
        }

        if(check) {
            val sendNotification = SendNotification()
            sendNotification.setResources(resources)
            sendNotification.setRepeat(c)
        }
    }

    override fun onResume() {
        super.onResume()

        val sharedPref =
            view?.context?.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
        val value: String = sharedPref?.getString("KEY", "Follow By System").toString()
        val itemName = setOf(value, "Yes", "No", "Follow By System")
        val list: ArrayList<String> = ArrayList()

        for (i in itemName.indices) {
            list.add(itemName.elementAt(i))
        }

        val adapter =
            view?.let {
                ArrayAdapter<String>(
                    it.context,
                    android.R.layout.simple_spinner_dropdown_item,
                    list
                )
            }
        binding.darkmodeActive.adapter = adapter

        binding.darkmodeActive.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (sharedPref != null) {
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
                            parent?.getItemAtPosition(position)
                                .toString() == "Follow By System" -> {
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
    }

}