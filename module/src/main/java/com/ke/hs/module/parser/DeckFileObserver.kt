package com.ke.hs.module.parser


import android.os.Looper
import com.ke.hs.module.entity.CurrentDeck
import com.orhanobut.logger.Logger
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.Calendar
import java.util.Date

/**
 * deck文件观察者
 */
class DeckFileObserver constructor(
    private val interval: Long = 2000,
    private val deckFileTextProvider: suspend () -> String?,
) {
    private var oldLogSize = 0L


    fun reset() {
        oldLogSize = 0
    }

    suspend fun start(): Flow<CurrentDeck> = flow {

        if (Thread.currentThread() == Looper.getMainLooper().thread) {
            throw RuntimeException("不能运行在主线程")
        }

        while (true) {
            deckFileTextProvider()?.reader()?.apply {
                if (oldLogSize > 0) {
                    skip(oldLogSize)
                }
                val text = readText()
                oldLogSize += text.length
                val lines = text.lines()
                    .filter { it.isNotEmpty() }

                listToDeck(lines)?.apply {
                    emit(this)
                }
                close()
            }
            delay(interval)
        }
    }

    private fun listToDeck(list: List<String>): CurrentDeck? {
        if (list.isEmpty()) {
            return null
        }
        val contentList = list.map {
            it.removeTime().third
        }
        val name = contentList.findLast {
            it.startsWith("###", true)
        } ?: return null

        val target =
            contentList.subList(contentList.indexOf(name), contentList.size).toMutableList()
        target.removeFirst()
        target.removeFirst()
        val code = target.removeFirst()

        return CurrentDeck(
            name.replace("### ", ""), code
        )
    }
}


fun String.removeTime(): Triple<String, Date, String> {

    val content = substring(PowerParserImpl.TIME_PREFIX_SIZE)
    val start = substring(0, 1)
    val hms = substring(2, 10).split(":")
    val calendar = Calendar.getInstance()
    calendar.set(
        Calendar.HOUR_OF_DAY, hms[0].toInt()
    )
    calendar.set(
        Calendar.MINUTE, hms[1].toInt()
    )
    calendar.set(
        Calendar.SECOND, hms[2].toInt()
    )

    return Triple(
        start,
        calendar.time,
        content
    )
}


fun String.log() {
    Logger.d(this)
}
