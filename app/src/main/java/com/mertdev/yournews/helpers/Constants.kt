package com.mertdev.yournews.helpers

import androidx.datastore.preferences.core.stringPreferencesKey

object Constants {
    val SELECTED_CATEGORIES_KEY = stringPreferencesKey("selected_categories")
    val NEWS_CATEGORIES =
        listOf("Business", "Health", "General", "Technology", "Science", "Sports", "Entertainment")

}