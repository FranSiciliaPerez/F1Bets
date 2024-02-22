package com.example.f1bets.entities

import java.io.Serializable

data class Circuit (
    val id: Long = 0,
    val country: String = "",
    val laps: Long = 0,
    val length: Long = 0,
    val nameCircuit: String = "",
    val picture: String? = null
)  : Serializable