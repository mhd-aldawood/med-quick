package com.example.kotlintest.screens.poct.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kotlintest.screens.poct.models.Category
import com.example.kotlintest.ui.theme.PrimaryMidLinkColor
import com.example.kotlintest.ui.theme.rhDisplayBold
import com.example.kotlintest.ui.theme.rhDisplayMedium

@Composable
fun SectionBox(section: Category) {
    Column {
        Text(
            text = section.title,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.rhDisplayBold.copy(
                fontSize = 14.sp,
                color = PrimaryMidLinkColor
            )
        )
        Spacer(Modifier.height(6.dp))
        section.items.forEachIndexed { index, it ->
            Text(
                text = it,
                style = MaterialTheme.typography.rhDisplayMedium.copy(
                    fontSize = 12.sp,
                    color = PrimaryMidLinkColor
                )
            )
            if (index != section.items.size - 1)
                Spacer(Modifier.height(8.dp))
        }
    }
}