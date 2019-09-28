package dev.findjoy.lightomatic_andy

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.lifecycle.*
import com.google.android.gms.location.*

import io.flutter.app.FlutterActivity
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugins.GeneratedPluginRegistrant
import java.util.jar.Manifest

class MainActivity: FlutterActivity(){
  private val CHANNEL = "dev.findjoy/lightomatic_andy"
  lateinit var geofencingClient: GeofencingClient
  var geofenceList: MutableList<Geofence> = arrayListOf()

  lateinit var lifeCycleRegistry: LifecycleRegistry
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    GeneratedPluginRegistrant.registerWith(this)
    geofencingClient  = LocationServices.getGeofencingClient(this)

    isLocationEnabled()
    methodCallHandler(CHANNEL)
  }

  private fun isLocationEnabled(){
    if(ContextCompat.checkSelfPermission
            (this, android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
      val permissions = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION)
      ActivityCompat.requestPermissions(this, permissions, 0)
    }
  }

  private fun getPlatform(): String{
    return "Android ${Build.VERSION.RELEASE}"
  }


  private fun buildGeoFenceAndAddToList(){
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

  private fun methodCallHandler(channel: String){

    MethodChannel(flutterView, channel).setMethodCallHandler{call, result ->
      when {
          call.method == "getPlatform" -> {
            val platform = getPlatform()
            result.success(platform)
          }
          call.method == "exeAndroidFence" -> {
            //val platform = getPlatform()
            buildGeoFenceAndAddToList()
            result.success("Fence Active")
          }

          else -> result.notImplemented()
      }
    }

  }

}
