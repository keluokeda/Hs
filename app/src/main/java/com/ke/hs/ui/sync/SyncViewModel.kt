package com.ke.hs.ui.sync

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.ke.hs.module.db.CardDao
import com.ke.hs.module.domain.GetCardListUseCase
import com.ke.hs.module.domain.InsertCardListToDatabaseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SyncViewModel @Inject constructor(
    private val getCardListUseCase: GetCardListUseCase,
    private val insertCardListToDatabaseUseCase: InsertCardListToDatabaseUseCase,
    private val cardDao: CardDao,
    @ApplicationContext private val context: Context
) :
    ViewModel() {
    private val _loading = MutableStateFlow(false)

    val loading: StateFlow<Boolean>
        get() = _loading

    suspend fun sync(): Boolean {
        return try {
            _loading.value = true
            cardDao.deleteAll()
            insertCardListToDatabaseUseCase.execute(getCardListUseCase.execute())
            _loading.value = false
            true
        } catch (e: Exception) {
            e.printStackTrace()
            _loading.value = false
            Toast.makeText(context, "同步失败", Toast.LENGTH_SHORT).show()
            false
        }

    }

}