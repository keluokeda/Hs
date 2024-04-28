package com.ke.hs.lib.set.wild_west

import com.ke.hs.lib.card.CardClass
import com.ke.hs.lib.card.CardSet
import com.ke.hs.lib.card.EnchantmentCard

/**
 * 矿岩深凿
 */
class Deepmining : EnchantmentCard() {
    override val set = CardSet.WildWest
    override val id: String
        get() = "DEEP_020e"
    override val cardClass: CardClass
        get() = CardClass.Warrior


    override var text = "你的战吼会触发两次。"

    override var name = "矿岩深凿"

}