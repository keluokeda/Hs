package com.ke.hs.lib.game

import com.ke.hs.lib.card.MinionCard
import com.ke.hs.lib.card.UsableCard

/**
 * 牌库
 */
interface IDeck {
    /**
     * 初始化牌库
     */
    fun init(cards: List<UsableCard>)

    /**
     * 插入卡牌到牌库
     */
    fun insert(cards: List<UsableCard>)

    /**
     * 抽一张牌
     */
    fun draw(): UsableCard?

    /**
     * 摧毁牌库
     */
    fun destroy()

    /**
     * 移出一个随从
     */
    fun removeMinion():MinionCard?

    /**
     * 移出一张指定费用的卡牌
     */
    fun removeCard(cost:Int):UsableCard

    /**
     * 复制一张卡牌
     */
    fun copeCard():UsableCard
}
//<b>战吼：</b>从你的牌库中召唤一只法力值消耗小于或等于（5）点的野兽。
//在你攻击后，从你的牌库中召唤一个传说随从。