package com.ke.hs.lib.card

/**
 * 法术卡牌
 */
abstract class SpellCard : UsableCard() {

    /**
     * 法术类型
     */
    abstract val spellSchool: SpellSchool?
}