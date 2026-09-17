package com.example.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.R

// Clean modern Gujarati font loaded from local assets
val GujaratiFontFamily = FontFamily(
  Font(R.font.noto_sans_gujarati, FontWeight.Normal),
  Font(R.font.noto_sans_gujarati, FontWeight.Medium),
  Font(R.font.noto_sans_gujarati, FontWeight.SemiBold),
  Font(R.font.noto_sans_gujarati, FontWeight.Bold)
)

val Typography = Typography(
  displayLarge = TextStyle(
    fontFamily = GujaratiFontFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 28.sp,
    lineHeight = 36.sp,
    color = TextWhite
  ),
  displayMedium = TextStyle(
    fontFamily = GujaratiFontFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 24.sp,
    lineHeight = 32.sp,
    color = TextWhite
  ),
  headlineMedium = TextStyle(
    fontFamily = GujaratiFontFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 20.sp,
    lineHeight = 28.sp,
    color = TextWhite
  ),
  titleLarge = TextStyle(
    fontFamily = GujaratiFontFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 18.sp,
    lineHeight = 26.sp,
    color = TextWhite
  ),
  titleMedium = TextStyle(
    fontFamily = GujaratiFontFamily,
    fontWeight = FontWeight.SemiBold,
    fontSize = 15.sp,
    lineHeight = 22.sp,
    color = TextWhite
  ),
  titleSmall = TextStyle(
    fontFamily = GujaratiFontFamily,
    fontWeight = FontWeight.Medium,
    fontSize = 13.sp,
    lineHeight = 18.sp,
    color = TextGoldenHighlight
  ),
  bodyLarge = TextStyle(
    fontFamily = GujaratiFontFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 14.sp,
    lineHeight = 20.sp,
    color = TextOffWhite
  ),
  bodyMedium = TextStyle(
    fontFamily = GujaratiFontFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 12.sp,
    lineHeight = 18.sp,
    color = TextMutedBlueGray
  ),
  bodySmall = TextStyle(
    fontFamily = GujaratiFontFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 11.sp,
    lineHeight = 16.sp,
    color = TextMutedBlueGray
  ),
  labelLarge = TextStyle(
    fontFamily = GujaratiFontFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 13.sp,
    lineHeight = 18.sp,
    color = NavyDark
  ),
  labelMedium = TextStyle(
    fontFamily = GujaratiFontFamily,
    fontWeight = FontWeight.SemiBold,
    fontSize = 11.sp,
    lineHeight = 16.sp,
    color = TextMutedBlueGray
  ),
  labelSmall = TextStyle(
    fontFamily = GujaratiFontFamily,
    fontWeight = FontWeight.Medium,
    fontSize = 10.sp,
    lineHeight = 14.sp,
    color = TextGoldenHighlight
  )
)
