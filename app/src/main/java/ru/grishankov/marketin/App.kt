package ru.grishankov.marketin

import android.app.Application
import ru.grishankov.marketin.di.DI

/**
 * Main Android application
 */
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        DI.init()
    }
}