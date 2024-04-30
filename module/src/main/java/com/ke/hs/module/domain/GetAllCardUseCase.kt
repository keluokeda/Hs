package com.ke.hs.module.domain


import com.ke.hs.module.db.CardDao
import com.ke.hs.module.entity.Card
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetAllCardUseCase @Inject constructor(
    private val cardDao: CardDao
) {

    suspend fun execute(): List<Card> {
        return withContext(Dispatchers.IO) {
            cardDao.getAll()
        }
    }
}