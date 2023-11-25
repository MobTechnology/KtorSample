package com.ktor.bal.api

import android.util.Log
import com.ktor.bal.model.InboxPostData
import com.ktor.bal.model.InboxResponseData
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.observer.ResponseObserver
import io.ktor.client.request.accept
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

interface InboxApi {

    suspend fun getInbox(inboxPostData: InboxPostData): InboxResponseData?

    companion object {
        fun create(): InboxApi {
            return InboxApiImpl(
                client = HttpClient(Android) {

                    install(ContentNegotiation) {
                        json(json)
                    }

                    install(HttpTimeout)
                    {
                        socketTimeoutMillis = 30000000
                        requestTimeoutMillis = 30000000
                        connectTimeoutMillis = 30000000
                    }

                    install(Logging) {
                        logger = object : Logger {
                            override fun log(message: String) {
                                Log.d("Logger", "log: $message")
                            }
                        }
                        level = LogLevel.ALL
                    }

                    install(ResponseObserver) {
                        onResponse { response ->
                            Log.d("HTTP status:", "${response.status.value}")
                        }
                    }

                    install(Auth)
                    {
                        bearer {

                        }
                    }

                    defaultRequest {
                        contentType(ContentType.Application.Json)
                        accept(ContentType.Application.Json)
                    }
                }
            )
        }

        private val json = Json {
            ignoreUnknownKeys = true
            isLenient = true
            encodeDefaults = false
        }
    }
}