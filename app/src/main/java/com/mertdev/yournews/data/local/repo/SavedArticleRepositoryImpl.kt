package com.mertdev.yournews.data.local.repo

import com.mertdev.yournews.data.local.data_source.SavedArticleDao
import com.mertdev.yournews.domain.entity.ArticleEntity
import com.mertdev.yournews.domain.repo.SavedArticleRepository
import kotlinx.coroutines.flow.Flow

class SavedArticleRepositoryImpl(private val dao: SavedArticleDao) : SavedArticleRepository {
    override fun getArticle(): Flow<List<ArticleEntity>> {
        return dao.getArticle()
    }

    override suspend fun insertArticle(articleEntity: ArticleEntity) {
        return dao.insertArticle(articleEntity)
    }
}