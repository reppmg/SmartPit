package io.bakerystud.smartpit.processing

import io.bakerystud.smartpit.model.BumpType
import io.bakerystud.smartpit.model.RecordWithLocation


object PitFinder {

    fun hasBumpByMean(events: Array<RecordWithLocation>): BumpType {
        val mean = calculateZMean(events)
        return if (mean > 0.3) {
            BumpType.HIGH
        } else if (mean > 0.2) {
            BumpType.LOW
        } else {
            BumpType.NO
        }
    }

    fun hasBumpByDiff(events: Array<RecordWithLocation>): BumpType {
        val diff = calculateZDiff(events)
        return when {
            diff > 1 -> {
                BumpType.HIGH
            }
            diff > 0.5 -> {
                BumpType.LOW
            }
            else -> {
                BumpType.NO
            }
        }
    }

    private fun calculateZMean(events: Array<RecordWithLocation>): Float {
        var mean = 0f
        for (e in events) {
            val z: Float = Math.abs(e.z)
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