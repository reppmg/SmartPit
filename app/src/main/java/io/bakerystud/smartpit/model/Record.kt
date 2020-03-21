package io.bakerystud.smartpit.model

data class Record (
    val x: Float, // real axis accelerations
    val y: Float,
    val z: Float,
    val timestamp: Long
)