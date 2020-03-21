package io.bakerystud.smartpit.model

data class RecordWithLocation(
    val x: Float, // real axis accelerations
    val y: Float,
    val z: Float,
    val timestamp: Long,
    val location: LatLng,
    val speed: Double
)