package com.mertdev.yournews.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.mertdev.yournews.domain.model.Article

data class ArticleDto(
    val author: String?,
    val content: String?,
    val description: String?,
    val publishedAt: String?,
    @SerializedName("source") val sourceDto: SourceDto?,
    val title: String?,
    val url: String?,
    val urlToImage: String?
) {
    fun toDomainModel() = Article(
        sourceName = sourceDto?.name,
        publishedAt = publishedAt,
        title = title,
        url = url,
        urlToImage = urlToImage
    )
}