package com.example.newsapp

import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import androidx.preference.PreferenceManager
import androidx.work.*
import com.example.newsapp.workers.RefreshWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class NewsApplication : Application() {


//    private val minutes = sharedPreferences.getBoolean("charge", true)

    private val appScope = CoroutineScope(Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()
        val sharedPreferences: SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val wifiRequired = sharedPreferences.getBoolean("wifi", true)
        val chargeRequired = sharedPreferences.getBoolean("charge", true)
        val minutes = sharedPreferences.getInt("minutes", 30)
        dealyStart(wifiRequired, chargeRequired, minutes)
        Log.i("NewsApplication", "Application started")
    }

    private fun dealyStart(wifiRequired: Boolean, chargeRequired: Boolean, minutes: Int) {
        appScope.launch {
            setupRecWork(wifiRequired, chargeRequired, minutes)
        }
    }

    //Setup to work
    private fun setupRecWork(wifiRequired: Boolean, chargeRequired: Boolean, minutes: Int) {
        //run once a day
//        val repeatedReq = PeriodicWorkRequestBuilder<RefreshWorker>(1, TimeUnit.DAYS).build()
        Log.i("setupRecWork", "setupRecWork")
        //when work should run
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(if (!wifiRequired) NetworkType.METERED else NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .setRequiresCharging(chargeRequired)
            .build()

        val repeatedReq = PeriodicWorkRequestBuilder<RefreshWorker>(30, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()


        //regulary/periodic basis
        WorkManager.getInstance().enqueueUniquePeriodicWork(
            RefreshWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP, // do not replace with other worker, not relevent to this app anyway
            repeatedReq
        )
    }


}