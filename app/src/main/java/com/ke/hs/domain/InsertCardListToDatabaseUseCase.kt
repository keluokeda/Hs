package com.ke.hs.domain


import com.ke.hs.db.CardDao
import com.ke.hs.entity.Card
import javax.inject.Inject

class InsertCardListToDatabaseUseCase @Inject constructor(
    private val cardDao: CardDao
) {
    suspend fun execute(parameters: List<Card>) {
        cardDao.insert(parameters)
    }
}