package com.example.newsapp.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.newsapp.Utils.Constans
import com.example.newsapp.Utils.ResultState
import com.example.newsapp.databases.NewsDatabase
import com.example.newsapp.models.News
import com.example.newsapp.network.NewsApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NewsRepository(private val database: NewsDatabase) {
    //get from dataBase
    val news: LiveData<News> = database.news.getNews()

    //insert to dataBase
    suspend fun refreshNews() {
        withContext(Dispatchers.IO) {
            val newsList =
                NewsApi.retrofitService.getNews("us", "934de238d46c4c0c88825b1c653a56d8").await()
            Log.i("NewsRepository", "$newsList")
            database.news.insertAll(newsList)
        }
    }

//    suspend fun refreshNews1(string: String) {
//        withContext(Dispatchers.IO) {
//            val newsList =
//                NewsApi.retrofitService.getNews(string, "934de238d46c4c0c88825b1c653a56d8").await()
//            if (newsList.totalResults <= 0) {
//                Log.i("NewsRepository", "inside if ${newsList.totalResults}")
//            } else {
//                Log.i("NewsRepository", "inside else  ${newsList.totalResults}")
//                Log.i("NewsRepository", "$newsList")
//                database.news.insertAll(newsList)
//            }
//        }
//    }

    suspend fun insertToDatabase(input: String): ResultState {
        return withContext<ResultState>(Dispatchers.IO) {
            val dataList =
                NewsApi.retrofitService.getNews(input, "934de238d46c4c0c88825b1c653a56d8").await()
            if (dataList.totalResults < 1) {
                return@withContext ResultState.Failure("We didn't find any data, please correct your input")
            } else {
                database.news.insertAll(dataList)
                return@withContext ResultState.Success
            }
        }
    }

    suspend fun insertToDatabaseByQuery(
        query: String,
        startDate: String,
        endDate: String,
        sortBy: String,
        selectedSearchType: String?,
        selectedCategory: String?
    ): ResultState {
        Log.i("insertToDatabaseByQuery", query)
        Log.i("insertToDatabaseByQuery", startDate)
        Log.i("insertToDatabaseByQuery", sortBy)
//        Log.i("insertToDatabaseByQuery", selectedSearchType)
        Log.i("insertToDatabaseByQuery", "selected catergory ${selectedCategory ?: "null"}")


        return withContext<ResultState>(Dispatchers.IO) {
            val dataList =
                if (selectedSearchType == Constans.ALL_NEWS) {
                    //TODO: No category in everything, notify user what to choose
                    NewsApi.retrofitService.getNewsByQueryEverything(
                        query,
                        startDate,
                        endDate,
                        sortBy,
                        Constans.API_KEY
                    ).await()
                } else {
                    NewsApi.retrofitService.getNewsByQueryTop(
                        query,
                        startDate,
                        endDate,
                        sortBy,
                        selectedCategory,
                        Constans.API_KEY
                    ).await()
                }
            if (dataList.totalResults < 1) {
                return@withContext ResultState.Failure("We could't find any data, please correct your input")
            } else {
                database.news.insertAll(dataList)
                return@withContext ResultState.Success
            }
        }
    }


}
