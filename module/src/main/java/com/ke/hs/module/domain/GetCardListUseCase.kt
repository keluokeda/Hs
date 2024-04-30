package com.ke.hs.module.domain


import com.ke.hs.module.api.HearthStoneJsonApi
import com.ke.hs.module.entity.Card
import javax.inject.Inject

class GetCardListUseCase @Inject constructor(

    private val api: HearthStoneJsonApi
) {

    suspend fun execute(): List<Card> {
        return api.getCardJsonList()
    }
}