package io.bakerystud.smartpit.tracking

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import io.bakerystud.smartpit.model.Record
import io.bakerystud.smartpit.storage.StoreManager
import timber.log.Timber
import javax.inject.Inject

class AccelerometerTracker @Inject constructor(
    private val storage: StoreManager
) : SensorEventListener {

    val data = mutableListOf<Record>()

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type ==Sensor.TYPE_ACCELEROMETER){
            val ax=event.values[0]
            val ay=event.values[1]
            val az=event.values[2]
            val record = Record(ax, ay, az, event.timestamp)
        } else {
            Timber.d("onSensorChanged listening non-accelerometer events")
        }
    }


}