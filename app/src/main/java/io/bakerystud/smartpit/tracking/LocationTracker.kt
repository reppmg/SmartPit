package io.bakerystud.smartpit.tracking

import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import javax.inject.Inject

class LocationTracker @Inject constructor(

) : LocationListener{

    private val data = mutableListOf<Location>()

    private var isRecording = false


    fun start() {
        isRecording = true
    }

    fun finish(): List<Location> {
        val result = data.toList()
        isRecording = false
        data.clear()
        return result
    }

    override fun onLocationChanged(location: Location?) {
        location ?: return
        if (!isRecording) return
        data.add(location)
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
    }

    override fun onProviderEnabled(provider: String?) {
    }

    override fun onProviderDisabled(provider: String?) {
    }
}