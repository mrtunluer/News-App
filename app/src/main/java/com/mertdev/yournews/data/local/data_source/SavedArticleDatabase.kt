package com.mertdev.yournews.data.local.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mertdev.yournews.domain.entity.ArticleEntity

@Database(
    entities = [ArticleEntity::class], version = 1
)
abstract class SavedArticleDatabase : RoomDatabase() {
    abstract val savedArticleDao: SavedArticleDao

    companion object {
        const val DB_NAME = "article_db"
    }
}