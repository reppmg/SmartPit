package io.bakerystud.smartpit.model


//poka ne nado
data class Event(
    val time: Long,
    val position: LatLng,
    val type: EventType
)

enum class EventType {
    PIT,
    POLICEMAN,
    TRAM
}