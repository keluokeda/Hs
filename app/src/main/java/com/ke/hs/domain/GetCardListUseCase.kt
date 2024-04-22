package com.ke.hs.domain


import com.ke.hs.api.HearthStoneJsonApi
import com.ke.hs.entity.Card
import javax.inject.Inject

class GetCardListUseCase @Inject constructor(

    private val api: HearthStoneJsonApi
) {

    suspend fun execute(): List<Card> {
        return api.getCardJsonList()
    }
}