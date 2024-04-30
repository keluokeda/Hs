package com.ke.hs.module.domain


import com.ke.hs.module.db.CardDao
import com.ke.hs.module.entity.Card
import javax.inject.Inject

class InsertCardListToDatabaseUseCase @Inject constructor(
    private val cardDao: CardDao
) {
    suspend fun execute(parameters: List<Card>) {
        cardDao.insert(parameters)
    }
}