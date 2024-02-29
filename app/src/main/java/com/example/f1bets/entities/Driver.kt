package com.example.f1bets.entities

import java.io.Serializable

data class Driver(
    val id: String = "",
    val name: String = "",
    var picture: String? = null,
    val team: String = "",
    val yearBirth: Long = 0
) : Serializable
