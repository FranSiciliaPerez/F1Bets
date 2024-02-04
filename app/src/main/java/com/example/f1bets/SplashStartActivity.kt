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
        finish()// Evita que pulsando el botón de atrás aparezca otra vez el splash
    }


}