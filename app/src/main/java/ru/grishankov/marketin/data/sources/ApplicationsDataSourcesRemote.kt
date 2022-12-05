package ru.grishankov.marketin.data.sources

import io.ktor.client.HttpClient
import io.ktor.client.request.setBody
import ru.grishankov.marketin.data.dto.AppDataDto
import ru.grishankov.marketin.data.dto.AppShortDataListDto
import ru.grishankov.marketin.data.dto.MarketUpdate
import ru.grishankov.marketin.data.dto.MarketUpdateReq
import ru.grishankov.marketin.util.network.Response
import ru.grishankov.marketin.util.network.getResponse
import ru.grishankov.marketin.util.network.postResponse

class ApplicationsDataSourcesRemote(private val httpClient: HttpClient) : ApplicationsDataSources {

    override suspend fun getAppList(): Response<AppShortDataListDto> {
        return httpClient.getResponse("api/applications")
    }

    override suspend fun getAppData(appId: Int): Response<AppDataDto> {
        return httpClient.getResponse("api/applications/$appId")
    }

    override suspend fun getLastVersion(updateReq: MarketUpdateReq): Response<MarketUpdate> {
        return httpClient.postResponse("api/market/update") {
            setBody(updateReq)
        }
    }
}