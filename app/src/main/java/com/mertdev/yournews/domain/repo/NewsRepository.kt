package com.mertdev.yournews.domain.repo

import com.mertdev.yournews.domain.model.Article
import com.mertdev.yournews.helpers.Resource
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    suspend fun getTopHeadlines(): Flow<Resource<List<Article>>>
    suspend fun getTopHeadlinesBySelectedCategories(categories: List<String>): Flow<Resource<List<Article>>>
    suspend fun getTopHeadlinesBySelectedCategory(category: String): Flow<Resource<List<Article>>>
    suspend fun searchNews(query: String): Flow<Resource<List<Article>>>
}