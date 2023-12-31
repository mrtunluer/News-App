package com.mertdev.yournews.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.mertdev.yournews.BuildConfig
import com.mertdev.yournews.data.remote.NewsApiService
import com.mertdev.yournews.data.local.repo.CategoryRepositoryImpl
import com.mertdev.yournews.data.remote.repo.NewsRepositoryImpl
import com.mertdev.yournews.domain.repo.CategoryRepository
import com.mertdev.yournews.domain.repo.NewsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.dataStore
    }

    @Provides
    @Singleton
    fun provideCategoryRepository(
        dataStore: DataStore<Preferences>
    ): CategoryRepository {
        return CategoryRepositoryImpl(dataStore)
    }

    @Provides
    @Singleton
    fun provideNewsRepository(
        newsApiService: NewsApiService
    ): NewsRepository {
        return NewsRepositoryImpl(newsApiService)
    }

    @Provides
    @Singleton
    fun provideNewsApiService(): NewsApiService {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val apiKeyInterceptor = Interceptor { chain ->
            val originalRequest = chain.request()
            val newUrl =
                originalRequest.url.newBuilder().addQueryParameter("apiKey", BuildConfig.API_KEY)
                    .build()
            val newRequest = originalRequest.newBuilder().url(newUrl).build()
            chain.proceed(newRequest)
        }

        val okHttpClient = OkHttpClient.Builder().addInterceptor(loggingInterceptor)
            .addInterceptor(apiKeyInterceptor).build()

        return Retrofit.Builder().baseUrl("https://newsapi.org/v2/").client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(NewsApiService::class.java)
    }

}

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "category_preferences")
