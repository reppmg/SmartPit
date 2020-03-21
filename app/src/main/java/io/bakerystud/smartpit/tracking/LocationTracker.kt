package io.bakerystud.smartpit.tracking

import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import javax.inject.Inject

class LocationTracker @Inject constructor(

) : LocationListener{

    val data = mutableListOf<Location>()

    init {

    }

    override fun onLocationChanged(location: Location?) {
        location ?: return
        data.add(location)
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
    }

    override fun onProviderEnabled(provider: String?) {
    }

    override fun onProviderDisabled(provider: String?) {
    }
}