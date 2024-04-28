package com.ke.hs.lib.set.battle_of_the_bands

import com.ke.hs.lib.card.CardClass
import com.ke.hs.lib.card.CardRarity
import com.ke.hs.lib.card.CardSet
import com.ke.hs.lib.card.SpellCard
import com.ke.hs.lib.card.SpellSchool

/**
 * 回荡混响
 */
class Reverberations : SpellCard() {
    override val spellSchool = SpellSchool.Shadow
    override val cost = 3
    override val rarity = CardRarity.Rare
    override val set = CardSet.BattleOfTheBands
    override val id = "JAM_031"
    override val cardClass = CardClass.Warlock

    override val classes: List<CardClass> = listOf(CardClass.Mage, CardClass.Warlock)
}