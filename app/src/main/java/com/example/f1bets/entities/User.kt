package com.example.f1bets.entities

import java.io.Serializable

data class User(
    var uid: String = "",
    var userName: String = "",
    var email: String = "",
    var password: String = "",
    var picture: String? = null
): Serializable
