package com.mertdev.yournews.presentation.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun LottieAnim(
    animationFileName: Int, modifier: Modifier
) {
    val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(animationFileName)
    )

    LottieAnimation(
        composition = composition, modifier = modifier, iterations = LottieConstants.IterateForever
    )
}