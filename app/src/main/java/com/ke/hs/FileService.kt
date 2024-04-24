package com.ke.hs

import android.content.ComponentName
import android.content.Context
import android.content.ServiceConnection
import android.os.IBinder
import com.orhanobut.logger.BuildConfig
import com.orhanobut.logger.Logger
import rikka.shizuku.Shizuku
import java.io.File
import java.io.IOException
import kotlin.system.exitProcess

class FileService : IFileService.Stub() {
    override fun getFiles(path: String): MutableList<String> {
        return File(path).listFiles()?.map { it.path }?.toMutableList() ?: mutableListOf()
    }

    override fun createFile(path: String, name: String) {
        try {
            File(path, name).createNewFile()
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    override fun deleteFile(path: String): Boolean {
        return File(path).delete()
    }

    override fun moveFile(origin: String, target: String) {
        Runtime.getRuntime().exec("mv $origin $target").waitFor()

    }

    override fun fileSize(path: String): Long {
        return File(path).length()
    }

    override fun clearFile(path: String) {
        File(path).writeText("")
    }

    override fun copyFile(origin: String, target: String) {
        Runtime.getRuntime().exec("cp $origin $target").waitFor()
    }


    override fun lastModified(path: String): Long {
        return File(path).lastModified()
    }


    override fun copyAndClearFile(origin: String, target: String): Boolean {
        copyFile(origin, target)
//        Thread.sleep(1000)
        clearFile(origin)
        return true
    }


    companion object {
        private var instance: IFileService? = null
        private val SERVICE_CONNECTION: ServiceConnection = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName, service: IBinder) {
                Logger.d("onServiceConnected")
                instance = asInterface(service)
            }

            override fun onServiceDisconnected(name: ComponentName) {
                Logger.d("onServiceDisconnected")
                instance = null
//                exitProcess(0)
                System.exit(0)
            }
        }

        fun getInstance(): IFileService? {
            return instance
        }

        fun bindService(context: Context) {
            Shizuku.bindUserService(
                Shizuku.UserServiceArgs(
                    ComponentName(context, FileService::class.java.getName())
                ).daemon(false).debuggable(BuildConfig.DEBUG)
                    .processNameSuffix("file_lastModified_service").version(36),
                SERVICE_CONNECTION
            )
        }
    }
}