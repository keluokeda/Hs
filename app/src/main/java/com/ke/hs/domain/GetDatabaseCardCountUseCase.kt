package com.ke.hs.domain


import com.ke.hs.db.CardDao
import javax.inject.Inject

class GetDatabaseCardCountUseCase @Inject constructor(
    private val cardDao: CardDao
) {

    suspend fun execute(parameters: Unit): Int {
        return cardDao.getCount()
    }
}