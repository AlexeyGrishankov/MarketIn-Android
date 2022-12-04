package ru.grishankov.marketin.data.repositories

import kotlinx.coroutines.flow.flow
import ru.grishankov.marketin.data.dto.AppDataDto
import ru.grishankov.marketin.data.dto.AppShortDataListDto
import ru.grishankov.marketin.data.sources.ApplicationsDataSources
import ru.grishankov.marketin.util.extension.emitFlow
import ru.grishankov.marketin.util.network.ErrorApp
import ru.grishankov.marketin.util.network.FlowData
import ru.grishankov.marketin.util.network.FlowResponse
import ru.grishankov.marketin.util.network.handling

class ApplicationsRepoImpl(private val source: ApplicationsDataSources) : ApplicationsRepo{
    override suspend fun getAppList(): FlowResponse<AppShortDataListDto> = flow {
        source.getAppList().handling(
            onOk = {
                emitFlow { FlowData(data = data) }
            },
            onApiError = {
                emitFlow { FlowData(error = error) }
            },
            onNetworkError = {
                emitFlow { FlowData(error = ErrorApp.ErrorNetwork) }
            },
            onUnknownError = {
                emitFlow { FlowData(error = ErrorApp.ErrorUnknown) }
            }
        )
    }

    override suspend fun getAppData(appId: Int): FlowResponse<AppDataDto> = flow {
        source.getAppData(appId).handling(
            onOk = {
                emitFlow { FlowData(data = data) }
            },
            onApiError = {
                emitFlow { FlowData(error = error) }
            },
            onNetworkError = {
                emitFlow { FlowData(error = ErrorApp.ErrorNetwork) }
            },
            onUnknownError = {
                emitFlow { FlowData(error = ErrorApp.ErrorUnknown) }
            }
        )
    }
}