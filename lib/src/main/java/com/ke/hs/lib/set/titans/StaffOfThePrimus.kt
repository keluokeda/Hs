package com.ke.hs.lib.set.titans

import com.ke.hs.lib.card.CardClass
import com.ke.hs.lib.card.CardRarity
import com.ke.hs.lib.card.CardSet
import com.ke.hs.lib.card.WeaponCard

/**
 * 兵主之杖
 */
class StaffOfThePrimus : WeaponCard() {
    override val attack: Int
        get() = 1
    override val durability: Int
        get() = 3
    override val cost: Int
        get() = 1
    override val rarity: CardRarity
        get() = CardRarity.Common
    override val set = CardSet.Titans
    override val id: String
        get() = "TTN_736"
    override val cardClass: CardClass
        get() = CardClass.DeathKnight

}