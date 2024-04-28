package com.ke.hs.lib.game

import com.ke.hs.lib.card.UsableCard

/**
 * 手牌
 */
interface IHand {
    /**
     * 最大手牌数量
     */
    var maxHandSize: Int

    fun insert(usableCard: UsableCard)

    fun use(usableCard: UsableCard)
}