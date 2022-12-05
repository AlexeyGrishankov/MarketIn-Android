package ru.grishankov.marketin.data.repositories

import ru.grishankov.marketin.data.dto.AppDataDto
import ru.grishankov.marketin.data.dto.AppShortDataListDto
import ru.grishankov.marketin.data.dto.MarketUpdate
import ru.grishankov.marketin.data.dto.MarketUpdateReq
import ru.grishankov.marketin.util.network.FlowResponse

interface ApplicationsRepo {

    suspend fun getAppList(): FlowResponse<AppShortDataListDto>

    suspend fun getAppData(appId: Int): FlowResponse<AppDataDto>

    suspend fun getLastVersion(updateReq: MarketUpdateReq): FlowResponse<MarketUpdate>
}