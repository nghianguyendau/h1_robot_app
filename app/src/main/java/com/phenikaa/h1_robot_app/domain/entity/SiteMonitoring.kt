package com.phenikaa.h1_robot_app.domain.entity

data class SiteMonitoring(
    val time: String,
    val dir: String,
    val coll: String,
    val movingState: String,
    val area: Long,
    val cur: Int,
    val adv: Int,
    val door: Boolean,
    val liftSide: Int,
    val state: String,
    val landing: Int,
    val liftMode: Int,
    val nominalSpeed: Int,
    val decks: List<Deck>,
)