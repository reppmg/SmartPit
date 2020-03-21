package io.bakerystud.smartpit.tracking

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.opengl.Matrix
import io.bakerystud.smartpit.model.Record
import io.bakerystud.smartpit.storage.StoreManager
import io.reactivex.subjects.BehaviorSubject
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin


class AccelerometerTracker @Inject constructor(
    private val storage: StoreManager
) : SensorEventListener {

    val zAccel = BehaviorSubject.create<Float>()
    val yAccel = BehaviorSubject.create<Float>()
    val xAccel = BehaviorSubject.create<Float>()

    private val data = mutableListOf<Record>()

    private var isRecording = false

    fun start() {
        isRecording = true
    }

    fun finish(): List<Record> {
        val result = data.toList()
        isRecording = false
        data.clear()
        return result
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    val orientationvalues = arrayOf(0.0, 0.0, 0.0)

    private var gravityValues: FloatArray? = null
    private var magneticValues: FloatArray? = null

    override fun onSensorChanged(event: SensorEvent) {
        if (!isRecording) return
        when (event.sensor.type) {
            Sensor.TYPE_LINEAR_ACCELERATION -> {

                val deviceRelativeAcceleration = FloatArray(4)
                deviceRelativeAcceleration[0] = event.values[0]
                deviceRelativeAcceleration[1] = event.values[1]
                deviceRelativeAcceleration[2] = event.values[2]
                deviceRelativeAcceleration[3] = 0f

                // Change the device relative acceleration values to earth relative values
                // X axis -> East
                // Y axis -> North Pole
                // Z axis -> Sky

                // Change the device relative acceleration values to earth relative values
                // X axis -> East
                // Y axis -> North Pole
                // Z axis -> Sky
                val R = FloatArray(16)
                val I = FloatArray(16)
                val earthAcc = FloatArray(16)

                gravityValues ?: return
                magneticValues ?: return

                SensorManager.getRotationMatrix(R, I, gravityValues, magneticValues)

                val inv = FloatArray(16)

                Matrix.invertM(inv, 0, R, 0)
                Matrix.multiplyMV(earthAcc, 0, inv, 0, deviceRelativeAcceleration, 0)
                val timestamp = event.timestamp
                val date = System.currentTimeMillis()
                val record = Record(earthAcc[0], earthAcc[1], earthAcc[2], date)
                data.add(record)
                zAccel.onNext(earthAcc[2])
                yAccel.onNext(earthAcc[1])
                xAccel.onNext(earthAcc[0])
            }
            Sensor.TYPE_ROTATION_VECTOR -> {
                orientationvalues[0] = event.values[0].toDouble()
                orientationvalues[1] = event.values[1].toDouble()
                orientationvalues[2] = event.values[2].toDouble()
            }
            Sensor.TYPE_GRAVITY -> {
                gravityValues = event.values
            }
            Sensor.TYPE_MAGNETIC_FIELD -> {
                magneticValues = event.values
            }
            else -> { Timber.d("onSensorChanged listening non-accelerometer events") }
        }
    }


}