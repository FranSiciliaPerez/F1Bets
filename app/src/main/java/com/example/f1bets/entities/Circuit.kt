package com.example.f1bets.entities

import java.io.Serializable

data class Circuit (
    val country: String = "",
    val laps: String = "",
    val length: String = "",
    val nameCircuit: String = "",
    val picture: String? = null
) : Serializable