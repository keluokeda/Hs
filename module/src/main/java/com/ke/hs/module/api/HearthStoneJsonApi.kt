package com.ke.hs.module.api

import com.ke.hs.module.entity.Card
import retrofit2.http.GET
import retrofit2.http.Path

interface HearthStoneJsonApi {


    /**
     * 获取卡牌数据
     */
    @GET("v1/{versionCode}/{region}/cards.json")
    suspend fun getCardJsonList(
        @Path("versionCode") versionCode: String = "latest",
        @Path("region") region: String = "zhCN",
    ): List<Card>


    companion object {
        const val BASE_URL = "https://api.hearthstonejson.com/"
    }
}