package com.mertdev.yournews.data.remote.repo

import com.mertdev.yournews.data.remote.NewsApiService
import com.mertdev.yournews.domain.model.Article
import com.mertdev.yournews.domain.repo.NewsRepository
import com.mertdev.yournews.helpers.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val newsApiService: NewsApiService
) : NewsRepository {
    override suspend fun getTopHeadlines(): Flow<Resource<List<Article>>> = flow {
        emit(Resource.Loading())
        try {
            val response = newsApiService.getTopHeadlines()
            emit(Resource.Success(response.articles.map { it.toDomainModel() }))
        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString()))
        }
    }

    override suspend fun getTopHeadlinesBySelectedCategory(category: String): Flow<Resource<List<Article>>> =
        flow {
            emit(Resource.Loading())
            try {
                val response = newsApiService.getTopHeadlinesBySelectedCategory(category)
                emit(Resource.Success(response.articles.map { it.toDomainModel() }))
            } catch (e: Exception) {
                emit(Resource.Error(e.message.toString()))
            }
        }

    override suspend fun searchNews(query: String): Flow<Resource<List<Article>>> = flow {
        emit(Resource.Loading())
        try {
            val response = newsApiService.searchNews(query)
            emit(Resource.Success(response.articles.map { it.toDomainModel() }))
        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString()))
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getTopHeadlinesBySelectedCategories(categories: List<String>): Flow<Resource<List<Article>>> =
        flow {
            emit(Resource.Loading())
            try {
                val articleLists = categories.asFlow().flatMapMerge { category ->
                    flow {
                        val response = newsApiService.getTopHeadlinesBySelectedCategory(category)
                        emit(response.articles.map { it.toDomainModel() })
                    }
                }.toList()
                val articles = articleLists.flatten().shuffled()
                emit(Resource.Success(articles))
            } catch (e: Exception) {
                emit(Resource.Error(e.message.toString()))
            }
        }
}

