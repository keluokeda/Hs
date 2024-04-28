package com.ke.hs.lib.set.wild_west

import com.ke.hs.lib.card.CardClass
import com.ke.hs.lib.card.CardRarity
import com.ke.hs.lib.card.CardSet
import com.ke.hs.lib.card.MinionCard
import com.ke.hs.lib.card.MinionRace

/**
 * 源质晶簇
 */
class ElementiumGeode : MinionCard() {
    override val attack: Int = 2
    override val health: Int = 1
    override val races: List<MinionRace> = listOf(MinionRace.Elemental)
    override val cost: Int
        get() = 2
    override val rarity: CardRarity
        get() = CardRarity.Rare
    override val set = CardSet.WildWest
    override val id: String = "DEEP_030"
    override val cardClass: CardClass
        get() = CardClass.Warlock


}