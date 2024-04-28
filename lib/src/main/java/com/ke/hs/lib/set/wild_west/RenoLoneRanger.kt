package com.ke.hs.lib.set.wild_west

import com.ke.hs.lib.card.CardClass
import com.ke.hs.lib.card.CardRarity
import com.ke.hs.lib.card.CardSet
import com.ke.hs.lib.card.HeroCard
import com.ke.hs.lib.card.mechanics.Mechanics

/**
 * 孤胆游侠雷诺
 */
class RenoLoneRanger : HeroCard() {
    override val armor = 5
    override val cost = 9
    override val rarity = CardRarity.Legendary
    override val set = CardSet.WildWest
    override val id = "WW_0700"
    override val cardClass: CardClass = CardClass.Neutral

    override val mechanics: List<Mechanics>
        get() = listOf(Mechanics.Battlecry)
}