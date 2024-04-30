package com.ke.hs.module.entity


data class GraveyardCard(
    val card: Card,
    /**
     * 插入时间
     */
    val time: Long = System.currentTimeMillis()
)