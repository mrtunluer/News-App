package com.mertdev.yournews.presentation.common

import com.mertdev.yournews.domain.model.Article

data class DataState(
    val articles: List<Article> = emptyList(),
    val selectedCategories: List<String> = emptyList(),
    val errorMessage: String? = null,
    val isLoading: Boolean = false
)