package com.mertdev.yournews.app.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.mertdev.yournews.R

val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily(
            Font(R.font.montserrat_medium, FontWeight.Light)
        ),
        fontWeight = FontWeight.Normal,
        fontSize = FontSize.Normal,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
)