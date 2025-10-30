package com.example.kotlintest.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.kotlintest.util.pxToSp

//list of all available typegraph will be used in the project
//val Typography.sfMonoItalic: TextStyle
//    @Composable
//    get() = TextStyle(
//        fontWeight = FontWeight.Normal,
//        fontSize = 12.sp,
//        fontFamily = FontSFPro(),
//        fontStyle = FontStyle.Italic,
//        lineHeight = 14.sp
//    )
//
//val Typography.sfMonoItalicLight: TextStyle
//    @Composable
//    get() = TextStyle(
//        fontWeight = FontWeight.Light,
//        fontSize = 12.sp,
//        fontFamily = FontSFMonoLightItalic(),
//        fontStyle = FontStyle.Italic,
//        lineHeight = 12.sp
//    )
//
//val Typography.sfMonoTF: TextStyle
//    @Composable
//    get() = TextStyle(
//        color = MaterialTheme.colorScheme.graysGray,
//        fontSize = 14.sp, // Adjust font size as needed
//        fontWeight = FontWeight(300),
//        lineHeight = 24.sp,
//        fontFamily = FontSFMono()
//    )
//
//val Typography.sfMonoTFWarning: TextStyle
//    @Composable
//    get() = TextStyle(
//        color = MaterialTheme.colorScheme.graysGray,
//        fontSize = 12.sp, // Adjust font size as needed
//        fontWeight = FontWeight(400),
//        lineHeight = 14.32.sp,
//        fontFamily = FontSFMono()
//    )
//NOTE for font side use same like the adobe xd
val Typography.rhDisplayRegular: TextStyle
    @Composable
    get() = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp,
        fontFamily = FontRHDisplay(),
        fontStyle = FontStyle.Normal,
    )
val Typography.rhDisplayMedium: TextStyle
    @Composable
    get() = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp,
        fontFamily = FontRHDisplay(),
        fontStyle = FontStyle.Normal,
    )
val Typography.rhDisplayBlack: TextStyle
    @Composable
    get() = TextStyle(
        fontWeight = FontWeight.Black,
        fontSize = pxToSp( 30F),
        fontFamily = FontRHDisplay(),
    )
val Typography.rhDisplayBold: TextStyle
    @Composable
    get() = TextStyle(
        fontSize = pxToSp( 29F),
        fontFamily = FontRHDisplay(),
        fontWeight = FontWeight.Bold
    )
val Typography.rhDisplaySemiBold: TextStyle
    @Composable
    get() = TextStyle(
        fontFamily = FontRHDisplay(),
        fontWeight = FontWeight.Bold
    )
val Typography.poppinsMedium: TextStyle
    @Composable
    get() = TextStyle(
        fontFamily = PoppinsFont(),
        fontWeight = FontWeight.Medium
    )