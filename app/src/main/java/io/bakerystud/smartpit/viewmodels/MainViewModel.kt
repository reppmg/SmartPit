package io.bakerystud.smartpit.viewmodels

import io.bakerystud.smartpit.tracking.LocationTracker
import io.bakerystud.smartpit.usecase.RecordingUseCase
import ru.terrakok.cicerone.Router

class MainViewModel(
    router: Router,
    val locationListener: LocationTracker,
    private val useCase: RecordingUseCase
) : BaseApiInteractionViewModel(router) {
    fun onStartRecording() {
        useCase.startRecording()
    }

    fun onStopRecording() {
        val log = useCase.stopRecording()
    }
}