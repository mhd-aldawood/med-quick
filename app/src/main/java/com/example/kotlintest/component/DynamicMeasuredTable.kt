package com.example.kotlintest.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


data class TableColumn(
    val header: String
)

data class TableRow(
    val cells: List<String>
)

@Composable
fun DynamicMeasuredTable(
    columns: List<TableColumn>,
    rows: List<TableRow>,
    textStyle: TextStyle,
    columnSpacing: Dp = 16.dp,
    rowSpacing: Dp = 8.dp
) {
    val textMeasurer = rememberTextMeasurer()
    val density = LocalDensity.current

    // 🔹 Measure headers
    val columnWidths = remember(columns) {
        columns.map { column ->
            textMeasurer.measure(
                text = AnnotatedString(column.header),
                style = textStyle
            ).size.width
        }
    }

    val columnWidthsDp = with(density) {
        columnWidths.map { it.toDp() }
    }

    Column {

        // HEADER
        Row (modifier = Modifier.width(300.dp), horizontalArrangement = Arrangement.SpaceBetween){
            columns.forEachIndexed { index, column ->
                Text(
                    text = column.header,
                    style = textStyle,
                    modifier = Modifier
                        .width(columnWidthsDp[index])
                        .padding(bottom = 8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // BODY
        rows.forEach { row ->
            Row (modifier = Modifier.width(300.dp), horizontalArrangement = Arrangement.SpaceBetween){
                row.cells.forEachIndexed { index, cell ->
                    Text(
                        text = cell,
                        style = textStyle,
                        modifier = Modifier.width(columnWidthsDp[index])
                    )

                }
            }

            Spacer(modifier = Modifier.height(rowSpacing))
        }
    }
}

val columns = listOf(
    TableColumn("Item"),
    TableColumn("Result"),
    TableColumn("Reference"),
    TableColumn("Unit")
)

val rows = listOf(
    TableRow(listOf("URO", "0.0", "0–1", "mg/dL")),
    TableRow(listOf("PH", "6.0", "5–8", "")),
    TableRow(listOf("SG", "1.020", "1.005–1.030", ""))
)

@Preview
@Composable
fun tet() {
    DynamicMeasuredTable(
        columns = columns,
        rows = rows,
        textStyle = TextStyle(
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    )
}