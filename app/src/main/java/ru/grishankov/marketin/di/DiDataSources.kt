package ru.grishankov.marketin.di

import org.koin.core.module.Module
import org.koin.dsl.module
import ru.grishankov.marketin.data.sources.ApplicationsDataSources
import ru.grishankov.marketin.data.sources.ApplicationsDataSourcesRemote

object DiDataSources : DiModule {
    override val module: Module
        get() = module {
            single<ApplicationsDataSources> { ApplicationsDataSourcesRemote(httpClient = get()) }
        }
}