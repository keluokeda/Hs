package com.ke.hs.parser

import android.content.Context
import android.os.Environment
import com.ke.hs.FileService
import com.ke.hs.db.GameDao
import com.ke.hs.domain.GetAllCardUseCase
import com.ke.hs.domain.ParseDeckCodeUseCase
import com.ke.hs.entity.Card
import com.ke.hs.entity.CardBean
import com.ke.hs.entity.CardType
import com.ke.hs.entity.CurrentDeck
import com.ke.hs.entity.GameEvent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject

/**
 * 剩余卡牌监听器
 */
interface DeckCardObserver {

    /**
     * 牌库的卡牌
     */
    val deckCardList: StateFlow<List<CardBean>>

    /**
     * 自己的墓地
     */
    val userGraveyardCardList: StateFlow<List<CardBean>>

    /**
     * 对手的墓地
     */
    val opponentGraveyardCardList: StateFlow<List<CardBean>>

    /**
     * 初始化
     */
    fun init(scope: CoroutineScope)

    /**
     * 分析诊断
     */
    fun analytics(): String
}

class DeckCardObserverImpl @Inject constructor(
    private val powerParser: PowerParser,
    private val powerTagHandler: PowerTagHandler,
    private val getAllCardUseCase: GetAllCardUseCase,
    private val parseDeckCodeUseCase: ParseDeckCodeUseCase,
    private val gameDao: GameDao,
    @ApplicationContext private val context: Context,
) : DeckCardObserver {


//    private val _userGraveyardCardList = MutableStateFlow<List<GraveyardCard>>(emptyList())
//
//   override val userGraveyardCardList: StateFlow<List<GraveyardCard>>
//        get() = _userGraveyardCardList

    /**
     * 当前用户的卡组
     */
    private var currentUserDeck: CurrentDeck? = null

    private val _deckCardList =
        MutableStateFlow<List<CardBean>>(emptyList())
//        Channel<List<CardBean>>(capacity = Channel.CONFLATED)


    override val deckCardList: StateFlow<List<CardBean>>
        get() = _deckCardList

    private val _userGraveyardCardList = MutableStateFlow<List<CardBean>>(emptyList())

    override val userGraveyardCardList: StateFlow<List<CardBean>>
        get() = _userGraveyardCardList

    private val _opponentGraveyardCardList = MutableStateFlow<List<CardBean>>(emptyList())
    override val opponentGraveyardCardList: StateFlow<List<CardBean>>
        get() = _opponentGraveyardCardList


    private fun readLocalFileText(fileName: String): String? {
        return try {
            val logDir = findLogDir()

            if (logDir == null) {
                "没有找到目标目录".log()
                return null
            }

            FileService.getInstance()!!.copyFile(
                "$logDir/$fileName",
                File(context.getExternalFilesDir(null), fileName).path
            )


            val file = File(context.getExternalFilesDir(null), fileName)
            file.readText()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun findLogDir(): String? {
        val logsDir =
            Environment.getExternalStorageDirectory().path + "/Android/data/com.blizzard.wtcg.hearthstone/files/Logs"


        val listFiles =
            FileService.getInstance()?.getFiles(
                logsDir
            ) ?: return null


        val logDir = listFiles.filter {
            it.contains("Hearthstone")
        }.maxByOrNull {
            FileService.getInstance()!!.lastModified(
                it
            )
        }
        return logDir
    }

    /**
     * 所有卡牌
     */
    private var allCards = emptyList<Card>()

    /**
     * 当前卡组的卡牌
     */
    private var currentDeckList: List<CardBean> = emptyList()

    /**
     * 当前卡组剩余的卡牌
     */
    private var deckLeftCardList: List<CardBean> = listOf()


    private var gameEvent: GameEvent? = null

    override fun init(
        scope: CoroutineScope,
    ) {
        val interval = 1500L

        scope.launch {
            clearPowerLogFile()
        }

        scope.launch {
            ///获取所有卡牌
            allCards = getAllCardUseCase.execute()
        }

        val deckFileObserver = DeckFileObserver(interval) {
            readLocalFileText("Decks.log")
        }

        val powerFileObserver = PowerFileObserver(interval) {
            readLocalFileText("Power.log")
        }

        scope.launch {
            //监听牌库
            delay(1000)
            deckFileObserver
                .start()
                .flowOn(Dispatchers.IO)
                .map {
                    currentUserDeck = it
                    parseDeckCodeUseCase.execute(it.code).first
                }.collect {
                    currentDeckList = it
                    _deckCardList.value = it.toList()
//                _deckCardList.send(it)
                }
        }



        scope.launch {
            powerTagHandler.gameEventFlow.collect {
                gameEvent = it
                when (it) {
                    null -> {

                    }

                    is GameEvent.OnGameOver -> {

                        _userGraveyardCardList.value = emptyList()
                        _opponentGraveyardCardList.value = emptyList()

                        clearPowerLogFile()

                        deckLeftCardList = currentDeckList.toList()
                        _deckCardList.value = deckLeftCardList.toList()
//                        _deckCardList.send(deckLeftCardList)


                        it.game.apply {
                            userDeckCode = currentUserDeck?.code ?: ""
                            userDeckName = currentUserDeck?.name ?: ""
                            scope.launch {
                                gameDao.insert(this@apply)
                            }
                        }




                        powerFileObserver.reset()
                    }

                    GameEvent.OnGameStart -> {

                        _userGraveyardCardList.value = emptyList()
                        _opponentGraveyardCardList.value = emptyList()
//                        deckLeftCardList = currentDeckList
//                        "清空卡牌 OnGameStart ,deckLeftCardList ${deckLeftCardList.size} , currentDeckList ${currentDeckList.size}".log()
//                        deckLeftCardList.clear()
//                        deckLeftCardList.addAll(currentDeckList)
                        deckLeftCardList = currentDeckList.toList()

                        _deckCardList.value = deckLeftCardList.toList()
//                        _deckCardList.send(deckLeftCardList)
                    }

                    is GameEvent.RemoveCardFromUserDeck -> {
                        onUserDeckCardListChanged(it.cardId, true)
                    }

                    is GameEvent.InsertCardToUserDeck -> {
                        onUserDeckCardListChanged(it.cardId, false)
                    }

                    is GameEvent.InsertCardToGraveyard -> {
                        onGraveyardCardsChanged(it.cardId, it.isUser)
                    }
                }
            }
        }

        powerParser.powerTagListener = {
            powerTagHandler.handle(it)
        }



        scope.launch {

            powerFileObserver.start()
                .flowOn(Dispatchers.IO)
                .collect { list ->
                    list.forEach {
                        powerParser.parse(it)
                    }
                }
        }

    }

    private fun onGraveyardCardsChanged(cardId: String, isUser: Boolean) {

        //TAG_CHANGE Entity=[entityName=UNKNOWN ENTITY [cardType=INVALID] id=106 zone=PLAY zonePos=0 cardId= player=1] tag=ZONE value=GRAVEYARD
        //TAG_CHANGE Entity=[entityName=UNKNOWN ENTITY [cardType=INVALID] id=5 zone=SECRET zonePos=0 cardId= player=1] tag=COST value=2
        //如果对面打出一张奥秘拍 会直接进入墓地
//            ?: throw RuntimeException("没有id $entity")

        val card = allCards.find {
            it.id == cardId
        } ?: return

        if (card.type == CardType.Enchantment) {
//            "衍生牌 $card 不能放到墓地去".log()
            return
        }

//        "插入一张牌到墓地 $card $entity".log()

        //TAG_CHANGE Entity=[entityName=破霰元素 id=62 zone=PLAY zonePos=1 cardId=AV_260 player=2] tag=ZONE value=GRAVEYARD
        if (isUser) {
            _userGraveyardCardList.value += CardBean(card, 1)
        } else {
            _opponentGraveyardCardList.value += CardBean(card, 1)
        }


    }

    /**
     * 清空log文件
     */
    private fun clearPowerLogFile() {

        val logDir = findLogDir() ?: return



        FileService.getInstance()!!.clearFile(File(logDir, "Power.log").path).apply {
            "删除文件 $logDir 结果 $this".log()
        }

        val localFile = File(context.getExternalFilesDir(null), "Power.log")
        if (localFile.exists()) {
            localFile.delete()
        }


    }

    override fun analytics(): String {


        return buildString {
            val logDir = findLogDir()
            appendLine("logDir $logDir")
            if (logDir != null) {
                val time = FileService.getInstance()!!.lastModified(logDir)
                appendLine("最后修改时间 ${simpleDateFormat.format(Date(time))}")

                appendLine(
                    "文件大小 ${
                        FileService.getInstance()!!.fileSize("$logDir/Power.log")
                    }"
                )
            }
            appendLine("游戏最新的事件 $gameEvent")
        }
    }

    /**
     * 用户牌库的卡牌发生了变化
     */
    private fun onUserDeckCardListChanged(cardId: String, remove: Boolean) {


        val card = allCards.find {
            it.id == cardId
        } ?: throw IllegalArgumentException("找不到id是 $cardId 的卡牌 , ${allCards.size}")



        if (card.type == CardType.Enchantment) {
            return
        }

//        "牌库的卡牌发生了变化 $card $remove ".log()

        val bean = deckLeftCardList.find {
            it.card.id == card.id
        }


        val list = mutableListOf<CardBean>()
        list.addAll(deckLeftCardList)


        if (bean == null) {
            list.add(CardBean(card, 1))
        } else {
//            bean.count =
            val newCount = if (remove) bean.count - 1 else bean.count + 1
            list[deckLeftCardList.indexOf(bean)] = bean.updateCount(newCount)
        }

//        if (bean?.count == 3) {
//            "插入了3张进去？ ".log()
//        }

        val newList = list.sortedBy {
            it.card.cost
        }.filter {
            it.count > 0
        }
//        deckLeftCardList.clear()
//        deckLeftCardList.addAll(newList)
        deckLeftCardList = newList
        _deckCardList.value = deckLeftCardList.toList()
//        _deckCardList.send(deckLeftCardList)
    }


}

private val simpleDateFormat = SimpleDateFormat("HH:mm:ss")