package com.febrian.covidapp

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.Manifest.permission.ACCESS_WIFI_STATE
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AppCompatActivity
import com.febrian.covidapp.databinding.ActivityMapBinding
import com.huawei.hms.maps.*
import com.huawei.hms.maps.model.LatLng


class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var hMap: HuaweiMap

    val TAG = "Map Activity"

    private lateinit var binding: ActivityMapBinding
    private lateinit var mMapView: MapView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mMapView = findViewById(R.id.mapview_mapviewdemo)
        mMapView.onCreate(null)
        mMapView.getMapAsync(this)
    }

    companion object {
        private const val MAPVIEW_BUNDLE_KEY = "MapViewBundleKey"
    }

    @SuppressLint("SupportAnnotationUsage")
    @RequiresPermission(ACCESS_FINE_LOCATION, allOf = arrayOf(ACCESS_WIFI_STATE))
    override fun onMapReady(map: HuaweiMap) {
        Log.d(TAG, "onMapReady: ")
        hMap = map
      //  hMap.isMyLocationEnabled = true
      //  hMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(48.893478, 2.334595), 10f))
    }
    //
    override fun onStart() {
        super.onStart()
        mMapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mMapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mMapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        mMapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mMapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mMapView.onLowMemory()
    }
}