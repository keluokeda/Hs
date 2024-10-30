package com.ke.hs.ui.permissions

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ke.hs.currentHsPackage
import com.ke.hs.module.db.CardDao
import com.ke.hs.module.entity.HsPackage
import com.ke.hs.setHsPackage
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PermissionsViewModel @Inject constructor(
    private val cardDao: CardDao,
    @ApplicationContext private val context: Context
) : ViewModel() {

    suspend fun hasCardData(): Boolean {
        return cardDao.getCount() > 0
    }

    val currentHsPackage = context.currentHsPackage.stateIn(
        viewModelScope, SharingStarted.Eagerly, HsPackage.Normal
    )

    fun setCurrentHsPackage(hsPackage: HsPackage) {
        viewModelScope.launch {
            context.setHsPackage(hsPackage)
        }
    }
}