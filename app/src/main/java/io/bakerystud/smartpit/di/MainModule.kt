package io.bakerystud.smartpit.di

import android.content.Context
import io.bakerystud.smartpit.di.providers.MainViewModelProvider
import io.bakerystud.smartpit.processing.Merger
import io.bakerystud.smartpit.server.RecordsRepository
import io.bakerystud.smartpit.storage.StoreManager
import io.bakerystud.smartpit.tracking.AccelerometerTracker
import io.bakerystud.smartpit.tracking.LocationTracker
import io.bakerystud.smartpit.usecase.RecordingUseCase
import io.bakerystud.smartpit.usecase.UploadUseCase
import toothpick.config.Module

class MainModule(context: Context) : Module() {
    init {
        bind(MainViewModelProvider::class.java).singletonInScope()
        bind(RecordingUseCase::class.java).singletonInScope()
        bind(UploadUseCase::class.java).singletonInScope()
        bind(RecordsRepository::class.java).singletonInScope()
        bind(Merger::class.java).singletonInScope()
        bind(StoreManager::class.java).singletonInScope()
        bind(AccelerometerTracker::class.java).singletonInScope()
        bind(LocationTracker::class.java).singletonInScope()
    }
}

const val SCOPE = "main"