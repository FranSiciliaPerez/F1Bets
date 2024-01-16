package com.example.f1bets

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class SplashStartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         startActivity(Intent(this, StartActivity::class.java))
        finish()//para evitar que pulsando el boton de atrás buelva a verse el splash
    }


}