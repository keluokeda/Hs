package com.ke.hs.parser

import android.content.Context
import com.ke.hs.bugDataPrefix
import com.ke.hs.currentHsPackage
import com.ke.hs.module.parser.log
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.io.File
import javax.inject.Inject

interface FileTextProvider {
    fun provide(hsLogFile: HsLogFile): String?
}

enum class HsLogFile(val fileName: String) {
    Deck("Decks.log"),
    Power("Power.log")
}

class FileTextProviderImpl @Inject constructor(@ApplicationContext private val context: Context) :
    FileTextProvider {
    private fun readLocalFileText(fileName: String): String? {
        return try {
            val logDir = findLogDir()

            if (logDir == null) {
                "没有找到目标目录".log()
                return null
            }


            File("$logDir/$fileName").copyTo(
                File(context.getExternalFilesDir(null), fileName)
            )


            val file = File(context.getExternalFilesDir(null), fileName)
            file.readText()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

//    private fun findLogDir(): String? {
//        val hsPackage = runBlocking {
//            context.currentHsPackage.first()
//        }
//
//        val logsDir =
//            Environment.getExternalStorageDirectory().path + "/Android/data/${hsPackage.packageName}/files/Logs"
//
//
//        val listFiles =
//            FileService.getInstance()?.getFiles(
//                logsDir
//            ) ?: return null
//
//
//        val logDir = listFiles.filter {
//            it.contains("Hearthstone")
//        }.maxByOrNull {
//            FileService.getInstance()!!.lastModified(
//                it
//            )
//        }
//        return logDir
//    }


    private fun findLogDir(): String? {
        val hsPackage = runBlocking {
            context.currentHsPackage.first()
        }

        val logsDir =
            bugDataPrefix + "/${hsPackage.packageName}/files/Logs"


        val listFiles =
            File(logsDir).listFiles()!!


        val logDir = listFiles.filter {
            it.name.contains("Hearthstone")
        }.maxByOrNull {
            it.lastModified()
        }
        return logDir?.path
    }
    override fun provide(hsLogFile: HsLogFile): String? {
        return readLocalFileText(hsLogFile.fileName)
    }
}