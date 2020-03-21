package io.bakerystud.smartpit.viewmodels

import android.location.LocationListener
import io.bakerystud.smartpit.tracking.LocationTracker
import ru.terrakok.cicerone.Router

class MainViewModel(
    router: Router,
    val locationListener: LocationTracker
) : BaseApiInteractionViewModel(router) {
}