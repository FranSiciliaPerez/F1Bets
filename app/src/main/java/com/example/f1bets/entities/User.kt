package com.example.f1bets.entities

data class User(
    var userName: String,
    var email: String,
    var password: String,
    var picture: String? = null
)
