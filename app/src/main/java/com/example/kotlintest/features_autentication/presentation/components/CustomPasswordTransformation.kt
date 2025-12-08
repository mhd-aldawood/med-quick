package com.example.kotlintest.features_autentication.presentation.components

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

class CustomPasswordTransformation(private val dotSize: TextUnit = 14.sp) :
    VisualTransformation {

    override fun filter(text: AnnotatedString): TransformedText {
        val dot = "●" // You can also use • or ○ or any symbol
        val transformed = dot.repeat(text.length)

        return TransformedText(
            AnnotatedString(
                transformed,
                spanStyles = listOf(
                    AnnotatedString.Range(
                        SpanStyle(fontSize = dotSize),
                        start = 0,
                        end = transformed.length
                    )
                )
            ),
            OffsetMapping.Identity
        )
    }
}
