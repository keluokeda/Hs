package com.ke.hs.lib.card

import com.ke.hs.lib.card.mechanics.Mechanics

/**
 * 可以使用的卡牌
 */
abstract class UsableCard : AbsCard() {

    /**
     * 能被打出去的卡牌必须有费用
     */
    abstract val cost: Int

    /**
     * 关键字
     */
    open val mechanics: List<Mechanics> = emptyList()

    /**
     * 卡牌品质
     */
    abstract val rarity: CardRarity
}