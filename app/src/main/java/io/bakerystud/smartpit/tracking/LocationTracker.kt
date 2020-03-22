package io.bakerystud.smartpit.tracking

import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.ReplaySubject
import timber.log.Timber
import javax.inject.Inject

class LocationTracker @Inject constructor() : LocationListener {

    val lat = BehaviorSubject.create<Double>()
    val lon = BehaviorSubject.create<Double>()

    val data = mutableListOf<Location>()

    val locationObservable = ReplaySubject.create<Location>()

    private var isRecording = false

    fun start() {
        if (data.isEmpty()) {
            throw IllegalStateException("no location data yet")
        }
        isRecording = true
    }

    fun finish(): List<Location> {
        val result = data.toList()
        isRecording = false
        data.clear()
        data.add(result.last())
        return result
    }

    override fun onLocationChanged(location: Location?) {
        Timber.d("onLocationChanged")
        location ?: return
        data.add(location)
        if (!isRecording) return
        lat.onNext(location.latitude)
        lon.onNext(location.longitude)
        locationObservable.onNext(location)
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
    }

    override fun onProviderEnabled(provider: String?) {
    }

    override fun onProviderDisabled(provider: String?) {
    }
}