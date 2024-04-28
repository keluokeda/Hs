package com.ke.hs.lib.card

interface ICard {
    /**
     * 名称
     */
    val name: String



    /**
     * 描述
     */
    val text: String

    /**
     * 集合
     */
    val set: CardSet


    /**
     * id
     */
    val id: String


    val cardClass: CardClass

    val classes: List<CardClass>
}