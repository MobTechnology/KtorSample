package com.ktor.bal.model

import kotlinx.serialization.Serializable

@Serializable
data class InboxPostData(
    var InspectionIds: ArrayList<Int> = ArrayList<Int>()
)