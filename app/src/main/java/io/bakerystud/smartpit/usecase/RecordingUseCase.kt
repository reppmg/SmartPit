package io.bakerystud.smartpit.usecase

import io.bakerystud.smartpit.model.RecordWithLocation
import io.bakerystud.smartpit.processing.Merger
import io.bakerystud.smartpit.tracking.AccelerometerTracker
import io.bakerystud.smartpit.tracking.LocationTracker
import timber.log.Timber
import javax.inject.Inject

class RecordingUseCase @Inject constructor(
    private val locationTracker: LocationTracker,
    private val accelerometerTracker: AccelerometerTracker,
    private val merger: Merger
) {

    fun startRecording() {
        locationTracker.start()
        accelerometerTracker.start()
    }

    fun stopRecording() : List<RecordWithLocation> {
        val locations = locationTracker.finish()
        val accels = accelerometerTracker.finish()
        val times = locations.map { it.time }
        Timber.d("stopRecording locs: ${times.min()} - ${times.max()}")
        val timess = accels.map { it.timestamp }
        Timber.d("stopRecording locs: ${timess.min()} - ${timess.max()}")
        return merger.mergeLocationToRecord(locations, accels)
    }
}