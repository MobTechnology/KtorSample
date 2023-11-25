package com.ktor.bal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import com.ktor.bal.model.CollapsableSection
import com.ktor.bal.viewmodel.MainViewModel

@Composable
fun CasesScreen(viewModel: MainViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.background))
            .wrapContentSize(Alignment.TopStart),
    ) {
        CollapsableLazyColumn(
            sections = listOf(
                CollapsableSection(
                    title = "Cases",
                    rows = viewModel.inboxList
                ),
            ),
        )
    }
}

@Composable
fun InspectionScreen(viewModel: MainViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.background))
            .wrapContentSize(Alignment.TopStart),
    ) {
        CollapsableLazyColumn(
            sections = listOf(
                CollapsableSection(
                    title = "Inspection",
                    rows = viewModel.inboxList
                ),
            ),
        )
    }
}

@Composable
fun ServiceRequestScreen(viewModel: MainViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.background))
            .wrapContentSize(Alignment.TopStart),
        verticalArrangement = Arrangement.Top

    ) {
        CollapsableLazyColumn(
            sections = listOf(
                CollapsableSection(
                    title = "Service Request",
                    rows = viewModel.inboxList
                ),
            ),
        )
    }
}

@Composable
fun WorkOrderScreen(viewModel: MainViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.background))
            .wrapContentSize(Alignment.TopStart),
        verticalArrangement = Arrangement.Top

    ) {
        CollapsableLazyColumn(
            sections = listOf(
                CollapsableSection(
                    title = "WorkOrder",
                    rows = viewModel.inboxList
                ),
            ),
        )
    }
}
