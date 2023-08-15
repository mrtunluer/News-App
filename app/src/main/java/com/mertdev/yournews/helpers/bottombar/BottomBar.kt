package com.mertdev.yournews.helpers.bottombar

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.mertdev.yournews.app.ui.theme.MyColor
import com.mertdev.yournews.presentation.common.Screen

@Composable
fun BottomBar(
    navController: NavHostController
) {
    val screens = listOf(
        Screen.HomeScreen,
        Screen.NewsByCategoriesScreen,
        Screen.BookmarkScreen,
        Screen.ProfileScreen
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    if (currentDestination?.route != Screen.CategorySelectionScreen.route) {
        BottomNavigation(
            backgroundColor = MyColor.Blue
        ) {
            screens.forEach { screen ->
                AddItem(
                    screen = screen,
                    currentDestination = currentDestination,
                    navController = navController
                )
            }
        }
    }
}

@Composable
fun RowScope.AddItem(
    screen: Screen, currentDestination: NavDestination?, navController: NavHostController
) {
    BottomNavigationItem(
        icon = {
            Icon(imageVector = screen.icon!!, contentDescription = null)
        },
        selected = currentDestination?.hierarchy?.any {
            it.route == screen.route
        } == true,
        onClick = {
            navController.navigate(screen.route) {
                launchSingleTop = true
            }
        },
        alwaysShowLabel = false,
        selectedContentColor = Color.White,
        unselectedContentColor = MyColor.UnselectedColor
    )
}