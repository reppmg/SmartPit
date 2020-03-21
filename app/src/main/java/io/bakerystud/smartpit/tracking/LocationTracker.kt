package io.bakerystud.smartpit.tracking

import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import io.reactivex.subjects.BehaviorSubject
import timber.log.Timber
import javax.inject.Inject

class LocationTracker @Inject constructor() : LocationListener {

    val lat = BehaviorSubject.create<Double>()
    val lon = BehaviorSubject.create<Double>()

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
        Timber.d("onLocationChanged")
        location ?: return
        if (!isRecording) return
        lat.onNext(location.latitude)
        lon.onNext(location.longitude)
        data.add(location)
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
    }

    override fun onProviderEnabled(provider: String?) {
    }

    override fun onProviderDisabled(provider: String?) {
    }
}