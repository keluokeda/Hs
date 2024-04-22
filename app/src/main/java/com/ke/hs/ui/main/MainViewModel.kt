package com.ke.hs.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ke.hs.db.GameDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val gameDao: GameDao
) : ViewModel() {

    val gameList = gameDao.getAll().stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
}