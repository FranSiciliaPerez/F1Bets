package com.example.f1bets

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp

class SplashStartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         FirebaseApp.initializeApp(this);
         startActivity(Intent(this, StartActivity::class.java))
        finish()//para evitar que pulsando el boton de atrás buelva a verse el splash
    }


}