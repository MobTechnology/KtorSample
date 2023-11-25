package com.ktor.bal.model

import kotlinx.serialization.Serializable

@Serializable
data class InboxResponseData(
    val ErrorMessages: List<String>,
    val Message: String,
    val Status: Int,
    val SuccessMessages: List<String>,
    val Value: List<InboxData>,
    val WarningMessages: List<String>
)