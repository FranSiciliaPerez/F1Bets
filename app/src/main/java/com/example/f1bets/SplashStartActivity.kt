package com.example.f1bets

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp

class SplashStartActivity : AppCompatActivity() {

    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         FirebaseApp.initializeApp(this);

        // Inicializa el MediaPlayer con el archivo de audio
        mediaPlayer = MediaPlayer.create(this, R.raw.f1)
        // Reproduce el audio
        mediaPlayer.start()

        // Inicia la actividad principal después de un breve retraso
        mediaPlayer.setOnCompletionListener {
            startActivity(Intent(this, StartActivity::class.java))
            finish() // Evitar que pulsar el botón de atrás vuelva a mostrar la pantalla de Splash
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Libera los recursos del MediaPlayer cuando la actividad se destruye
        mediaPlayer.release()
    }
}