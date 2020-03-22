package io.bakerystud.smartpit.usecase

import android.location.Location
import io.bakerystud.smartpit.applyAsync
import io.bakerystud.smartpit.model.BumpType
import io.bakerystud.smartpit.model.Record
import io.bakerystud.smartpit.model.RecordWithLocation
import io.bakerystud.smartpit.processing.Merger
import io.bakerystud.smartpit.processing.PitFinder
import io.bakerystud.smartpit.tracking.AccelerometerTracker
import io.bakerystud.smartpit.tracking.LocationTracker
import io.reactivex.Observable
import timber.log.Timber
import javax.inject.Inject

class RecordingUseCase @Inject constructor(
    private val locationTracker: LocationTracker,
    private val accelerometerTracker: AccelerometerTracker,
    private val merger: Merger
) {
    private val windowSize: Int = 2

    val events = mutableListOf<RecordWithLocation>()

    fun startRecording(): Observable<BumpType> {
        locationTracker.start()
        accelerometerTracker.start()

        events.clear()
        return accelerometerTracker.dataObservable
            .buffer(windowSize)
            .map { recordsWindow: List<Record> ->
                val location = locationTracker.data.last()

                if (recordsWindow.size < windowSize) {
                    return@map BumpType.NO
                } else {
                    val events =
                        merger.mergeWithoutInterpolation(recordsWindow, location).toTypedArray()
                    val hasBump = PitFinder.hasBumpByDiff(events)
                    if (hasBump != BumpType.NO) {
                        this.events.add(
                            RecordWithLocation(
                                events.map { it.x }.average().toFloat(),
                                events.map { it.y }.average().toFloat(),
                                events.map { it.z }.average().toFloat(),
                                events.map { it.timestamp }.average().toLong(),
                                events[events.size / 2].location,
                                events.map { it.speed }.average(),
                                hasBump
                            )
                        )
                    }
                    return@map hasBump
                }
            }
            .applyAsync()
    }

    fun stopRecording(): List<RecordWithLocation> {
        val locations = locationTracker.finish()
        val accels = accelerometerTracker.finish()
        val times = locations.map { it.time }
        Timber.d("stopRecording locs: ${times.min()} - ${times.max()}")
        val timess = accels.map { it.timestamp }
        Timber.d("stopRecording accels: ${timess.min()} - ${timess.max()}")
        return merger.mergeLocationToRecord(locations, accels)
    }
}