package ru.grishankov.marketin.di

import org.koin.core.module.Module
import org.koin.dsl.module
import ru.grishankov.marketin.data.repositories.ApplicationsRepo
import ru.grishankov.marketin.data.repositories.ApplicationsRepoImpl

object DiRepositories : DiModule {
    override val module: Module
        get() = module {
            single<ApplicationsRepo> { ApplicationsRepoImpl(source = get()) }
        }
}