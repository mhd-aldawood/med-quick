package com.example.kotlintest.features_appointment.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kotlintest.ui.theme.FrenchWine
import com.example.kotlintest.ui.theme.Periwinkle
import com.example.kotlintest.ui.theme.PrimaryMidLinkColor
import com.example.kotlintest.ui.theme.rhDisplayBold
import com.example.kotlintest.util.scalePxToDp
import androidx.compose.ui.input.key.Key

@Composable
fun DateField(
    value: String,
    placeholder: String,
    maxLength: Int,
    focusRequester: FocusRequester,
    isError: Boolean,
    onValueChange: (String) -> Unit,
    onBackspaceWhenEmpty: () -> Unit
) {
    val underlineColor = if (isError) FrenchWine else PrimaryMidLinkColor

    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        Box(
            modifier = Modifier,
            contentAlignment = Alignment.Center
        ) {
            if (value.isEmpty()) {
                Text(
                    text = placeholder,
                    fontSize = 14.sp,
                    color = Periwinkle,
                    textAlign = TextAlign.Center,
                )
            }

            BasicTextField(
                value = value,
                onValueChange = {
                    if (it.length <= maxLength && it.all(Char::isDigit)) {
                        onValueChange(it)
                    }
                },
                textStyle = MaterialTheme.typography.rhDisplayBold.copy(
                    color = PrimaryMidLinkColor,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                singleLine = true,
                modifier = Modifier
                    .width(
                        when (maxLength) {
                            4 -> scalePxToDp(80f)
                            else ->scalePxToDp(50f)
                        }
                    )
                    .focusRequester(focusRequester)
                    .onKeyEvent { event ->
                        if (
                            event.type == KeyEventType.KeyDown &&
                            event.key == Key.Backspace &&
                            value.isEmpty()
                        ) {
                            onBackspaceWhenEmpty()
                            true
                        } else {
                            false
                        }
                    },
            )
        }

        Spacer(Modifier.height(2.dp))

        Box(
            modifier = Modifier
                .width(
                    when (maxLength) {
                        4 -> scalePxToDp(80f)
                        else ->scalePxToDp(50f)
                    }
                )
                .height(1.dp)
                .background(underlineColor)
        )
    }
}
