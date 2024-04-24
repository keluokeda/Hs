package com.ke.hs

import android.app.Application
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Logger.addLogAdapter(AndroidLogAdapter())

        Logger.d("MainApplication onCreate")

//        FileService.bindService(this)
    }
}

val String.tileImage
    get() = "https://art.hearthstonejson.com/v1/tiles/${this}.png"