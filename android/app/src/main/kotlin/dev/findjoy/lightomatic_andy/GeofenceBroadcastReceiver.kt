package dev.findjoy.lightomatic_andy

import android.app.Notification
import android.content.BroadcastReceiver
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent
import java.time.Duration

class GeofenceBroadcastReceiver : BroadcastReceiver() {
    // ...
    override fun onReceive(context: Context?, intent: Intent?) {
        val geofencingEvent = GeofencingEvent.fromIntent(intent)
        if (geofencingEvent.hasError()) {
            val errorMessage = geofencingEvent.errorCode
            Log.e(TAG, errorMessage.toString())
            return
        }

        // Get the transition type.
        val geofenceTransition = geofencingEvent.geofenceTransition

        // Test that the reported transition was of interest.
        when (geofenceTransition) {
            Geofence.GEOFENCE_TRANSITION_ENTER -> { //hit the turn on endpoint
                Log.i(TAG, geofenceTransition.toString())
                Toast.makeText(context, "Entered fence", Toast.LENGTH_LONG).show()
            }


            Geofence.GEOFENCE_TRANSITION_EXIT -> {//hit the off endpoint
                Log.i(TAG, geofenceTransition.toString())
                Toast.makeText(context, "Exited fence", Toast.LENGTH_LONG).show()
            }
            else -> Log.i(TAG, "Error on geofence transition")
        }
    }
}
