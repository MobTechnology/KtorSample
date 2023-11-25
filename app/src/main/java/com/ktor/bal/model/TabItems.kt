package com.ktor.bal.model

import androidx.compose.runtime.Composable
import com.ktor.bal.R

sealed class TabItems(var icon: Int, var title: String, var route: String) {
    object Cases : TabItems(R.drawable.permit, "Cases", "Cases")
    object Inspection : TabItems(R.drawable.inspection, "Inspections", "Inspections")
    object ServiceRequest :
        TabItems(R.drawable.service_request, "ServiceRequests", "ServiceRequests")

    object WorkOrder : TabItems(R.drawable.work_order, "WorkOrders", "WorkOrders")

    companion object {
        val navItems =
            listOf(TabItems.Cases, TabItems.Inspection, TabItems.ServiceRequest, TabItems.WorkOrder)
    }
}

