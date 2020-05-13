package com.example.newsapp

import android.app.Application
import android.util.Log
import androidx.work.*
import com.example.newsapp.workers.RefreshWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class NewsApplication: Application() {

    private val  appScope = CoroutineScope(Dispatchers.Default)

    private fun dealyStart() {
        appScope.launch {
            setupRecWork()
        }
    }

    //Setup to work
    private fun setupRecWork() {
        //run once a day
//        val repeatedReq = PeriodicWorkRequestBuilder<RefreshWorker>(1, TimeUnit.DAYS).build()
        Log.i("setupRecWork", "setupRecWork")
        //when work should run
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.METERED)
            .setRequiresBatteryNotLow(true)
            .setRequiresCharging(true)
            .build()

        val repeatedReq = PeriodicWorkRequestBuilder<RefreshWorker>(30, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()


        //regulary/periodic basis
        WorkManager.getInstance().enqueueUniquePeriodicWork(
            RefreshWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP, // do not replace with other worker, not relevent to this app anyway
            repeatedReq)
    }

    override fun onCreate() {
        Log.i("NewsApplication", "Application created")
        super.onCreate()
        dealyStart()
        Log.i("NewsApplication", "Application started")

    }
}