package com.ke.hs.lib.card

/**
 * 武器牌
 */
abstract class WeaponCard : UsableCard(){

    /**
     * 攻击力
     */
    abstract val attack:Int


    /**
     * 耐久
     */
    abstract val durability:Int
}