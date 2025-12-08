package com.example.kotlintest.features_autentication.presentation.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kotlintest.R
import com.example.kotlintest.ui.theme.Periwinkle
import com.example.kotlintest.ui.theme.Platinum
import com.example.kotlintest.ui.theme.PrimaryMidLinkColor
import com.example.kotlintest.ui.theme.rhDisplayMedium
import com.example.kotlintest.util.scalePxToDp

@Composable
fun FloatingLabelUnderlineTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    isPassword: Boolean = false,
    showPassword: Boolean = false,
    onTogglePassword: (() -> Unit)? = null
) {
    val isFocused = remember { mutableStateOf(false) }

    val labelY by animateDpAsState(
        targetValue = if (value.isNotEmpty() || isFocused.value) 0.dp else 18.dp,
        animationSpec = tween(180)
    )

    val labelSize by animateFloatAsState(
        targetValue = if (value.isNotEmpty() || isFocused.value) 12f else 18f,
        animationSpec = tween(180)
    )

    Column(modifier) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(scalePxToDp(106f))
        ) {

            // Floating label
            Text(
                text = label,
                style = MaterialTheme.typography.rhDisplayMedium.copy(
                    color = Periwinkle,
                    fontSize = labelSize.sp,
                ),
                modifier = Modifier
                    .offset(y = labelY)
            )

            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = true,
                textStyle = MaterialTheme.typography.rhDisplayMedium.copy(
                    color = PrimaryMidLinkColor,
                    fontSize = 18.sp,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(scalePxToDp(70f))
                    .align(Alignment.BottomStart)
                    .onFocusChanged { isFocused.value = it.isFocused },
                visualTransformation =
                    if (isPassword && !showPassword)
                        CustomPasswordTransformation(dotSize = 22.sp)
                    else
                        VisualTransformation.None,
                decorationBox = { innerTextField ->

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(modifier = Modifier.weight(1f)) {
                            innerTextField()
                        }

                        if (isPassword && onTogglePassword != null) {
                            IconButton(onClick = onTogglePassword,
                                modifier = Modifier.size(scalePxToDp(60f))
                            ) {
                                Icon(
                                    painter = painterResource(
                                        if (showPassword) R.drawable.ic_eye_open
                                        else R.drawable.ic_eye_closed
                                    ),
                                    contentDescription = null,
                                    tint = PrimaryMidLinkColor,
                                    modifier =  Modifier.fillMaxSize()
                                )
                            }

                        }
                    }
                }
            )
        }

        // Underline
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .background(Platinum)
        )
    }
}