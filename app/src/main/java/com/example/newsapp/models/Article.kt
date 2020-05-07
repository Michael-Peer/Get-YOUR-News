package com.example.newsapp.models


import com.google.gson.annotations.SerializedName

data class Article(
    @SerializedName("author")
    val author: String? = null,
    @SerializedName("content")
    val content: Any? = null,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("publishedAt")
    val publishedAt: String? = null,
//    @Embedded(prefix = "source_")
    @SerializedName("source")
//    @TypeConverters(MyTypeConvertors::class)
    val source: Source? = null,
    @SerializedName("title")
    val title: String? = "Title unavailable",
    @SerializedName("url")
    val url: String? = null,
    @SerializedName("urlToImage")
    val urlToImage: String? = null
)