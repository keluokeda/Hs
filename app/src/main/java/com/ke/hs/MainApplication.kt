package com.ke.hs

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.tencent.bugly.crashreport.CrashReport
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@HiltAndroidApp
class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Logger.addLogAdapter(AndroidLogAdapter())

        Logger.d("MainApplication onCreate")

//        FileService.bindService(this)

        CrashReport.initCrashReport(this, "741b982487", BuildConfig.DEBUG)
    }
}

// At the top level of your kotlin file:
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

val LogsEnable = booleanPreferencesKey("logs_enable")

/**
 * 日志保存功能是否开启
 */
val Context.logsEnable: Flow<Boolean>
    get() = this.dataStore.data
        .map { preferences ->
            // No type safety.
            preferences[LogsEnable] ?: false
        }

suspend fun Context.setLogsEnable(enable: Boolean) {
    dataStore.edit { settings ->
        settings[LogsEnable] = enable
    }
}

val String.tileImage
    get() = "https://art.hearthstonejson.com/v1/tiles/${this}.png"

val String.renderImage
    get() = "https://art.hearthstonejson.com/v1/render/latest/zhCN/512x/$this.png"