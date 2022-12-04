package ru.grishankov.marketin.util.network

/**
 * Кастомное исключение для сетевого клиента
 */
class NetworkClientException(
    val error: ErrorApp.ErrorCauses,
    val statusCode: Int,
) : Exception()