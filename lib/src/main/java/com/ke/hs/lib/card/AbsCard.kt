package com.ke.hs.lib.card

import com.ke.hs.lib.set.core.FaerieDragon

abstract class AbsCard :
    ICard {

    override var name: String = ""

    //        private set
//
    override var text: String = ""

    override val classes: List<CardClass> = emptyList()
}

fun main() {
    val card = FaerieDragon()
}