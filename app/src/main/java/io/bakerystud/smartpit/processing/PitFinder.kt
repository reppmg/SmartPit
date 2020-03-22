package io.bakerystud.smartpit.processing

import io.bakerystud.smartpit.model.RecordWithLocation
import kotlin.math.abs

object PitFinder {
    fun hasBumpByMean(events: Array<RecordWithLocation>): Boolean {
        val mean = calculateZMean(events)
        return mean > 0.3
    }

    fun hasBumpByDiff(events: Array<RecordWithLocation>): Boolean {
        val diff = calculateZDiff(events)
        return diff > 1
    }

    private fun calculateZMean(events: Array<RecordWithLocation>): Float {
        var mean = 0f
        for (e in events) {
            val z: Float = abs(e.z)
            mean += z
        }
        return mean
    }

    private fun calculateZDiff(events: Array<RecordWithLocation>): Float {
        var max = -100f
        var min = 100f
        for (e in events) {
            if (e.z > max) {
                max = e.z
            }
            if (e.z < min) {
                min = e.z
            }
        }
        return max - min
    }
}