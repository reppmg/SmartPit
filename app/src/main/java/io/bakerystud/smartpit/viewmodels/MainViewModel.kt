package io.bakerystud.smartpit.viewmodels

import androidx.lifecycle.MutableLiveData
import io.bakerystud.smartpit.tracking.AccelerometerTracker
import io.bakerystud.smartpit.tracking.LocationTracker
import io.bakerystud.smartpit.usecase.RecordingUseCase
import io.bakerystud.smartpit.usecase.UploadUseCase
import ru.terrakok.cicerone.Router

class MainViewModel(
    router: Router,
    val locationListener: LocationTracker,
    private val recordUseCase: RecordingUseCase,
    private val uploadUseCase: UploadUseCase,
    val accelerometerTracker: AccelerometerTracker
) : BaseApiInteractionViewModel(router) {
    val recordingState = MutableLiveData<Boolean>()

    fun onStartRecording() {
        if (recordingState.value == true) return
        recordingState.value = true
        recordUseCase.startRecording()
    }

    fun onStopRecording() {
        if (recordingState.value == false) return
        recordingState.value = false
        val log = recordUseCase.stopRecording()
        startProgress()
        uploadUseCase.upload(log)
            .subscribe(this::onUplaod, this::onApiInteractionError)
            .disposeOnClear()
    }

    private fun onUplaod() {
        message.value = "Saved successfully"
    }
}