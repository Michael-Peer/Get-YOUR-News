package com.example.newsapp.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.example.newsapp.Utils.ResultState
import com.example.newsapp.auth.FirebaseUserLiveData
import com.example.newsapp.databases.getDatabase
import com.example.newsapp.models.Article
import com.example.newsapp.models.News
import com.example.newsapp.repositories.NewsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.*

class MainViewModel(application: Application) : AndroidViewModel(application) {

    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _articles = MutableLiveData<List<Article>>()
    //expose articles
    val articles: LiveData<List<Article>>
        get() = _articles

    private val _totalResults = MutableLiveData<Int>()
    //expose results
    val totalResults: LiveData<Int>
        get() = _totalResults

//    private var _networkError = MutableLiveData<Boolean>(false)
//
//    val networkError: LiveData<Boolean>
//        get() = _networkError

    private var _isNetworkError = MutableLiveData<Boolean>(false)
    //expose networkError
    val isNetworkError: LiveData<Boolean>
        get() = _isNetworkError

    private var _stateLiveData = MutableLiveData<ResultState>()
    val stateLiveData: LiveData<ResultState>
        get() = _stateLiveData


    //repository
    private val newsRepo = NewsRepository(
        getDatabase(
            application
        )
    )

    val news = newsRepo.news

    enum class AuthState {
        AUTHENTICATED, UNAUTHENTICATED
    }


    //TODO: different viewModel
    val authenticationState = FirebaseUserLiveData().map { user ->
        if (user != null) {
            AuthState.AUTHENTICATED
        } else {
            AuthState.UNAUTHENTICATED
        }
    }


    init {
        Log.i("MainViewModel", "---init---")
        Log.i("MainViewModel", "---${_isNetworkError.value}---")
        refreshFromRepo()
//        coroutineScope.launch {
//            newsRepo.refreshNews()
//        }
    }


    //try to fetch from network. if not succesed, we already have data from chace
    //TODO: refresh strategy
    private fun refreshFromRepo() {
        Log.i("MainViewModel", "refreshFromRepo")
        coroutineScope.launch {
            Log.i("MainViewModel", "refreshFromRepo --- scope")
            try {
//                _networkError.value = false
                _isNetworkError.value = false
                Log.i("MainViewModel", "refreshFromRepo --- try")
                newsRepo.refreshNews()

            } catch (e: IOException) {
                Log.i("MainViewModel", "error: $e")
                _isNetworkError.value = true

            }
        }
    }

    fun onNetworkError() {
        _isNetworkError.value = true
    }

    //extract data from main model
    fun extractData(news: News) {
//         Log.i("MainViewModel", "inside extractData function")
//         Log.i("MainViewModel", "----------------------------")
//         Log.i("MainViewModel", "$news")
        _totalResults.value = news.totalResults
        _articles.value = news.articles
//         Log.i("MainViewModel", "----------------------------")
//         Log.i("MainViewModel", "totalResults: $_totalResults")
//         Log.i("MainViewModel", "----------------------------")
//         Log.i("MainViewModel", "articles: $_articles")


    }


    fun onSearchButtonClicked(countryName: String) {
        val countryCode = getCountryCodeFromCountryName(countryName.toLowerCase().capitalize())
        countryCode?.let {
            coroutineScope.launch {
                val resultState = newsRepo.insertToDatabase(countryCode)
                _stateLiveData.value = resultState
                Log.i("MainViewModel", "inside onSearchButtonClicked ${news.value}")

            }
        }
    }

    //convert to country code
    private fun getCountryCodeFromCountryName(countryName: String): String? {
        val countryCode = Locale.getISOCountries().find {
            Locale("", it).displayCountry == countryName
        }
        if (countryCode == null) {
            _stateLiveData.value =
                ResultState.Failure("We coudn't found any coutnry that match your input")
            return countryCode
        }
        Log.i("MainViewModel", "else $countryCode")
        return countryCode


    }


    //clear job and cancel coroutine
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }


}
