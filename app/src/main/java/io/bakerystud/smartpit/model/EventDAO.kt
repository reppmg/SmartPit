package io.bakerystud.smartpit.model

data class EventDAO(
    val latitude: Double,
    val longitude: Double,
    val timestamp: Long,
    val speed: Double
)

fun RecordWithLocation.toDao(): EventDAO {
    return EventDAO(
        this.location.latitude,
        this.location.longitude,
        this.timestamp,
        this.speed
    )
}