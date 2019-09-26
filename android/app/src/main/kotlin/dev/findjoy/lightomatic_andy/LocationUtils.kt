package dev.findjoy.lightomatic_andy

import android.content.Context
import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class LocationUtils {

    private var location: MutableLiveData<Location> = MutableLiveData()
    private var fusedLocationProviderClient: FusedLocationProviderClient ?= null

    fun getInstance(context: Context): FusedLocationProviderClient{

        if (fusedLocationProviderClient == null)
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
        return fusedLocationProviderClient!!

    }

    fun getLocation(): LiveData<Location>{
        fusedLocationProviderClient!!.lastLocation
                .addOnSuccessListener { loc: Location? ->
                    location.value = loc

                }
        return  location
    }

}