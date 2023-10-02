package com.mertdev.yournews.domain.repo

import com.mertdev.yournews.domain.entity.ArticleEntity
import kotlinx.coroutines.flow.Flow

interface SavedArticleRepository {
    fun getArticle(): Flow<List<ArticleEntity>>

    suspend fun insertArticle(articleEntity: ArticleEntity)
}