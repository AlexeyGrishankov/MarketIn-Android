package ru.grishankov.marketin.data.sources

import io.ktor.client.HttpClient
import ru.grishankov.marketin.data.dto.AppDataDto
import ru.grishankov.marketin.data.dto.AppShortDataListDto
import ru.grishankov.marketin.util.network.Response
import ru.grishankov.marketin.util.network.getResponse

class ApplicationsDataSourcesRemote(private val httpClient: HttpClient) : ApplicationsDataSources {

    override suspend fun getAppList(): Response<AppShortDataListDto> {
        return httpClient.getResponse("api/applications")
    }

    override suspend fun getAppData(appId: Int): Response<AppDataDto> {
        return httpClient.getResponse("api/applications/$appId")
    }
}