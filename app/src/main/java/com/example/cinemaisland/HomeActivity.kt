package com.example.cinemaisland

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import com.example.cinemaisland.databinding.ActivityHomeBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class HomeActivity : BaseActivity() , OnMapReadyCallback {
    //구글맵 연수언니천재
    var googleMap: GoogleMap? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityHomeBinding.inflate(layoutInflater)
        findViewById<FrameLayout>(R.id.activity_content).addView(binding.root)

        (supportFragmentManager.findFragmentById(R.id.mapView) as
                SupportMapFragment?)!!.getMapAsync(this)    //호출 되면서 아래의 onMapReady()를 호출

    }

    //지도뷰 객체 얻기
    override fun onMapReady(p0: GoogleMap) {
        googleMap = p0

        val latLng = LatLng(37.554884, 126.936148)
        val position = CameraPosition.Builder()
            .target(latLng)
            .zoom(16f)
            .build()
        googleMap?.moveCamera(CameraUpdateFactory.newCameraPosition(position))

        // 마커 옵션
        val markerOptions = MarkerOptions()
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_small))
        markerOptions.position(latLng)
        markerOptions.title("시네마 아일랜드")
        markerOptions.snippet("Tel: 01-111-2222")
        // 마커 표시하기
        googleMap?.addMarker(markerOptions)
        Log.d("ssum", "Google Map is ready. Marker added.")


        googleMap?.setOnMapClickListener { latLng ->
            Log.d("ssum", "click: ${latLng.latitude}, ${latLng.longitude}")
        }
        googleMap?.setOnMapLongClickListener { latLng ->
            Log.d("ssum", "Long click: ${latLng.latitude}, ${latLng.longitude}")
        }
        googleMap?.setOnCameraIdleListener {
            val position = googleMap!!.cameraPosition
            val zoom = position.zoom
            val latitude = position.target.latitude
            val longitude = position.target.longitude
            Log.d("ssum", "user change: $zoom, $latitude, $longitude")
        }
        googleMap?.setOnMarkerClickListener { marker ->
            true
        }
    }

    override fun getLayoutResId(): Int {
        return R.layout.activity_home
    }
}