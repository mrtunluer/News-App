package com.mertdev.yournews.data.remote.dto

import com.mertdev.yournews.domain.model.Article

data class ArticleDto(
    val author: String?,
    val content: String?,
    val description: String?,
    val publishedAt: String?,
    val sourceDto: SourceDto?,
    val title: String?,
    val url: String?,
    val urlToImage: String?
) {
    fun toDomainModel() = Article(
        author = author,
        content = content,
        description = description,
        publishedAt = publishedAt,
        title = title,
        url = url,
        urlToImage = urlToImage
    )
}