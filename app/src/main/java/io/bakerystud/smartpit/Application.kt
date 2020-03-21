package io.bakerystud.smartpit

import android.app.Application
import io.bakerystud.smartpit.di.MainModule
import io.bakerystud.smartpit.di.SCOPE
import toothpick.Toothpick

class Application : Application(){
    override fun onCreate() {
        super.onCreate()
        Toothpick.openScope(SCOPE).installModules(MainModule(this))
    }
}