package com.febrian.covidapp

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.Manifest.permission.ACCESS_WIFI_STATE
import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AppCompatActivity
import com.febrian.covidapp.api.ApiService
import com.febrian.covidapp.databinding.ActivityMapBinding
import com.febrian.covidapp.global.response.Response
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.huawei.hms.maps.*
import com.huawei.hms.maps.model.LatLng
import com.huawei.hms.maps.model.MapStyleOptions
import com.huawei.hms.maps.model.MarkerOptions
import retrofit2.Call
import retrofit2.Callback
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    var hMap: HuaweiMap? = null

    val TAG = "Map Activity"
    private lateinit var darkStyle: MapStyleOptions
    private lateinit var binding: ActivityMapBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val mSupportMapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?

        val searchView = findViewById<androidx.appcompat.widget.SearchView>(R.id.idSearchView)
        darkStyle = MapStyleOptions.loadRawResourceStyle(applicationContext, R.raw.mapstyle_night)


        searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                val location: String = query.toString()

                ApiService.instance.getConfirmed().enqueue(object : Callback<ArrayList<Response>> {
                    override fun onResponse(
                        call: Call<ArrayList<Response>>,
                        response: retrofit2.Response<ArrayList<Response>>
                    ) {
                        if (response.isSuccessful) {
                            val body = response.body()
                            val list = ArrayList<Response>()

                            for (i in 0 until body!!.size) {
                                list.add(body[i])
                            }

                            for (i in 0 until list.size) {
                                if(list[i].combinedKey?.toUpperCase()!!.contains(location.toUpperCase())) {
                                    hMap!!.animateCamera(
                                        CameraUpdateFactory.newLatLngZoom(
                                            LatLng(
                                                list[i].lat!!,
                                                list[i].long!!
                                            ), 9f
                                        )
                                    )
                                }
                            }
                        }
                    }

                    override fun onFailure(call: Call<ArrayList<Response>>, t: Throwable) {
                        Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                    }

                })

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })

        mSupportMapFragment?.getMapAsync(this)
    }

    private fun isDarkModeOn(): Boolean {

        return when (resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_YES -> true
            Configuration.UI_MODE_NIGHT_NO -> false
            Configuration.UI_MODE_NIGHT_UNDEFINED -> false
            else -> false
        }
    }


    @SuppressLint("SupportAnnotationUsage")
    @RequiresPermission(ACCESS_FINE_LOCATION, allOf = arrayOf(ACCESS_WIFI_STATE))
    override fun onMapReady(map: HuaweiMap) {
        Log.d(TAG, "onMapReady: ")
        hMap = map

        if (isDarkModeOn()) map.setMapStyle(darkStyle) else map.setMapStyle(null)

        hMap?.mapType = HuaweiMap.MAP_TYPE_NORMAL

        ApiService.instance.getConfirmed().enqueue(object : Callback<ArrayList<Response>> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(
                call: Call<ArrayList<Response>>,
                response: retrofit2.Response<ArrayList<Response>>
            ) {
                if (response.isSuccessful) {
                    val body = response.body()
                    val list = ArrayList<Response>()

                    for (i in 0 until body!!.size) {
                        list.add(body[i])
                    }

                    for (i in 0 until list.size) {
                        if(list[i].lat != null &&
                            list[i].long != null && list[i].combinedKey !=null) {

                            hMap!!.addMarker(
                                MarkerOptions().position(
                                    LatLng(
                                        list[i].lat!!,
                                        list[i].long!!
                                    )
                                ).title(list[i].combinedKey.toString())

                            )
                        }

                    }

                    hMap!!.setOnMarkerClickListener { p0 ->
                        val bottomSheet = BottomSheetDialog(
                            this@MapActivity,
                            R.style.BottomSheetDialogTheme
                        )
                        val bottomSheetView: View =
                            LayoutInflater.from(applicationContext)
                                .inflate(
                                    R.layout.item_bottom_sheet,
                                    findViewById<LinearLayout>(R.id.bottomSheetContainer)
                                )
                        bottomSheetView.findViewById<TextView>(R.id.text_country).text =
                            p0.title.toString()

                        for (j in 0 until list.size) {
                            if (list[j].combinedKey == p0.title) {
                                val stamp = Timestamp(list[j].lastUpdate!!)
                                val date = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(
                                    Date(stamp.time)
                                )

                                val lastUpdate = applicationContext.resources.getString(R.string.last_update)

                                bottomSheetView.findViewById<TextView>(R.id.last_update).text = "$lastUpdate $date"

                                if(list[j].active == null)
                                    list[j].active = 0
                                if (list[j].confirmed == null)
                                    list[j].confirmed = 0
                                if (list[j].deaths == null)
                                    list[j].deaths = 0
                                if(list[j].recovered == null)
                                    list[j].recovered = 0

                                bottomSheetView.findViewById<TextView>(R.id.text_confirmed).text =
                                    list[j].active.toString()
                                bottomSheetView.findViewById<TextView>(R.id.text_deceased).text =
                                    list[j].deaths.toString()
                                bottomSheetView.findViewById<TextView>(R.id.text_recovered).text =
                                    list[j].recovered.toString()
                                bottomSheetView.findViewById<TextView>(R.id.text_total_case).text =
                                    list[j].confirmed.toString()
                            }
                        }
                        bottomSheet.setContentView(bottomSheetView)
                        bottomSheet.show()
                        true
                    }

                }
            }

            override fun onFailure(call: Call<ArrayList<Response>>, t: Throwable) {
                Log.d("Error", t.message.toString())
                Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
            }
        })

    }
}