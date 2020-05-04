package com.example.newsapp.repositories

import android.util.Log
import androidx.lifecycle.LiveData
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

    suspend fun insertToDatabaseByQuery(query: String, date: String, sortBy: String): ResultState {
        return withContext<ResultState>(Dispatchers.IO) {
            val dataList =
                NewsApi.retrofitService.getNewsByQuery(
                    query,
                    date,
                    sortBy,
                    "934de238d46c4c0c88825b1c653a56d8"
                ).await()
            if (dataList.totalResults < 1) {
                return@withContext ResultState.Failure("We could't find any data, please correct your input")
            } else {
                database.news.insertAll(dataList)
                return@withContext ResultState.Success
            }
        }
    }


}
