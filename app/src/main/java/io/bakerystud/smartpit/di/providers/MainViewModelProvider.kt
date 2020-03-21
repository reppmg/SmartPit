package io.bakerystud.smartpit.di.providers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import io.bakerystud.smartpit.viewmodels.MainViewModel
import ru.terrakok.cicerone.Router

@Suppress("UNCHECKED_CAST")
class MainViewModelProvider @Inject constructor(
    private val router: Router
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            MainViewModel(router) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}