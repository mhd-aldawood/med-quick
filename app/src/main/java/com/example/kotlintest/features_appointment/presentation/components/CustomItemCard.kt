package com.example.kotlintest.features_appointment.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.kotlintest.ui.theme.Lotion
import com.example.kotlintest.ui.theme.PrimaryMidLinkColor
import com.example.kotlintest.ui.theme.White


@Composable
fun CustomItemCard(
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    onClick: () -> Unit = {},
    content : @Composable () -> Unit
) {
    ElevatedCard(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = if (selected) Lotion else White
        ),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp,  // make it more visible
            pressedElevation = 4.dp
        ),
        onClick = onClick,
    )
    {
        Box(
            modifier = Modifier
                .border(
                    1.dp,
                    if (selected) PrimaryMidLinkColor else White,
                    RoundedCornerShape(10.dp)
                )
                .fillMaxWidth()
        ) {
            content()
        }
    }
}