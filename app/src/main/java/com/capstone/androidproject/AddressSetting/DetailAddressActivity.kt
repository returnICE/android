package com.capstone.androidproject.AddressSetting

import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.capstone.androidproject.R
import com.google.android.gms.maps.*
import android.content.pm.PackageManager
import android.Manifest.permission.ACCESS_COARSE_LOCATION
import androidx.core.app.ActivityCompat
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.DialogInterface
import android.content.Intent
import android.location.LocationManager
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.CameraUpdateFactory
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.capstone.androidproject.Fragment.HomeFragment
import com.capstone.androidproject.MainActivity
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.maps.model.*
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_detail_address.*
import org.jetbrains.anko.startActivity
import java.io.IOException
import java.util.*


class DetailAddressActivity : AppCompatActivity(), OnMapReadyCallback,
    ActivityCompat.OnRequestPermissionsResultCallback{

    val TAG="Address"

    var mGoogleMap: GoogleMap? = null
    var currentMarker:Marker? = null

    var DEFAULT_LOCATION = LatLng(37.279, 127.043)

    private val PERMISSIONS_REQUEST_CODE = 100
    private val GPS_ENABLE_REQUEST_CODE = 2001
    val REQUIRED_PERMISSIONS:Array<String> = arrayOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION)

    var mMoveMapByUser = true
    var mMoveMapByAPI = true
    var needRequest = false

    var currentPosition: LatLng? = null

    lateinit var location:Location
    lateinit var mCurrentLocatiion:Location
    lateinit var locationRequest: LocationRequest
    private lateinit var mfusedLocationClient: FusedLocationProviderClient

    lateinit var mLayout: View
    lateinit var imm: InputMethodManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        setContentView(R.layout.activity_detail_address)

        mLayout = findViewById(R.id.layout_map)
        imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager

        btnSelectAddress.setOnClickListener {
            textDetail.visibility = View.VISIBLE
            pickAddress.visibility = View.VISIBLE
            textDetail.requestFocus()
            imm.showSoftInput(textDetail,0)
        }

        btnBackArrow.setOnClickListener {
            onBackPressed()
        }

        pickAddress.setOnClickListener {
            val result:String = textAddress.text.toString() + textDetail.text.toString()

            startActivity<MainActivity>("myAddress" to result)
            finishAffinity()
        }

        locationRequest = LocationRequest()
            .setPriority(PRIORITY_HIGH_ACCURACY)
            .setNumUpdates(1)

        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(locationRequest)

        mfusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mGoogleMap = googleMap

        //지도의 초기위치로 이동
        setDefaultLocation()

        //위치 퍼미션을 가지고 있는지 체크
        val hasFineLocationPermission = ContextCompat.checkSelfPermission(this,
                ACCESS_FINE_LOCATION)
        val hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this,
                ACCESS_COARSE_LOCATION)

        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
            hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED   ) {
            startLocationUpdates() // 위치 업데이트 시작
        }else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])) {

                Snackbar.make(mLayout, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.",
                    Snackbar.LENGTH_INDEFINITE).setAction("확인",object:View.OnClickListener {
                        override fun onClick(view:View) {
                        ActivityCompat.requestPermissions( this@DetailAddressActivity, REQUIRED_PERMISSIONS,
                            PERMISSIONS_REQUEST_CODE)
                    }
                }).show()
            } else {
                ActivityCompat.requestPermissions( this, REQUIRED_PERMISSIONS,
                    PERMISSIONS_REQUEST_CODE)
            }
        }
        mGoogleMap?.getUiSettings()!!.isMyLocationButtonEnabled = true
        mGoogleMap?.animateCamera(CameraUpdateFactory.zoomTo(15f))

        mGoogleMap?.setOnMyLocationButtonClickListener(object:GoogleMap.OnMyLocationButtonClickListener { // 현재 위치 찾아가기
            override fun onMyLocationButtonClick(): Boolean {
                setDefaultLocation()
                return true
            }
        })

        mGoogleMap?.setOnCameraMoveStartedListener(object:GoogleMap.OnCameraMoveStartedListener {
            override fun onCameraMoveStarted(p0: Int) {
                mMoveMapByUser = false
                mMoveMapByAPI = false
                textDetail.setText("")
                textDetail.visibility = View.INVISIBLE
                pickAddress.visibility = View.INVISIBLE
                imm.hideSoftInputFromWindow(textDetail.windowToken,0)
                Log.d(TAG, "Start")
            }
        })

        mGoogleMap?.setOnCameraMoveListener(object:GoogleMap.OnCameraMoveListener {
            override fun onCameraMove() {
                if(mMoveMapByAPI == true) {
                    mMoveMapByUser = false

                    customMarker.visibility = View.GONE
                }
                else {
                    mMoveMapByUser = true

                    currentMarker?.remove()
                    customMarker.visibility = View.VISIBLE
                }
                Log.d(TAG,"Move")
            }
        })
        mGoogleMap?.setOnCameraIdleListener(object:GoogleMap.OnCameraIdleListener{
            override fun onCameraIdle() {
                if(mMoveMapByUser == true ) {
                    Log.d(TAG, "onCameraIdle")
                    val location_center = mGoogleMap?.cameraPosition!!.target

                    currentPosition = location_center

                    val location = Location("currentLoc")
                    location.latitude = location_center!!.latitude
                    location.longitude = location_center.longitude

                    val markerTitle = getCurrentAddress(currentPosition!!)

                    setCurrentLocation(location, markerTitle)

                    Log.d(TAG, "Idle")
                }
            }
        })
    }

    var locationCallback:LocationCallback = object:LocationCallback() {
        override fun onLocationResult(locationResult:LocationResult) {
            super.onLocationResult(locationResult)
            val locationList = locationResult.getLocations()
            if (locationList.size > 0)
            {
                location = locationList.get(locationList.size - 1)
                //location = locationList.get(0);
                currentPosition = LatLng(location.getLatitude(), location.getLongitude())
                val markerTitle = getCurrentAddress(currentPosition!!)
                //현재 위치에 마커 생성하고 이동
                setCurrentLocation(location, markerTitle)
                mCurrentLocatiion = location
            }
        }
    }

    private fun startLocationUpdates() {
        if (!checkLocationServicesStatus()) {
            showDialogForLocationServiceSetting()
        } else {
            val hasFineLocationPermission = ContextCompat.checkSelfPermission(this,
                    ACCESS_FINE_LOCATION)
            val hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this,
                    ACCESS_COARSE_LOCATION)

            if (hasFineLocationPermission != PackageManager.PERMISSION_GRANTED ||
                    hasCoarseLocationPermission != PackageManager.PERMISSION_GRANTED   ) {

                Log.d(TAG, "startLocationUpdates : 퍼미션 안가지고 있음")
                return
            }

            mfusedLocationClient.lastLocation
                .addOnSuccessListener { location : Location? ->
                    // Got last known location. In some rare situations this can be null.
                    if(location != null) {
                        DEFAULT_LOCATION = LatLng(location.latitude,location.longitude)
                    }
                }

            mfusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)

            if (checkPermission())
                mGoogleMap?.setMyLocationEnabled(true)
        }
    }

    override fun onStart() {
        super.onStart()
        if (checkPermission()) {
            mfusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)

            if (mGoogleMap!=null)
                mGoogleMap?.setMyLocationEnabled(true)
        }
    }
    override fun onStop() {
        super.onStop()

        mfusedLocationClient.removeLocationUpdates(locationCallback)
    }
    override fun onPause() {
        super.onPause()
        mfusedLocationClient.removeLocationUpdates(locationCallback)
    }

    fun getCurrentAddress(latlng: LatLng): String{
        val geocoder = Geocoder(this, Locale.getDefault())
        lateinit var addresses: List<Address>
        try {
            addresses = geocoder.getFromLocation(
                latlng.latitude,
                latlng.longitude,
                1)
        } catch (ioException: IOException) {
            //네트워크 문제
            Log.d(TAG,ioException.toString())
            Toast.makeText(this, "지오코더 서비스 사용불가 - 네트워크 에러", Toast.LENGTH_LONG).show()
            return "지오코더 서비스 사용불가"
        } catch (illegalArgumentException: IllegalArgumentException) {
            Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show()
            return "잘못된 GPS 좌표"
        }

        if (addresses == null || addresses.size == 0) {
            Toast.makeText(this, "주소 미발견"+latlng.latitude.toString()+"   "+latlng.longitude.toString(), Toast.LENGTH_LONG).show()
            return "주소 미발견"
        } else {
            val address = addresses.get(0).getAddressLine(0).toString().split(" ")
            var str = " "
            for((i,s) in address.withIndex()){
                if(i>1){
                    str+=s+" "
                }
            }
            return str
        }
    }

    fun checkLocationServicesStatus():Boolean {
        val locationManager:LocationManager = getSystemService(LOCATION_SERVICE) as LocationManager

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    fun setCurrentLocation(location:Location, address:String) {
        if (currentMarker != null)
            currentMarker?.remove()

        customMarker.visibility = View.GONE

        val currentLatLng = LatLng(location.getLatitude(), location.getLongitude())
        val markerOptions = MarkerOptions()
        markerOptions.position(currentLatLng)
        markerOptions.draggable(true)
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
        currentMarker = mGoogleMap?.addMarker(markerOptions)

        textAddress.setText(address)

        val cameraUpdate = CameraUpdateFactory.newLatLng(currentLatLng)
        mGoogleMap?.moveCamera(cameraUpdate)
        mMoveMapByAPI = true
    }

    fun setDefaultLocation() {
        //디폴트 위치, 마지막 위치
        val address = getCurrentAddress(DEFAULT_LOCATION)

        if (currentMarker != null)
            currentMarker?.remove()

        customMarker.visibility = View.GONE

        val markerOptions = MarkerOptions()
        markerOptions.position(DEFAULT_LOCATION)
        markerOptions.draggable(true)
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
        currentMarker = mGoogleMap?.addMarker(markerOptions)

        textAddress.setText(address)

        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(DEFAULT_LOCATION, 15f)
        mGoogleMap?.moveCamera(cameraUpdate)
    }

    private fun checkPermission():Boolean  {
        val hasFineLocationPermission = ContextCompat.checkSelfPermission(this,
                ACCESS_FINE_LOCATION)
        val hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this,
                ACCESS_COARSE_LOCATION)

        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED   ) {
            return true
        }
        return false
    }

    override fun onRequestPermissionsResult(permsRequestCode:Int,
                                            @NonNull permissions:Array<String>,
                                            @NonNull grantResults:IntArray) {
        if (((permsRequestCode == PERMISSIONS_REQUEST_CODE ) && grantResults.size == REQUIRED_PERMISSIONS.size)) {
            var check_result = true
            for (result in grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false
                    break
                }
            }
            if (check_result) {
                startLocationUpdates()
            } else {
                if ((ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        REQUIRED_PERMISSIONS[0]
                    ) || ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        REQUIRED_PERMISSIONS[1]
                    ))
                ) {
                    Snackbar.make(
                        mLayout, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요. ",
                        Snackbar.LENGTH_INDEFINITE
                    ).setAction("확인", object: View.OnClickListener {
                        override fun onClick(view: View) {
                            finish()
                        }
                    }).show()
                } else {
                    Snackbar.make(
                        mLayout, "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ",
                        Snackbar.LENGTH_INDEFINITE
                    ).setAction("확인", object: View.OnClickListener {
                        override fun onClick(view: View) {
                            finish()
                        }
                    }).show()
                }
            }
        }
    }

    private fun showDialogForLocationServiceSetting() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("위치 서비스 비활성화")
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하시겠습니까?")
        builder.setCancelable(true)
        builder.setPositiveButton("설정", object:DialogInterface.OnClickListener {
            override fun onClick(dialog:DialogInterface, id:Int) {
                val callGPSSettingIntent
                        = Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE)
            }
        })
        builder.setNegativeButton("취소", object:DialogInterface.OnClickListener {
            override fun onClick(dialog:DialogInterface, id:Int) {
                dialog.cancel()
            }
        })
        builder.create().show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            GPS_ENABLE_REQUEST_CODE ->
                //사용자가 GPS 활성 시켰는지 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {
                        Log.d(TAG, "onActivityResult : 퍼미션 가지고 있음")
                        needRequest = true
                        return
                    }
                }
        }
    }

//https://webnautes.tistory.com/1249
// https://link2me.tistory.com/1703
}
