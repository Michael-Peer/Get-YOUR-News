package com.example.newsapp.network

import com.example.newsapp.models.News
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://newsapi.org/"

private const val API_KEY = "934de238d46c4c0c88825b1c653a56d8" //TODO:Move to constants


private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .addCallAdapterFactory(CoroutineCallAdapterFactory()) //coroutine
    .baseUrl(BASE_URL)
    .build()

interface NewsApiService {
    //by country
    @GET("v2/top-headlines")
    fun getNews(
        @Query("country") country: String,
        @Query("apiKey") apiKey: String //TODO: Store const key and send
        ): Deferred<News>

    //by query
    @GET("v2/top-headlines")
    fun  getNewsByQuery(
        @Query("q") query: String,
        @Query("from") date: String = "2020-05-04", //TODO: calculate by minus week from current day for deafult
        @Query("sortBy") sortBy: String ="popularity",
        @Query("apiKey") apiKey: String //TODO: Store const key and send
    ): Deferred<News>
}

//only one instance
object NewsApi{
    val retrofitService: NewsApiService by lazy {
        retrofit.create(NewsApiService::class.java)
    }
}
