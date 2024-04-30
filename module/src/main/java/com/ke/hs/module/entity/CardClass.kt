package com.ke.hs.module.entity

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.ke.hs.module.R
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

enum class CardClass(
    @StringRes
    val titleRes: Int,
    @ColorRes
    val color: Int = 0,

    @DrawableRes
    val roundIcon: Int? = null,
    val isHero: Boolean = true
) {
    /**
     * 法师
     */
    Mage(R.string.module_mage, R.color.module_mage, R.drawable.mage),

    /**
     * 术士
     */
    Warlock(R.string.module_warlock, R.color.module_warlock, R.drawable.warlock),

    /**
     * 牧师
     */
    Priest(R.string.module_priest, R.color.module_priest, R.drawable.priest),

    /**
     * 德鲁伊
     */
    Druid(R.string.module_druid, R.color.module_druid, R.drawable.druid),

    /**
     * 盗贼
     */
    Rogue(R.string.module_rogue, R.color.module_rogue, R.drawable.rogue),

    /**
     * 萨满
     */
    Shaman(R.string.module_shaman, R.color.module_shaman, R.drawable.shaman),


    /**
     * 猎人
     */
    Hunter(R.string.module_hunter, R.color.module_hunter, R.drawable.hunter),

    /**
     * 圣骑士
     */
    Paladin(R.string.module_paladin, R.color.module_paladin, R.drawable.paladin),

    /**
     * 战士
     */
    Warrior(R.string.module_warrior, R.color.module_warrior, R.drawable.warrior),

    /**
     * 恶魔猎手
     */
    DemonHunter(
        R.string.module_demon_hunter,
        R.color.module_demon_hunter,
        R.drawable.demonhunter
    ),

    /**
     * 死亡骑士
     */
    DeathKnight(
        R.string.module_death_knight,
        R.color.module_death_knight,
        R.drawable.deathknight,
    ),

    /**
     * 中立
     */
    Neutral(R.string.module_neutral, R.color.module_neutral, isHero = false),

    /**
     * 威兹班
     */
    Whizbang(R.string.module_whizbang, 0, isHero = false),

    /**
     * 梦境牌
     */
    Dream(R.string.module_dream, 0, isHero = false),


}

val userHeroClass = CardClass.entries.filter {
    it.isHero
}

class CardClassAdapter {
    @FromJson
    fun fromJson(value: String): CardClass {

        return EnumMoshiAdapter.fromJson(value, CardClass.entries.toTypedArray())

    }

    @ToJson
    fun toJson(cardClass: CardClass) = cardClass.name.uppercase()
}