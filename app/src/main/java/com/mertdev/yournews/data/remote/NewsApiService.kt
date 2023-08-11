package com.mertdev.yournews.data.remote

import com.mertdev.yournews.data.remote.dto.NewsDto
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {

    @GET("top-headlines?country=us&pageSize=100")
    suspend fun getTopHeadlines(): NewsDto

    @GET("top-headlines?country=us&pageSize=100")
    suspend fun getTopHeadlinesBySelectedCategory(
        @Query("category") category: String,
    ): NewsDto

    @GET("everything")
    suspend fun searchNews(
        @Query("q") query: String,
    ): NewsDto

}