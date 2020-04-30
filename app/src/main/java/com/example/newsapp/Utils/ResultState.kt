package com.example.newsapp.Utils

sealed class ResultState{
    object Success: ResultState() // this is object because I added no params
    data class Failure(val message: String): ResultState()
}