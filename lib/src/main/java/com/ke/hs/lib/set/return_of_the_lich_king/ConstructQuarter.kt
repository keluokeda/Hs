package com.ke.hs.lib.set.return_of_the_lich_king

import com.ke.hs.lib.card.CardClass
import com.ke.hs.lib.card.CardRarity
import com.ke.hs.lib.card.CardSet
import com.ke.hs.lib.card.LocationCard

/**
 * 构造区
 */
class ConstructQuarter : LocationCard() {
    override val health = 3
    override val cost = 3
    override val rarity = CardRarity.Rare
    override val set = CardSet.ReturnOfTheLichKing
    override val id = "NX2_036"
    override val cardClass = CardClass.DeathKnight
}