package com.ke.hs.lib.set.wild_west

import com.ke.hs.lib.card.MinionCard
import com.ke.hs.lib.card.MinionRace

/**
 * 源质晶簇
 */
class ElementiumGeode : MinionCard() {
    override val attack: Int = 2
    override val health: Int = 1
    override val races: List<MinionRace> = listOf(MinionRace.Elemental)
    override val set: String = "WILD_WEST"
    override val id: String = "DEEP_030"


}