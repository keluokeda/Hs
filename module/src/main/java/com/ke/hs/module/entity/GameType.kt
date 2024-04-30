package com.ke.hs.module.entity


import androidx.annotation.StringRes
import com.ke.hs.module.R

enum class GameType(@StringRes val title: Int) {
    /**
     * 排名
     */
    Ranked(R.string.module_ranked),

    /**
     * 休闲
     */
    Casual(R.string.module_casual),

    Unknown(R.string.module_unknown)
}

val String.toGameType: GameType
    get() = GameType.entries.find {
        this.contains(it.name, true)
    } ?: GameType.Unknown