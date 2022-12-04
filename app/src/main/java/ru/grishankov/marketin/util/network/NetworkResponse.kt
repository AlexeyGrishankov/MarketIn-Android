package ru.grishankov.marketin.util.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Сетевой запрос
 */
sealed class NetworkResponse<out Data : Any, out ErrorApp : Any> {

    /**
     * Успешный запрос
     */
    data class Ok<Data : Any>(val data: Data, val code: Int) : NetworkResponse<Data, Nothing>()

    /**
     * Ошибка API
     */
    data class ApiError<Failure : Any>(val error: Failure, val code: Int) : NetworkResponse<Nothing, Failure>()

    /**
     * Сетевая ошибка
     */
    data class NetworkError(val error: Throwable?) : NetworkResponse<Nothing, Nothing>()

    /**
     * Неизвестная ошибка
     */
    data class UnknownError(val error: Throwable?) : NetworkResponse<Nothing, Nothing>()
}

/**
 * Обработка ошибки
 */
suspend fun <Data : Any, Failure : Any> NetworkResponse<Data, Failure>.handling(
    onUnknownError: suspend NetworkResponse.UnknownError.() -> Unit = {},
    onNetworkError: suspend NetworkResponse.NetworkError.() -> Unit = {},
    onApiError: suspend NetworkResponse.ApiError<Failure>.() -> Unit = {},
    onOk: suspend NetworkResponse.Ok<Data>.() -> Unit = {},
) {
    when (this) {
        is NetworkResponse.UnknownError -> onUnknownError(this)
        is NetworkResponse.NetworkError -> onNetworkError(this)
        is NetworkResponse.ApiError -> onApiError(this)
        is NetworkResponse.Ok -> onOk(this)
    }
}

/**
 * Ошибки приложения
 */
@Serializable
sealed class ErrorApp {

    /**
     * Ошибка сети
     */
    @Serializable
    object ErrorNetwork : ErrorApp()

    /**
     * Неищвестная ошибка
     */
    @Serializable
    object ErrorUnknown : ErrorApp() {
        const val error = "Неизвестная ошибка"
    }

    /**
     * Ошибка с причиной
     */
    @Serializable
    data class ErrorCauses(
        @SerialName("causes") val causes: List<Causes>,
        @SerialName("title") val title: String,
    ): ErrorApp() {

        /**
         * Причины ошибки
         */
        @Serializable
        data class Causes(
            @SerialName("details") val details: List<String>,
            @SerialName("source") val source: String,
        )
    }
}

