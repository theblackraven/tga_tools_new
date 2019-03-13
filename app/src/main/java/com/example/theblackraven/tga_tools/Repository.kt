package com.example.theblackraven.tga_tools

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.support.annotation.WorkerThread



class AppsRepository(private val context: Context) {

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    val db = TGA_RoomDatabase.getDatabase(context)
    val DaoApps = db.DaoApps()
    val Apps = DaoApps.getAllApps_LiveData()

    fun getAllApps() : LiveData<List<Apps>>  {
        return Apps
    }

    // You must call this on a non-UI thread or your app will crash. So we're making this a
    // suspend function so the caller methods know this.
    // Like this, Room ensures that you're not doing any long running operations on the main
    // thread, blocking the UI.



}

