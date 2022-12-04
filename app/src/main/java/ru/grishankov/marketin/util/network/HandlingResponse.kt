package ru.grishankov.marketin.util.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import kotlinx.coroutines.flow.Flow

/**
 * Алиасы - обертки над запросами
 */
typealias Response<Data> = NetworkResponse<Data, ErrorApp>
typealias FlowResponse<Data> = Flow<FlowData<Data>>

/**
 * Flow Дата
 */
data class FlowData<out Data : Any>(
    val data: Data? = null,
    val error: ErrorApp? = null
)

/**
 * Обработчик всех сетевых ошибок
 */
fun <T : Any> handleAllError(ex: Throwable): NetworkResponse<T, ErrorApp> {
    return when (ex) {
        is NetworkClientException -> NetworkResponse.ApiError(ex.error, ex.statusCode)
        else -> NetworkResponse.UnknownError(ex)
    }
}

/**
 * Функция расширения для безопасного GET запроса
 */
suspend inline fun <reified T : Any> HttpClient.getResponse(
    url: String,
    block: HttpRequestBuilder.() -> Unit = {},
): NetworkResponse<T, ErrorApp> {
    return try {
        get(url, block).let {
            NetworkResponse.Ok(it.body(), it.status.value)
        }
    } catch (e: Throwable) {
        handleAllError(e)
    }
}

/**
 * Функция расширения для безопасного POST запроса
 */
suspend inline fun <reified T : Any> HttpClient.postResponse(
    url: String,
    block: HttpRequestBuilder.() -> Unit = {},
): NetworkResponse<T, ErrorApp> {
    return try {
        post(url, block).let {
            NetworkResponse.Ok(it.body(), it.status.value)
        }
    } catch (e: Throwable) {
        handleAllError(e)
    }
}

/**
 * Функция расширения для безопасного PUT запроса
 */
suspend inline fun <reified T : Any> HttpClient.putResponse(
    url: String,
    block: HttpRequestBuilder.() -> Unit = {},
): NetworkResponse<T, ErrorApp> {
    return try {
        put(url, block).let {
            NetworkResponse.Ok(it.body(), it.status.value)
        }
    } catch (e: Throwable) {
        handleAllError(e)
    }
}

/**
 * Функция расширения для безопасного DELETE запроса
 */
suspend inline fun <reified T : Any> HttpClient.deleteResponse(
    url: String,
    block: HttpRequestBuilder.() -> Unit = {},
): NetworkResponse<T, ErrorApp> {
    return try {
        delete(url, block).let {
            NetworkResponse.Ok(it.body(), it.status.value)
        }
    } catch (e: Throwable) {
        handleAllError(e)
    }
}