package ru.grishankov.marketin.di

import org.koin.core.KoinApplication
import org.koin.core.context.startKoin

/**
 * DI Koin
 */
object DI {

    /**
     * Init DI
     */
    fun init(): KoinApplication {
        return startKoin {
            modules(
                DiNetworkModule.module + DiViewModel.module + DiDataSources.module + DiRepositories.module
            )
        }
    }
}