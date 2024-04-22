package com.ke.hs.ui.chart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ke.hs.db.GameDao
import com.ke.hs.entity.CardClass
import com.ke.hs.entity.userHeroClass
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SummaryChartViewModel @Inject constructor(private val gameDao: GameDao) : ViewModel() {

    private val _opponentHeroCount = MutableStateFlow<List<Pair<CardClass, Int>>>(emptyList())

    val opponentHeroCount: StateFlow<List<Pair<CardClass, Int>>>
        get() = _opponentHeroCount

    init {
        viewModelScope.launch {
            gameDao.getAll().collect { list ->
                _opponentHeroCount.value = userHeroClass.map { cardClass ->
                    cardClass to list.count {
                        it.opponentHero == cardClass
                    }
                }.filter {
                    it.second > 0
                }

            }
        }
    }
}