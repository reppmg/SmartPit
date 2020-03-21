package io.bakerystud.smartpit.di.providers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.bakerystud.smartpit.tracking.LocationTracker
import io.bakerystud.smartpit.usecase.RecordingUseCase
import io.bakerystud.smartpit.usecase.UploadUseCase
import io.bakerystud.smartpit.viewmodels.MainViewModel
import ru.terrakok.cicerone.Router
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class MainViewModelProvider @Inject constructor(
    private val router: Router,
    private val locationTracker: LocationTracker,
    private val recordingUseCase: RecordingUseCase,
    private val uploadUseCase: UploadUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            MainViewModel(router, locationTracker, recordingUseCase, uploadUseCase) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}