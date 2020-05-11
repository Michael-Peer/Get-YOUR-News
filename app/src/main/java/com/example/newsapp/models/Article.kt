package com.example.newsapp.models


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class Article(
    @SerializedName("author")
    val author: String? = null,
    @SerializedName("content")
    val content: @RawValue Any? = null,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("publishedAt")
    val publishedAt: String? = null,
//    @Embedded(prefix = "source_")
    @SerializedName("source")
//    @TypeConverters(MyTypeConvertors::class)
    val source: @RawValue Source? = null,
    @SerializedName("title")
    val title: String? = "Title unavailable",
    @SerializedName("url")
    val url: String? = null,
    @SerializedName("urlToImage")
    val urlToImage: String? = null
) : Parcelable