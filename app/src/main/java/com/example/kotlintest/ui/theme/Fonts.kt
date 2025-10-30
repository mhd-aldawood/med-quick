package com.example.kotlintest.ui.theme


import androidx.compose.runtime.Stable
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.example.kotlintest.R
//list of all available fonts
@Stable
fun FontRHDisplay() = FontFamily(
//    Font(R.font.sfprodisplayblackitalic, weight = FontWeight.Normal, style = FontStyle.Italic),

//    Font(R.font.sfprodisplayregular, weight = FontWeight.Normal),
//    Font(R.font.sfprodisplayregularItalic, style = FontStyle.Italic),

//    Font(R.font.sfprodisplaybold, weight = FontWeight.Bold),
//    Font(R.font.sfprodisplayboldItalic, weight = FontWeight.Bold),

//    Font(R.font.sfprodisplayheavyitalic, weight = FontWeight.Bold, style = FontStyle.Italic),

//    Font(R.font.sfprodisplaylightitalic, weight = FontWeight.Normal, style = FontStyle.Italic),

    Font(R.font.redhat_display_medium, weight = FontWeight.Medium),
    Font(R.font.redhat_display_regular),
    Font(R.font.redhat_display_black, weight = FontWeight.Black),
    Font(R.font.redhat_display_bold, weight = FontWeight.Bold),
    Font(R.font.redhat_display_semi_bold, weight = FontWeight.SemiBold),
//    Font(R.font.sfprodisplaymediumItalic, style = FontStyle.Italic),

//    Font(R.font.sfprodisplaythinitalic, style = FontStyle.Italic),
//    Font(R.font.sfprodisplayultralightitalic, style = FontStyle.Italic),
//
//    Font(R.font.sfprodisplaysemibold, weight = FontWeight.SemiBold, style = FontStyle.Normal),
//    Font(R.font.sfprodisplaysemibolditalic, weight = FontWeight.SemiBold, style = FontStyle.Italic),

    )
//
//@Composable
//@Stable
//fun FontSFMono() = FontFamily(
//    Font(R.font.sfmonobold, weight = FontWeight.Bold),
//    Font(R.font.sfmonobolditalic, weight = FontWeight.Bold),
//    Font(R.font.sfmonoligth),
//    Font(R.font.sfmonomeduim, weight = FontWeight.Medium),
//    Font(R.font.sfmonoregular),
//    Font(R.font.sfmonosemibold, weight = FontWeight.SemiBold),
//)
//
//@Composable
//@Stable
//fun FontSFMonoLightItalic() = FontFamily(
//    Font(R.font.sfmonoligthitalic, style = FontStyle.Italic, weight = FontWeight.Normal)
//)
//
//@Composable
//@Stable
//fun FontSFMonoBoldItalic() = FontFamily(
//    Font(R.font.sfmonobolditalic, style = FontStyle.Italic, weight = FontWeight.Bold)
//)
//
//@Composable
//@Stable
//fun FontInter() = FontFamily(
//    Font(R.font.interregular),
//)
//
//@Composable
//@Stable
//fun FontJakarta() = FontFamily(
//    Font(R.font.plusjakartasans),
//    Font(R.font.plusjakartasansitalic, style = FontStyle.Italic),
//)
//
@Stable
fun PoppinsFont() = FontFamily(
    Font(R.font.poppins_medium, weight = FontWeight.Medium),
)
