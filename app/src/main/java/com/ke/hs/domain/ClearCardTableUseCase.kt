package com.ke.hs.domain

import com.ke.hs.db.CardDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ClearCardTableUseCase @Inject constructor(
    private val cardDao: CardDao,
) {
    suspend fun execute() {
        withContext(Dispatchers.IO) {
            cardDao.deleteAll()
        }
    }
}