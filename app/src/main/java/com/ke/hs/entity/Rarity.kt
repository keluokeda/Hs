package com.ke.hs.entity


import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import com.ke.hs.R
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

enum class Rarity(val title: String, @ColorRes val colorRes: Int) {

    /**
     * 免费
     */
    Free("免费", R.color.module_rarity_common),

    /**
     * 普通
     */
    Common("普通", R.color.module_rarity_common),

    /**
     * 稀有
     */
    Rare("稀有", R.color.module_rarity_rare),

    /**
     * 史诗
     */
    Epic("史诗", R.color.module_rarity_epic),

    /**
     * 传说
     */
    Legendary("传说", R.color.module_rarity_legendary)
}

class RarityAdapter {


    @FromJson
    fun fromJson(value: String): Rarity {
        return EnumMoshiAdapter.fromJson(
            value, Rarity.entries.toTypedArray()
        )
    }

    @ToJson
    fun toJson(value: Rarity) = EnumMoshiAdapter.toJson(value)
}