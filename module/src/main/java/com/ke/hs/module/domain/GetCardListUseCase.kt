package com.ke.hs.module.domain


import com.ke.hs.module.api.HearthStoneJsonApi
import com.ke.hs.module.api.KeApi
import com.ke.hs.module.entity.Card
import javax.inject.Inject

class GetCardListUseCase @Inject constructor(

    private val api: HearthStoneJsonApi,
    private val keApi: KeApi
) {

    suspend fun execute(useKeApi: Boolean): List<Card> {
        return if (useKeApi) keApi.cards() else api.getCardJsonList()
    }
}