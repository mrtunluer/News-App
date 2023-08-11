package com.mertdev.yournews.domain.repo

interface CategoryRepository {
    suspend fun getSelectedCategories(): List<String>
    suspend fun saveSelectedCategories(categories: List<String>)
    suspend fun isAlreadySelected(): Boolean
}