package com.capstone.androidproject.MainFragment.StoreList

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.capstone.androidproject.MainActivity
import com.capstone.androidproject.R
import com.capstone.androidproject.Response.SellerData
import com.capstone.androidproject.Response.SellerDataResponse
import com.capstone.androidproject.ServerConfig.ServerConnect
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_find_to_map.*
import org.jetbrains.anko.startActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.*

class FindToMapActivity : AppCompatActivity(), OnMapReadyCallback,
    ActivityCompat.OnRequestPermissionsResultCallback, GoogleMap.OnMarkerClickListener{

    val TAG="Address"

    var mGoogleMap: GoogleMap? = null
    var currentMarker: Marker? = null

    val ZOOM = 17f

    lateinit var DEFAULT_LOCATION: LatLng

    private val PERMISSIONS_REQUEST_CODE = 100
    private val GPS_ENABLE_REQUEST_CODE = 2001
    val REQUIRED_PERMISSIONS:Array<String> = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    var mMoveMapByUser = true
    var mMoveMapByAPI = true
    var needRequest = false

    var currentPosition: LatLng? = null

    lateinit var _location: Location
    lateinit var mCurrentLocatiion: Location
    lateinit var locationRequest: LocationRequest
    private lateinit var mfusedLocationClient: FusedLocationProviderClient

    lateinit var mLayout: View
    lateinit var imm: InputMethodManager

    var sellers: ArrayList<SellerData> = ArrayList()
    var makerlist:ArrayList<MarkerOptions> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        setContentView(R.layout.activity_find_to_map)

        mLayout = findViewById(R.id.layout_map)
        imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager

        btnBackArrow.setOnClickListener {
            onBackPressed()
        }

        locationRequest = LocationRequest()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
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

        mGoogleMap?.setOnMarkerClickListener(this)
        DEFAULT_LOCATION = LatLng(intent.getDoubleExtra("lat",37.279), intent.getDoubleExtra("lon",127.043))

        //지도의 초기위치로 이동
        setDefaultLocation()

        //위치 퍼미션을 가지고 있는지 체크
        val hasFineLocationPermission = ContextCompat.checkSelfPermission(this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        val hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
            hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED   ) {
            startLocationUpdates() // 위치 업데이트 시작
        }else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])) {

                Snackbar.make(mLayout, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.",
                    Snackbar.LENGTH_INDEFINITE).setAction("확인",object: View.OnClickListener {
                    override fun onClick(view: View) {
                        ActivityCompat.requestPermissions( this@FindToMapActivity, REQUIRED_PERMISSIONS,
                            PERMISSIONS_REQUEST_CODE)
                    }
                }).show()
            } else {
                ActivityCompat.requestPermissions( this, REQUIRED_PERMISSIONS,
                    PERMISSIONS_REQUEST_CODE)
            }
        }
        mGoogleMap?.getUiSettings()!!.isMyLocationButtonEnabled = false
        mGoogleMap?.animateCamera(CameraUpdateFactory.zoomTo(ZOOM))

        mGoogleMap?.setOnMyLocationButtonClickListener(object: GoogleMap.OnMyLocationButtonClickListener { // 현재 위치 찾아가기
            override fun onMyLocationButtonClick(): Boolean {
                setDefaultLocation()
                return true
            }
        })

        mGoogleMap?.setOnCameraMoveStartedListener(object: GoogleMap.OnCameraMoveStartedListener {
            override fun onCameraMoveStarted(p0: Int) {
                mMoveMapByUser = false
                mMoveMapByAPI = false


                imgTextSeller.visibility = View.GONE
                textSellerName.setText("")
                textSubCount.setText("")
                textMinPrice.setText("")
            }
        })

        mGoogleMap?.setOnCameraMoveListener(object: GoogleMap.OnCameraMoveListener {
            override fun onCameraMove() {
                if(mMoveMapByAPI == true) {
                    mMoveMapByUser = false
                }
                else {
                    mMoveMapByUser = true
                }
                Log.d(TAG,"Move")
            }
        })
        mGoogleMap?.setOnCameraIdleListener(object: GoogleMap.OnCameraIdleListener{
            override fun onCameraIdle() {
                if(mMoveMapByUser == true ) {
                    Log.d(TAG, "onCameraIdle")
                    val location_center = mGoogleMap?.cameraPosition!!.target

                    currentPosition = location_center

                    val location = Location("currentLoc")
                    location.latitude = location_center!!.latitude
                    location.longitude = location_center.longitude

                    _location=location

                    val markerTitle = getCurrentAddress(currentPosition!!)

                    setCurrentLocation(location, markerTitle)

                    Log.d(TAG, "Idle")
                }
            }
        })
    }

    var locationCallback: LocationCallback = object: LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            val locationList = locationResult.getLocations()
            if (locationList.size > 0)
            {
                _location = locationList.get(locationList.size - 1)
                //location = locationList.get(0);
                currentPosition = LatLng(_location.getLatitude(), _location.getLongitude())
                val markerTitle = getCurrentAddress(currentPosition!!)
                //현재 위치에 마커 생성하고 이동
                setCurrentLocation(_location, markerTitle)
                mCurrentLocatiion = _location
            }
        }
    }

    private fun startLocationUpdates() {
        if (!checkLocationServicesStatus()) {
            showDialogForLocationServiceSetting()
        } else {
            val hasFineLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            val hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )

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
        val locationManager: LocationManager = getSystemService(LOCATION_SERVICE) as LocationManager

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    fun setCurrentLocation(location: Location, address:String) {
        if (currentMarker != null)
            currentMarker?.remove()

        val currentLatLng = LatLng(location.getLatitude(), location.getLongitude())

        val cameraUpdate = CameraUpdateFactory.newLatLng(currentLatLng)
        mGoogleMap?.moveCamera(cameraUpdate)
        mMoveMapByAPI = true

        getSeller(location,-1,mGoogleMap?.cameraPosition!!.zoom)

    }

    fun setDefaultLocation() {
        //디폴트 위치, 마지막 위치
        val address = getCurrentAddress(DEFAULT_LOCATION)

        if (currentMarker != null)
            currentMarker?.remove()

        val markerOptions = MarkerOptions()
        markerOptions.position(DEFAULT_LOCATION)
        markerOptions.draggable(true)
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
        currentMarker = mGoogleMap?.addMarker(markerOptions)

        Log.d("maplocation_DetailAddress",DEFAULT_LOCATION.latitude.toString())
        Log.d("maplocation_DetailAddress",DEFAULT_LOCATION.longitude.toString())

        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(DEFAULT_LOCATION, ZOOM)
        mGoogleMap?.moveCamera(cameraUpdate)
    }

    private fun checkPermission():Boolean  {
        val hasFineLocationPermission = ContextCompat.checkSelfPermission(this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        val hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

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
        builder.setPositiveButton("설정", object: DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface, id:Int) {
                val callGPSSettingIntent
                        = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE)
            }
        })
        builder.setNegativeButton("취소", object: DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface, id:Int) {
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

    fun getSeller( mylocate: Location, page: Int,zoom:Float) {
        val serverConnect = ServerConnect(this)
        val server = serverConnect.conn()

        server.postSellerRequest(mylocate.latitude, mylocate.longitude, page, zoom).enqueue(object :
            Callback<SellerDataResponse> {
            override fun onFailure(call: Call<SellerDataResponse>?, t: Throwable?) {
                Toast.makeText(this@FindToMapActivity, "통신 실패", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<SellerDataResponse>, response: Response<SellerDataResponse>) {
                val success = response.body()!!.success
                val sellerdata = response.body()!!.sellerdata
                sellers.clear()

                if (!success) {
                    Toast.makeText(this@FindToMapActivity, "새로고침 실패", Toast.LENGTH_SHORT).show()
                } else {
                    for (seller in sellerdata) {
                        val sellerlocate = Location("myLoc")
                        sellerlocate.longitude = seller.lon
                        sellerlocate.latitude = seller.lat
                        val distance = mylocate.distanceTo(sellerlocate).toDouble()

                        sellers.add(
                            SellerData(
                                seller.sellerId,
                                seller.name,
                                "",
                                "",
                                seller.totalSubs,
                                seller.lat,
                                seller.lon,
                                distance,
                                seller.imgURL,
                                "hihi",
                                seller.type,
                                seller.minPrice
                            )
                        )
                    }

                    setMarker()
                }
            }
        })
    }

    fun setMarker(){
        mGoogleMap?.clear()
        for(seller in sellers){
            val markerOptions = MarkerOptions()
            markerOptions.position(LatLng(seller.lat,seller.lon))

            when(seller.type){
                "cafe" -> {
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                }
                "dinner" -> {
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                }
                "bar" -> {
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                }
            }
            val marker:Marker = mGoogleMap?.addMarker(markerOptions)!!
            marker.tag = seller

        }
    }

    override fun onMarkerClick(p0: Marker?): Boolean {
        val seller = p0?.tag as SellerData
        imgTextSeller.visibility = View.VISIBLE
        textSellerName.setText(seller.name)
        textSubCount.setText(seller.totalSubs.toString())
        textMinPrice.setText(seller.minPrice.toString())

        return true
    }
//https://webnautes.tistory.com/1249
// https://link2me.tistory.com/1703
}
