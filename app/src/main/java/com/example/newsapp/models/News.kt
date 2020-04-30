package com.example.newsapp.models


import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.newsapp.Utils.MyTypeConvertors
import com.google.gson.annotations.SerializedName

//"Main" model
@Entity(tableName = "news")
data class News(
    @PrimaryKey(autoGenerate = false)
    val id: Int = 1,
    @SerializedName("articles")
//    @Embedded(prefix = "articles_")
    @TypeConverters(MyTypeConvertors::class)
    val articles: List<Article>,
    @SerializedName("status")
    val status: String? = "",
    @SerializedName("totalResults")
    val totalResults: Int = 0
)
{

}
