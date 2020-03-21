package io.bakerystud.smartpit.usecase

import io.bakerystud.smartpit.model.RecordWithLocation
import io.bakerystud.smartpit.processing.Merger
import io.bakerystud.smartpit.tracking.AccelerometerTracker
import io.bakerystud.smartpit.tracking.LocationTracker
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
        return merger.mergeLocationToRecord(locations, accels)
    }
}