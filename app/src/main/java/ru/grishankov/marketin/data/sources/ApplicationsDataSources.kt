package ru.grishankov.marketin.data.sources

import ru.grishankov.marketin.data.dto.AppDataDto
import ru.grishankov.marketin.data.dto.AppShortDataListDto
import ru.grishankov.marketin.util.network.Response

interface ApplicationsDataSources {

    suspend fun getAppList(): Response<AppShortDataListDto>

    suspend fun getAppData(appId: Int): Response<AppDataDto>
}