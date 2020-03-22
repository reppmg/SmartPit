package io.bakerystud.smartpit.viewmodels

import androidx.lifecycle.MutableLiveData
import io.bakerystud.smartpit.model.BumpType
import io.bakerystud.smartpit.tracking.AccelerometerTracker
import io.bakerystud.smartpit.tracking.LocationTracker
import io.bakerystud.smartpit.usecase.RecordingUseCase
import io.bakerystud.smartpit.usecase.UploadUseCase
import ru.terrakok.cicerone.Router
import timber.log.Timber

class MainViewModel(
    router: Router,
    val locationListener: LocationTracker,
    private val recordUseCase: RecordingUseCase,
    private val uploadUseCase: UploadUseCase,
    val accelerometerTracker: AccelerometerTracker
) : BaseApiInteractionViewModel(router) {
    val recordingState = MutableLiveData<Boolean>()

    val isPit = MutableLiveData<BumpType>()

    fun onStartRecording() {
        if (recordingState.value == true) return
        try {
            recordUseCase.startRecording()
                .subscribe(this::onClassificationResult, this::onError)
                .disposeOnClear()
            recordingState.value = true
        } catch (e: IllegalStateException) {
            message.value = "No location data yet"
        }
    }

    private fun onClassificationResult(isPit: BumpType) {
        this.isPit.value = isPit
    }

    private fun onError(it: Throwable) {
        message.value = it.message
    }

    fun onStopRecording() {
        if (recordingState.value == false) return
        recordingState.value = false
        val log = recordUseCase.stopRecording()
        val events = recordUseCase.events
        startProgress()
        Timber.d("onStopRecording uploading ${events.size} events")
        uploadUseCase.upload(log, events)
            .subscribe(this::onUplaod, this::onApiInteractionError)
            .disposeOnClear()
    }

    private fun onUplaod() {
        message.value = "Saved successfully"
    }
}