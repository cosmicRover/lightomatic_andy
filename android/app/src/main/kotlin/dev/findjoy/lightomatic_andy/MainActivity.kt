package dev.findjoy.lightomatic_andy

import android.app.PendingIntent
import android.content.ContentProvider
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.ContactsContract
import android.provider.Settings
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*

import io.flutter.app.FlutterActivity
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugins.GeneratedPluginRegistrant

class MainActivity: FlutterActivity() {

  private val CHANNEL = "dev.findjoy/lightomatic_andy"
  lateinit var geofencingClient: GeofencingClient
  lateinit var locationRequest: LocationRequest
  lateinit var locationCallback: LocationCallback
  lateinit var fusedLocationProviderClient: FusedLocationProviderClient
  var geofenceList: MutableList<Geofence> = arrayListOf()
  private lateinit var fusedLocationClient: FusedLocationProviderClient

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    GeneratedPluginRegistrant.registerWith(this)

//    val permissions = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION,
//            android.Manifest.permission.ACCESS_COARSE_LOCATION
//            )
//    ActivityCompat.requestPermissions(this, permissions,0)

    getLocation()

    geofencingClient  = LocationServices.getGeofencingClient(this)
    methodCallHandler(CHANNEL);
  }

  override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)



  }

  private fun getLocation(){
    buildLocationRequest()
    buildLocationCallBack()

    fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
    fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())


  }

  private fun buildLocationCallBack(){
    locationCallback = object : LocationCallback(){

      override fun onLocationResult(p0: LocationResult?) {
        Log.d("last location ->", "${p0!!.lastLocation}")
      }
    }
  }

  private fun buildLocationRequest(){
    locationRequest = LocationRequest()
    locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    locationRequest.interval = 5000
    locationRequest.fastestInterval = 3000
    locationRequest.smallestDisplacement = 10f
  }

  private fun buildGeoFence(){

    geofenceList.add(Geofence.Builder().setRequestId("fence1").setCircularRegion(
            40.83966064453125,
            -73.86280397081097,
            150.0.toFloat()
    ).setExpirationDuration(Geofence.NEVER_EXPIRE).setTransitionTypes(
            Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT
    ).build())

    geofencingClient?.addGeofences(getGeofencingRequest(), geofencePendingIntent)?.run {
      addOnSuccessListener {
        Log.e("------>", "Geofence add successful")
      }
      addOnFailureListener {
        Log.e("------>", "Geofence add FAILED******")
      }
    }
  }



  private fun getGeofencingRequest(): GeofencingRequest {
    return GeofencingRequest.Builder().apply {
      setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
      addGeofences(geofenceList)
    }.build()
  }

  private val geofencePendingIntent: PendingIntent by lazy {
    val intent = Intent(this, GeofenceBroadcastReceiver::class.java)
    // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when calling
    // addGeofences() and removeGeofences().
    PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
  }

  private fun getPlatform(): String{
    return "Android ${Build.VERSION.RELEASE}"
  }

  private fun methodCallHandler(channel: String){

    MethodChannel(flutterView, channel).setMethodCallHandler{call, result ->
      when {
          call.method == "getPlatform" -> {
            val platform = getPlatform()
            result.success(platform)
          }
          call.method == "exeAndroidFence" -> {
            //val platform = getPlatform()
            buildGeoFence()
            result.success(geofenceList[0].requestId)
          }
          else -> result.notImplemented()
      }
    }

  }

}
