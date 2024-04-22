package com.ke.hs.ui.sync

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ke.hs.db.CardDao
import com.ke.hs.domain.GetAllCardUseCase
import com.ke.hs.domain.GetCardListUseCase
import com.ke.hs.domain.InsertCardListToDatabaseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SyncViewModel @Inject constructor(
    private val getCardListUseCase: GetCardListUseCase,
    private val insertCardListToDatabaseUseCase: InsertCardListToDatabaseUseCase,
    private val cardDao: CardDao
) :
    ViewModel() {
    private val _loading = MutableStateFlow(false)

    val loading: StateFlow<Boolean>
        get() = _loading

    suspend fun sync() {
        _loading.value = true
        cardDao.deleteAll()
        insertCardListToDatabaseUseCase.execute(getCardListUseCase.execute())
        _loading.value = false
    }

}