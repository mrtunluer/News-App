package com.mertdev.yournews.domain.usecase

import com.mertdev.yournews.domain.model.Article
import com.mertdev.yournews.domain.repo.NewsRepository
import com.mertdev.yournews.helpers.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTopHeadlines @Inject constructor(
    private val repo: NewsRepository
) {
    suspend operator fun invoke(): Flow<Resource<List<Article>>> {
        return repo.getTopHeadlines()
    }
}