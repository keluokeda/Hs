package com.ke.hs.module.entity


import com.ke.hs.module.db.entity.Game

/**
 * 游戏事件
 */
sealed interface GameEvent {


    /**
     * 游戏开始
     */
    data object OnGameStart : GameEvent

    /**
     * 游戏结束
     */
    data class OnGameOver(
        val game: Game
    ) : GameEvent


    /**
     * 插入一张卡牌到用户的牌库
     */
    data class InsertCardToUserDeck(
        val cardId: String
    ) : GameEvent

    /**
     * 从牌库中移除一张卡牌
     */
    data class RemoveCardFromUserDeck(
        val cardId: String
    ) : GameEvent

    /**
     * 插入一张卡牌到墓地
     */
    data class InsertCardToGraveyard(val cardId: String, val isUser: Boolean) : GameEvent
}