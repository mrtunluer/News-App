package com.mertdev.yournews.helpers.extension

import androidx.navigation.NavController

fun NavController.navigateAndClearBackStack(route: String, popUpToRoute: String) {
    navigate(route) {
        popUpTo(popUpToRoute) {
            inclusive = true
        }
    }
}