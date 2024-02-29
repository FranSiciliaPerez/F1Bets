package com.example.f1bets.entities

import java.io.Serializable

data class Bets (
    val id: String = "",
    val idCircuit: String = "",
    val idDriver: String = "",
    val betMoney: Long = 0,
) : Serializable
