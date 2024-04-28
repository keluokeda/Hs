package com.ke.hs.lib.set.core

import com.ke.hs.lib.card.CardClass
import com.ke.hs.lib.card.CardRarity
import com.ke.hs.lib.card.CardSet
import com.ke.hs.lib.card.MinionCard
import com.ke.hs.lib.card.MinionRace
import com.ke.hs.lib.card.mechanics.Elusive

/**
 * 精灵龙
 */
class FaerieDragon : MinionCard(), Elusive {

    override val id: String
        get() = "NEW1_023"
    override val cardClass: CardClass
        get() = CardClass.Neutral



    override val set = CardSet.Core

    /**
     * 费用
     */
    override val cost: Int = 2

    /**
     * 攻击力
     */
    override val attack: Int = 3

    /**
     * 生命
     */
    override val health: Int = 2

    /**
     * 随从种族
     */
    override val races: List<MinionRace> = listOf(MinionRace.Dragon)

    override var elusiveEnable: Boolean = true

    override val rarity: CardRarity
        get() = CardRarity.Common
}