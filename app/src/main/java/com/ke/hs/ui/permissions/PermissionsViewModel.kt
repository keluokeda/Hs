package com.ke.hs.ui.permissions

import androidx.lifecycle.ViewModel
import com.ke.hs.module.db.CardDao
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PermissionsViewModel @Inject constructor(private val cardDao: CardDao) : ViewModel() {

    suspend fun hasCardData(): Boolean {
        return cardDao.getCount() > 0
    }
}