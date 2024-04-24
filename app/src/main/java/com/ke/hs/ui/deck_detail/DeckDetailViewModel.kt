package com.ke.hs.ui.deck_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ke.hs.db.GameDao
import com.ke.hs.db.entity.Game
import com.ke.hs.domain.ParseDeckCodeUseCase
import com.ke.hs.entity.CardBean
import com.ke.hs.entity.CardClass
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeckDetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val parseDeckCodeUseCase: ParseDeckCodeUseCase,
    private val gameDao: GameDao
) :
    ViewModel() {

    val name = savedStateHandle.get<String>("name")!!

    val code = savedStateHandle.get<String>("code")!!

    private val _deckCardList = MutableStateFlow<List<CardBean>>(emptyList())

    val deckCardBeanList: StateFlow<List<CardBean>>
        get() = _deckCardList

    private val _records = MutableStateFlow<List<Game>>(emptyList())

    val records: StateFlow<List<Game>>
        get() = _records


    private val _statistics = MutableStateFlow<List<Triple<CardClass, Int, Int>>>(emptyList())

    val statistics: StateFlow<List<Triple<CardClass, Int, Int>>>
        get() = _statistics

    init {
        viewModelScope.launch {
            _deckCardList.value = parseDeckCodeUseCase.execute(code).first
        }

        viewModelScope.launch {
            gameDao.getGameListByNameAndCode(name, code)
                .collect { list ->
                    _records.value = list

                    _statistics.value = list.groupBy {
                        it.opponentHero
                    }.map { map ->
                        Triple(
                            (map.key ?: CardClass.Neutral),
                            map.value.count { it.isUserWin == true },
                            map.value.count { it.isUserWin == false }
                        )
                    }.filter {
                        it.first.isHero
                    }
                }
        }
    }

    fun deleteGameRecord(game: Game) {
        viewModelScope.launch {
            gameDao.delete(game)
        }
    }
}
