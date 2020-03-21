package io.bakerystud.smartpit

import android.Manifest
import android.content.pm.PackageManager
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
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
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

    }
}
