package com.ke.hs.lib.set.core

import com.ke.hs.lib.card.CardClass
import com.ke.hs.lib.card.CardRarity
import com.ke.hs.lib.card.CardSet
import com.ke.hs.lib.card.SpellCard
import com.ke.hs.lib.card.SpellSchool

/**
 * 寒冬号角
 */
class HornOfWinter : SpellCard() {
    override val spellSchool: SpellSchool
        get() = SpellSchool.Frost
    override val set = CardSet.Core
    override val id: String
        get() = "CORE_RLK_042"
    override val cardClass: CardClass
        get() = CardClass.DeathKnight


    override val cost: Int
        get() = 0
    override val rarity: CardRarity
        get() = CardRarity.Common

}