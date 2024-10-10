package com.ke.hs.module.entity

enum class HsPackage(val index: Int, val description: String, val packageName: String) {
    Normal(1, "官网", "com.blizzard.wtcg.hearthstone"),

    Huawei(2, "华为", "com.blizzard.wtcg.hearthstone.cn.huawei"),

    Dashen(3, "网易大神", "com.blizzard.wtcg.hearthstone.cn.dashen"),

}