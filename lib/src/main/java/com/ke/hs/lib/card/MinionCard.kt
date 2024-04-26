package com.ke.hs.lib.card

abstract class MinionCard : AbsCard() {
    /**
     * 攻击力
     */
    abstract val attack: Int

    /**
     * 生命值
     */
    abstract val health: Int

    /**
     * 随从种族
     */
    abstract val races: List<MinionRace>
}

