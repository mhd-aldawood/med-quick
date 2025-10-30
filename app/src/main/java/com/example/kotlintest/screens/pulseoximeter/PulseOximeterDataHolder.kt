package com.example.kotlintest.screens.pulseoximeter

import androidx.compose.ui.graphics.Color

data class PulseOximeterDataHolder(val title: String, val titleIcon: Int,val cancelText:String,val cancelIcon:Int,val pulseOximeterCardList:List<PulseOximeterCard>)
data class PulseOximeterCard(val value:String, val title:String,val cardColor: Color, val cardUnit:String, val normalRange:String)