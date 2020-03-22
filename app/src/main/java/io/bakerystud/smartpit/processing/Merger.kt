package io.bakerystud.smartpit.processing

import android.location.Location
import io.bakerystud.smartpit.model.LatLng
import io.bakerystud.smartpit.model.Record
import io.bakerystud.smartpit.model.RecordWithLocation
import org.apache.commons.math3.analysis.interpolation.SplineInterpolator
import javax.inject.Inject

class Merger @Inject constructor(

) {
    fun mergeLocationToRecord(locs: List<Location>, accs: List<Record>): List<RecordWithLocation> {
        val result = mutableListOf<RecordWithLocation>()
        val locs = locs.sortedBy { it.time }
        val lastLocationTime = locs.last().time
        val speeds = locs.map { it.speed.toDouble() }.toDoubleArray()
        val latitudes = locs.map { it.latitude }.toDoubleArray()
        val longitudes = locs.map { it.longitude }.toDoubleArray()
        val locTimes = locs.map { it.time.toDouble() }.toDoubleArray()
        val latitudeInterpolation = SplineInterpolator().interpolate(locTimes, latitudes)
        val longitudeInterpolation = SplineInterpolator().interpolate(locTimes, longitudes)
        val speedInterpolation = SplineInterpolator().interpolate(locTimes, speeds)
        for (accelerationRecord in accs.filter { it.timestamp < lastLocationTime }) {
            val time = accelerationRecord.timestamp.toDouble()
            val lat = latitudeInterpolation.value(time)
            val lon = longitudeInterpolation.value(time)
            val speed = speedInterpolation.value(time)
            val recordWithLocation = RecordWithLocation(
                accelerationRecord.x,
                accelerationRecord.y,
                accelerationRecord.z,
                accelerationRecord.timestamp,
                LatLng(lat, lon),
                speed
            )
            result.add(recordWithLocation)
        }

        return result
    }

    fun mergeWithoutInterpolation(accs: List<Record>, location: Location): List<RecordWithLocation> {
        return accs.map {
            RecordWithLocation(
                it.x,
                it.y,
                it.z,
                it.timestamp,
                LatLng(location.latitude, location.longitude),
                location.speed.toDouble()
            )
        }
    }
}