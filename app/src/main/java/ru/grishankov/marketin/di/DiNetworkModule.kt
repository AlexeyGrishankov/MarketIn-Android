package ru.grishankov.marketin.di

import android.util.Log
import androidx.compose.ui.viewinterop.AndroidView
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import kotlinx.serialization.json.Json
import org.koin.core.module.Module
import org.koin.dsl.module
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.decodeFromString
import ru.grishankov.marketin.BuildConfig
import ru.grishankov.marketin.util.network.ErrorApp
import ru.grishankov.marketin.util.network.NetworkClientException

/**
 * DI Ktor Module
 */
object DiNetworkModule : DiModule {
    override val module: Module
        get() = module {
            single { Json { ignoreUnknownKeys = true } }
            single { loggerProvider() }
            single { httpClientProvider(json = get(), ktorLogger = get()) }
        }

    /**
     * Ktor Logger impl Android
     */
    private fun loggerProvider() : Logger = object : Logger {
        override fun log(message: String) {
            Log.i("KTOR", message)
        }
    }

    /**
     * Http Ktor client
     */
    private fun httpClientProvider(
        json: Json,
        ktorLogger: Logger
    )  = HttpClient(Android) {

        expectSuccess = true

        HttpResponseValidator {
            handleResponseExceptionWithRequest(block = { t, _ ->
                val clientException = t as? ClientRequestException ?: return@handleResponseExceptionWithRequest
                val eResponse = clientException.response
                when(eResponse.status.value) {
                    in 400..499 -> {
                        val error: ErrorApp.ErrorCauses = Json.decodeFromString(eResponse.bodyAsText())
                        throw NetworkClientException(error, eResponse.status.value)
                    }
                }
            })
        }

        defaultRequest {
            url {
                contentType(ContentType.Application.Json)
                protocol = URLProtocol.HTTP
                host = BuildConfig.API_URL
            }
        }

        install(HttpTimeout) {
            requestTimeoutMillis = 30000
            connectTimeoutMillis = 30000
        }

        install(Logging) {
            logger = ktorLogger
            level = LogLevel.ALL
        }

        install(ContentNegotiation) {
            json(json = json)
        }
    }
}