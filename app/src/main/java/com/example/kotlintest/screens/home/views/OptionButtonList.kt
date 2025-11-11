package com.example.kotlintest.screens.home.views

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kotlintest.component.HorizontalSpacer
import com.example.kotlintest.ui.theme.CeruleanBlue
import com.example.kotlintest.ui.theme.Lotion
import com.example.kotlintest.ui.theme.rhDisplayRegular

@Composable
fun RowScope.OptionBtnList(optionList: List<String>, onClick: (Int) -> Unit) {
    optionList
        .forEachIndexed { index, string ->
            Text(
                text = string,
                modifier = Modifier

                    .clip(RoundedCornerShape(15.dp))
                    .background(color = CeruleanBlue)
                    .border(color = CeruleanBlue, shape = RoundedCornerShape(15.dp), width = 2.dp)

                    .clickable {
                        onClick.invoke(index)
                    }
                    .padding(vertical = 10.dp, horizontal = 20.dp)
                ,
                style = MaterialTheme.typography.rhDisplayRegular.copy(
                    fontSize = 15.sp,
                    color = Lotion
                )
            )
            HorizontalSpacer(20)
        }
}
