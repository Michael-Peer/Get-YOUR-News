package com.example.newsapp.models

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.newsapp.Utils.GithubTypeConverters

@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(news: News)

    @Query("select * from news")
    fun getNews(): LiveData<News>
}

@TypeConverters(GithubTypeConverters::class)
@Database(entities = [News::class], version = 3)
abstract class NewsDatabase : RoomDatabase() {
    abstract val news: NewsDao
}

private lateinit var INSTANCE: NewsDatabase

fun getDatabase(context: Context): NewsDatabase {
    synchronized(NewsDatabase::class) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE =
                Room.databaseBuilder(context.applicationContext, NewsDatabase::class.java, "news")
                    .fallbackToDestructiveMigration()
                    .build()
        }
    }
    return INSTANCE
}