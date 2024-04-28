package com.ke.hs.lib.card

abstract class AbsCard : ICard {

    override var name: String = ""

    override var text: String = ""

    override val classes: List<CardClass> = emptyList()
}