package io.bakerystud.smartpit

import android.Manifest
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorManager
import android.location.Criteria
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.PermissionChecker
import androidx.lifecycle.Observer
import io.bakerystud.smartpit.di.SCOPE
import io.bakerystud.smartpit.di.providers.MainViewModelProvider
import io.bakerystud.smartpit.viewmodels.MainViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber


class MainActivity : AppCompatActivity() {

    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.plant(Timber.DebugTree())
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = getViewModel(SCOPE, MainViewModelProvider::class.java)

        buttonStart.setOnClickListener {
            viewModel.onStartRecording()
        }
        buttonStop.setOnClickListener {
            viewModel.onStopRecording()
        }

        viewModel.recordingState.observe(this, Observer { isRecording ->
            if (isRecording) {
                buttonStart.hide()
                textRecordProgress.show()
                buttonStop.show()
            } else {
                buttonStop.hide()
                textRecordProgress.hide()
                buttonStart.show()
            }
        })
        viewModel.message.observe(this, Observer { showSnackbar(it) })

        requestAppPermissions()
    }

    private fun requestAppPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ), 322
            )
        } else {
            onPermissionsGranted()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 322) {
            if (grantResults.toList().all { it == PermissionChecker.PERMISSION_GRANTED }) {
                onPermissionsGranted()
                return
            }
        }
        requestAppPermissions()
    }

    private fun onPermissionsGranted() {
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        val criteria = Criteria()
        criteria.accuracy = Criteria.ACCURACY_FINE
        criteria.powerRequirement = Criteria.POWER_HIGH
        criteria.isAltitudeRequired = false
        criteria.isSpeedRequired = false
        criteria.isCostAllowed = true
        criteria.isBearingRequired = false
        val gpsFreqInMillis = 1000L
        val gpsFreqInDistance = 1.0f // in meters
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            locationManager.requestLocationUpdates(
                gpsFreqInMillis,
                gpsFreqInDistance,
                criteria,
                viewModel.locationListener,
                null
            )
            return
        }

        val sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        val rotationSensor: Sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
        val sensor: Sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)
        val gravS: Sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY)
        val magSensor: Sensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
        sensorManager.registerListener(viewModel.accelerometerTracker, sensor, 500_000)
        sensorManager.registerListener(viewModel.accelerometerTracker, rotationSensor, 50_000)
        sensorManager.registerListener(viewModel.accelerometerTracker, gravS, 50_000)
        sensorManager.registerListener(viewModel.accelerometerTracker, magSensor, 50_000)

        viewModel.accelerometerTracker.zAccel
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {textZ.text = String.format("%.2f", it)}
    }
}
