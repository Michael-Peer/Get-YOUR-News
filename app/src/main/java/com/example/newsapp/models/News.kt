package com.example.newsapp.models


import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.newsapp.Utils.GithubTypeConverters
import com.google.gson.annotations.SerializedName

@Entity(tableName = "news")
data class News(

    @SerializedName("articles")
//    @Embedded(prefix = "articles_")
    @TypeConverters(GithubTypeConverters::class)
    val articles: List<Article>,
    @SerializedName("status")
    val status: String? = "",
    @SerializedName("totalResults")
    val totalResults: Int? = 0
)
{
    @PrimaryKey(autoGenerate = true)
    var id: Int? = 0
}
