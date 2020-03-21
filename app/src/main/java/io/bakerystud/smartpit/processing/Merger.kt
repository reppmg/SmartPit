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

        val latitudes = locs.map { it.latitude }.toDoubleArray()
        val longitudes = locs.map { it.longitude }.toDoubleArray()
        val locTimes = locs.map { it.time.toDouble() }.toDoubleArray()
        val latitudeInterpolation = SplineInterpolator().interpolate(locTimes, latitudes)
        val longitudeInterpolation = SplineInterpolator().interpolate(locTimes, longitudes)
        for (accelerationRecord in accs) {
            val time = accelerationRecord.timestamp.toDouble()
            val lat = latitudeInterpolation.value(time)
            val lon = longitudeInterpolation.value(time)
            val recordWithLocation = RecordWithLocation(
                accelerationRecord.x,
                accelerationRecord.y,
                accelerationRecord.z,
                accelerationRecord.timestamp,
                LatLng(lat, lon)
            )
            result.add(recordWithLocation)
        }

        return result
    }
}