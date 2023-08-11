package com.mertdev.yournews.data.local.repo

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.mertdev.yournews.domain.repo.CategoryRepository
import com.mertdev.yournews.helpers.Constants
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : CategoryRepository {

    override suspend fun getSelectedCategories(): List<String> {
        val preferences = dataStore.data.first()
        val selectedCategoriesString = preferences[Constants.SELECTED_CATEGORIES_KEY] ?: ""
        return if (selectedCategoriesString.isEmpty()) listOf()
        else selectedCategoriesString.split(",")
    }

    override suspend fun saveSelectedCategories(categories: List<String>) {
        dataStore.edit { preferences ->
            preferences[Constants.SELECTED_CATEGORIES_KEY] = categories.joinToString(",")
        }
    }

    override suspend fun isAlreadySelected(): Boolean {
        return getSelectedCategories().isNotEmpty()
    }

}