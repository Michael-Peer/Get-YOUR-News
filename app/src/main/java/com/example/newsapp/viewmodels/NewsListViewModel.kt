package com.example.newsapp.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.newsapp.databases.getDatabase
import com.example.newsapp.repositories.NewsRepository

class NewsListViewModel(application: Application) : ViewModel() {

    //repository
    private val newsRepo = NewsRepository(
        getDatabase(
            application
        )
    )

    val news = newsRepo.news
}