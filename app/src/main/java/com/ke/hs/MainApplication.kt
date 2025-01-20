package com.ke.hs

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.ke.hs.module.ModuleApplication
import com.ke.hs.module.entity.HsPackage
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.tencent.bugly.crashreport.CrashReport
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.internal.Contexts.getApplication
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


@HiltAndroidApp
class MainApplication : ModuleApplication() {

    override fun onCreate() {
        super.onCreate()
        Logger.addLogAdapter(AndroidLogAdapter())

        Logger.d("MainApplication onCreate")

//        FileService.bindService(this)

        CrashReport.initCrashReport(this, "741b982487", BuildConfig.DEBUG)
        CrashReport.setDeviceModel(this, android.os.Build.MODEL)

    }
}

// At the top level of your kotlin file:
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

private val LogsEnable = booleanPreferencesKey("logs_enable")

suspend fun Context.setLogsEnable(enable: Boolean) {
    dataStore.edit { settings ->
        settings[LogsEnable] = enable
    }
}

/**
 * 日志保存功能是否开启
 */
val Context.logsEnable: Flow<Boolean>
    get() = this.dataStore.data
        .map { preferences ->
            // No type safety.
            preferences[LogsEnable] ?: false
        }

private val currentHsPackage = intPreferencesKey("current_hs_package")

val Context.currentHsPackage: Flow<HsPackage>
    get() = this.dataStore.data.map {
        it[com.ke.hs.currentHsPackage] ?: 1
    }.map { index ->
        HsPackage.entries.find {
            it.index == index
        }!!
    }


suspend fun Context.setHsPackage(hsPackage: HsPackage) {
    dataStore.edit {
        it[com.ke.hs.currentHsPackage] = hsPackage.index
    }
}


private val lastWindowWidthKey = intPreferencesKey("last_window_width")

val Context.lastWindowWidth: Flow<Int?>
    get() = this.dataStore.data.map {
        it[lastWindowWidthKey]
    }

suspend fun Context.setWindowWidth(width: Int) {
    dataStore.edit {
//        it[lastWindowWidthKey] = width
        it[lastWindowWidthKey] = width
    }
}

val String.tileImage
    get() = "https://art.hearthstonejson.com/v1/tiles/${this}.png"

val String.renderImage
    get() = "https://art.hearthstonejson.com/v1/render/latest/zhCN/512x/$this.png"

fun Context.checkAppInstalled(hsPackage: HsPackage = HsPackage.Normal): Boolean {
    return try {
        this.packageManager.getPackageInfo(hsPackage.packageName, 0)
        true
    } catch (e: Exception) {
        false
    }
}

