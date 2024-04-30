package com.ke.hs.module.entity


import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

/**
 * 法术类型
 */
enum class SpellSchool(val title: String) {
    /**
     * 奥数
     */
    Arcane("奥术"),

//    /**
//     * 冰霜
//     */
//    Freeze,

    /**
     * 冰霜
     */
    Frost("冰雹"),

    /**
     * 火焰
     */
    Fire("火焰"),

    /**
     * 自然
     */
    Nature("自然"),

    /**
     * 暗影
     */
    Shadow("暗影"),

    /**
     * 神圣
     */
    Holy("神圣"),

    /**
     * 邪能
     */
    Fel("邪能"),

    /**
     * 冲锋攻击
     */
    PhysicalCombat("冲锋攻击")
}

class SpellSchoolAdapter {
    @FromJson
    fun fromJson(value: String): SpellSchool? {

        return SpellSchool.entries.find { it.name.equals(value.replace("_", ""), true) }

    }

    @ToJson
    fun toJson(spellSchool: SpellSchool?) = spellSchool?.name?.uppercase()
}