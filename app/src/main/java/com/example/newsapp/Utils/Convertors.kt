package com.example.newsapp.Utils

import androidx.room.TypeConverter
import com.example.newsapp.models.Article
import com.example.newsapp.models.Source
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class GithubTypeConverters {
        @TypeConverter
        fun stringToNews(data:String):List<Article> {
             val gson = Gson()
            val listType = object:TypeToken<List<Article>>() {
            }.getType()
            return gson.fromJson(data, listType)
        }
        @TypeConverter
        fun newsToString(someObjects:List<Article>):String {
            val gson = Gson()
            return gson.toJson(someObjects)
        }

    @TypeConverter
    fun stringToSource(data:String):List<Source> {
        val gson = Gson()
        val listType = object:TypeToken<List<Article>>() {
        }.getType()
        return gson.fromJson(data, listType)
    }
    @TypeConverter
    fun sourceToString(someObjects:List<Source>):String {
        val gson = Gson()
        return gson.toJson(someObjects)
    }
    }

