package com.ke.hs.lib.set.core

import com.ke.hs.lib.card.AbsCard
import com.ke.hs.lib.card.ICard
import com.ke.hs.lib.card.MinionRace
import com.ke.hs.lib.card.mechanics.Elusive

class FaerieDragon : AbsCard(), Elusive {

    override val id: String
        get() = "NEW1_023"



    override val set: String
        get() = "common"

    /**
     * 费用
     */
    val cost: Int = 2

    /**
     * 攻击力
     */
    val attack: Int = 3

    /**
     * 生命
     */
    val health: Int = 2

    /**
     * 随从种族
     */
    val races: List<MinionRace> = listOf(MinionRace.Dragon)

    override var elusiveEnable: Boolean = true
}