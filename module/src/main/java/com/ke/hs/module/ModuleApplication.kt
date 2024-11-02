package com.ke.hs.module

import android.app.Application
import com.huawei.agconnect.AGConnectInstance
import com.huawei.agconnect.remoteconfig.AGConnectConfig

abstract class ModuleApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        if (AGConnectInstance.getInstance() == null) {
            AGConnectInstance.initialize(this)
        }


        AGConnectConfig.getInstance().applyDefault(mapOf("baseUrl" to "https://baidu.com"))
    }
}