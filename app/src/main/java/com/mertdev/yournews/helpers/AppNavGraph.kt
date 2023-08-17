package com.mertdev.yournews.helpers

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mertdev.yournews.domain.model.Article
import com.mertdev.yournews.presentation.bookmark.BookmarkScreen
import com.mertdev.yournews.presentation.onboarding.CategorySelectionScreen
import com.mertdev.yournews.presentation.home.HomeScreen
import com.mertdev.yournews.presentation.news_by_category.NewsByCategoriesScreen
import com.mertdev.yournews.presentation.common.Screen
import com.mertdev.yournews.presentation.detail.DetailScreen
import com.mertdev.yournews.presentation.profile.ProfileScreen

@Composable
fun AppNavGraph(
    modifier: Modifier, navController: NavHostController
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Screen.CategorySelectionScreen.route
    ) {
        composable(route = Screen.CategorySelectionScreen.route) {
            CategorySelectionScreen(navController = navController)
        }
        composable(route = Screen.HomeScreen.route) {
            HomeScreen(navController)
        }
        composable(route = Screen.NewsByCategoriesScreen.route) {
            NewsByCategoriesScreen(navController)
        }
        composable(route = Screen.BookmarkScreen.route) {
            BookmarkScreen()
        }
        composable(route = Screen.ProfileScreen.route) {
            ProfileScreen()
        }
        composable(
            route = Screen.DetailScreen.route
        ) {
            val article =
                navController.previousBackStackEntry?.savedStateHandle?.get<Article>("article")
            article?.let {
                DetailScreen(it)
            }
        }
    }
}
