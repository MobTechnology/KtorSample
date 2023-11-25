package com.ktor.bal.api

sealed class Resources {
    companion object {
        const val TIME_OUT = 10_000
        const val TAG_KTOR_LOGGER = "ktor_logger:"
        const val TAG_HTTP_STATUS_LOGGER = "http_status:"

    }
}
