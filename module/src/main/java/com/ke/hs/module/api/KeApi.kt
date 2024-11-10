package com.ke.hs.module.api

import com.ke.hs.module.entity.Card
import retrofit2.http.GET

interface KeApi {
    @GET("cards")
    suspend fun cards(): List<Card>

    companion object{
        const val baseUrl  = "https://ke-api.cpolar.top/"
    }
}