package com.example.newsapp.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.newsapp.databases.getDatabase
import com.example.newsapp.repositories.NewsRepository
import retrofit2.HttpException

class RefreshWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    companion object {
        const val WORK_NAME = "RefreshWorker"
    }
    //Maximum 10m
    override suspend fun doWork(): Result {
        Log.i("NewsApplication", "Refresh is running before")

        val database = getDatabase(applicationContext)
        val repo = NewsRepository(database)

        try {
            Log.i("NewsApplication", "Refresh is running before")
            repo.refreshNews()
            Log.i("NewsApplication", "Refresh is running")
        } catch (http: HttpException) {
            //retry
            Log.i("NewsApplication", "Refresh retry")
            return Result.retry()
        }
        return Result.success()
    }
}