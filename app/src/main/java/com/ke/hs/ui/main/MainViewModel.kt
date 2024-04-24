package com.ke.hs.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ke.hs.db.GameDao
import com.ke.hs.db.entity.Game
import com.ke.hs.domain.ParseDeckCodeUseCase
import com.orhanobut.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val gameDao: GameDao,
    private val parseDeckCodeUseCase: ParseDeckCodeUseCase
) : ViewModel() {


    val title = "首页"


    init {

        Logger.d("MainViewModel init")
        viewModelScope.launch {
            gameDao.getAll().collect { list ->
                _records.value = list


                val deckSummaries = mutableListOf<DeckSummary>()

                list.groupBy {
                    it.userDeckName to it.userDeckCode
                }.forEach { (pair, l) ->
                    val winCount = l.count {
                        it.isUserWin == true
                    }
                    val lostCount = l.count {
                        it.isUserWin == false
                    }

                    val heroId = parseDeckCodeUseCase.execute(pair.second).second.card.id
                    deckSummaries.add(
                        DeckSummary(
                            pair.first,
                            pair.second,
                            winCount,
                            lostCount,
                            heroId
                        )
                    )

                }

                _deckSummaryList.value = deckSummaries
            }
        }
    }

//    val gameList = gameDao.getAll().stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    private val _records = MutableStateFlow<List<Game>>(emptyList())

    val records: StateFlow<List<Game>>
        get() = _records

    private val _deckSummaryList = MutableStateFlow<List<DeckSummary>>(emptyList())

    val deckSummaryList: StateFlow<List<DeckSummary>>
        get() = _deckSummaryList
}

data class DeckSummary(
    val name: String,
    val code: String,
    val winCount: Int,
    val lostCount: Int,
    val heroId: String
) {
    fun percent(): String {
        return (winCount.toFloat() / (winCount + lostCount) * 100).toString().substringBefore(".") + "%"

    }
}