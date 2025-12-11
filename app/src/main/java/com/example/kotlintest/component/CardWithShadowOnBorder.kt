package com.example.kotlintest.component

import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CardWithShadowOnBorder(modifier: Modifier = Modifier,cardContainerColor: Color= Color.White,content: @Composable () -> Unit) {
    ElevatedCard(
        modifier = modifier, colors = CardDefaults
            .cardColors(
                containerColor = cardContainerColor
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        content()
    }
}