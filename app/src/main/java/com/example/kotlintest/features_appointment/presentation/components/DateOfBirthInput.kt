package com.example.kotlintest.features_appointment.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.kotlintest.util.data.model.DateOfBirth

@Composable
fun DateOfBirthInput(
    dateOfBirth: DateOfBirth,
    onPatientDateOfBirthChanged:(DateOfBirth) -> Unit,
) {
    val dayFocus = remember { FocusRequester() }
    val monthFocus = remember { FocusRequester() }
    val yearFocus = remember { FocusRequester() }

    var day by remember { mutableStateOf(dateOfBirth.day) }
    var month by remember { mutableStateOf(dateOfBirth.month) }
    var year by remember { mutableStateOf(dateOfBirth.year) }

    val isComplete = day.length == 2 && month.length == 2 && year.length == 4

    val isValidDate = remember(day, month, year) {
        isComplete && validateDate(day, month, year)
    }

    LaunchedEffect(isValidDate, isComplete) {
        if (isValidDate) {
            onPatientDateOfBirthChanged(
                DateOfBirth(
                    day = day,
                    month = month,
                    year = year
                )
            )
        }
    }

    Row(verticalAlignment = Alignment.CenterVertically) {
        DateField(
            value = day,
            placeholder = "DD",
            maxLength = 2,
            focusRequester = dayFocus,
            isError = !isValidDate && day.isNotEmpty(),
            onValueChange = {
                day = it
                if (it.length == 2) monthFocus.requestFocus()
            },
            onBackspaceWhenEmpty = {
                // First field → do nothing
            }
        )

        Spacer(Modifier.width(8.dp))

        DateField(
            value = month,
            placeholder = "MM",
            maxLength = 2,
            focusRequester = monthFocus,
            isError = !isValidDate && month.isNotEmpty(),
            onValueChange = {
                month = it
                if (it.length == 2) yearFocus.requestFocus()
            },
            onBackspaceWhenEmpty = {
                dayFocus.requestFocus()
            }
        )

        Spacer(Modifier.width(8.dp))

        DateField(
            value = year,
            placeholder = "YYYY",
            maxLength = 4,
            focusRequester = yearFocus,
            isError = !isValidDate && year.isNotEmpty(),
            onValueChange = { year = it },
            onBackspaceWhenEmpty = {
                monthFocus.requestFocus()
            }
        )
    }

    if (!isValidDate && (day.isNotEmpty() || month.isNotEmpty() || year.isNotEmpty())) {
        Text(
            text = "Invalid date",
            color = Color.Red,
            fontSize = 12.sp,
            modifier = Modifier.padding(start = 20.dp)
        )
    }
}


fun validateDate(day: String, month: String, year: String): Boolean {
    return try {
        val d = day.toInt()
        val m = month.toInt()
        val y = year.toInt()

        val date = LocalDate.of(y, m, d)
        date.year in 1900..LocalDate.now().year
    } catch (e: Exception) {
        false
    }
}
