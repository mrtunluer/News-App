package com.mertdev.yournews.presentation.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Category
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(
    val route: String, val icon: ImageVector? = null
) {
    object CategorySelectionScreen : Screen(
        route = "category_selection_screen"
    )

    object HomeScreen : Screen(
        route = "home_screen", icon = Icons.Rounded.Home
    )

    object NewsByCategoriesScreen : Screen(
        route = "news_by_categories_screen", icon = Icons.Rounded.Category
    )

    object ProfileScreen : Screen(
        route = "profile_screen", icon = Icons.Rounded.Settings
    )
}