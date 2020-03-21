package io.bakerystud.smartpit.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.bakerystud.smartpit.R
import io.bakerystud.smartpit.isNoInternet
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import ru.terrakok.cicerone.Router
import timber.log.Timber
import toothpick.Toothpick
import java.net.SocketTimeoutException
import java.net.UnknownHostException

abstract class BaseApiInteractionViewModel(
    protected val router: Router
) : ViewModel() {

    private val subscription = CompositeDisposable()

    protected open val scope: String? = null

    val message = MutableLiveData<String>()
    val messageRes = MutableLiveData<Int>()
    val progressState = MutableLiveData<Boolean>()
    val noInternetShown = MutableLiveData<Boolean>(false)

    protected open fun onApiInteractionSuccess() {
        progressState.value = false
    }

    protected open fun onApiInteractionError(error: Throwable?) {
        progressState.value = false
        if (error.isNoInternet()) {
            messageRes.value = R.string.no_internet_retry
            return
        }
        val unknownText = "Unknown error"
        Timber.e(error)
            message.value = ((error as? UnknownError)?.let { unknownText })
                ?: (specificErrorMessage(error)) ?: error?.localizedMessage
                        ?: unknownText
    }

    protected fun startProgress() {
        progressState.value = true
    }

    open fun specificErrorMessage(error: Throwable?): String? = null

    override fun onCleared() {
        Timber.d("onCleared ${this::class.java}")
        super.onCleared()
        subscription.dispose()
    }

    fun Disposable.disposeOnClear() {
        this@BaseApiInteractionViewModel.subscription.add(this)
    }


    /**
     * The only way to close activity to preserve consistent DI
     */
    open fun exit() {
        router.exit()
        onExit()
    }

    open fun onExit() {
        scope?.let { Toothpick.closeScope(scope) }
    }

}

