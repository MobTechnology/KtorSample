package com.ktor.bal

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ktor.bal.model.CollapsableSection

const val MaterialIconDimension = 24f

@Composable
fun CollapsableLazyColumn(
    sections: List<CollapsableSection>,
    modifier: Modifier = Modifier
) {
    val collapsedState = remember(sections) { sections.map { true }.toMutableStateList() }
    LazyColumn(modifier) {
        sections.forEachIndexed { i, dataItem ->
            val collapsed = collapsedState[i]
            item(key = "header_$i") {
                HeaderRow(collapsed, collapsedState, i, dataItem)
                Divider()
            }
            if (!collapsed) {
                items(dataItem.rows) { item ->
                    Row {
                        //Spacer(modifier = Modifier.size(MaterialIconDimension.dp))
                        CardView(
                            title = item.InspTemplateName,
                            date = item.InitiateDate,
                            initiatedBy = item.InitiatedBy,
                            status = item.Status
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CardView(title: String, date: String, initiatedBy: String, status: String) {
    Column(
        modifier = Modifier
            .clip(shape = RectangleShape)
            .padding(2.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(start = 8.dp)
                .fillMaxWidth()
        ) {
            Column() {
                Text(
                    text = title,
                    fontSize = 18.sp,
                    style = MaterialTheme.typography.labelLarge
                )
                Text(
                    text = date,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp
                )
                Text(
                    text = initiatedBy,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp
                )
                Text(
                    text = status,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp
                )
            }
        }
        Divider()
    }
}

@Composable
fun HeaderRow(
    collapsed: Boolean,
    collapsedState: SnapshotStateList<Boolean>,
    i: Int,
    dataItem: CollapsableSection
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable {
                collapsedState[i] = !collapsed
            }
    ) {
        Text(
            dataItem.title,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(vertical = 10.dp)
                .weight(1f)
        )
        Icon(
            Icons.Default.run {
                if (collapsed)
                    KeyboardArrowDown
                else
                    KeyboardArrowUp
            },
            contentDescription = "",
            tint = Color.LightGray,
        )
    }
}