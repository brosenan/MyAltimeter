package net.rosenan.timeseries.sensors

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import net.rosenan.timeseries.TimeSeriesProvider

class LocationTimeSeries(private val context: Activity, private val intervalMillis: Long) :
    TimeSeriesProvider<Location>() {
    companion object {
        val TAG = "LocationTimeSeries"
    }

    private val fusedLocationClient = FusedLocationProviderClient(context)

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            Log.v(TAG, "Received location: ${locationResult.lastLocation}")
            for (location in locationResult.locations) {
                broadcast(location, System.currentTimeMillis())
            }
        }

        override fun onLocationAvailability(availability: LocationAvailability) {
            Log.v(TAG, "Location availability: $availability")
        }
    }

    fun start(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                context, arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), 0
            )
            return false
        }
        fusedLocationClient.lastLocation.addOnSuccessListener {
            fusedLocationClient.requestLocationUpdates(LocationRequest.create().apply {
                interval = intervalMillis
                priority = Priority.PRIORITY_BALANCED_POWER_ACCURACY
                maxWaitTime = 2000
            }, locationCallback, Looper.getMainLooper())
        }
        return true
    }

    fun stop() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }
}
