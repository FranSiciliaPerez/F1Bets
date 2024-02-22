package com.example.f1bets.activities

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.f1bets.R
import com.google.firebase.FirebaseApp

class SplashStartActivity : AppCompatActivity() {

    private lateinit var md: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         FirebaseApp.initializeApp(this);

        // Initialize the MediaPlayer with the audio file
        md = MediaPlayer.create(this, R.raw.f1)
        // Play the audio
        md.start()

        // Initialize the activity after a little time
        md.setOnCompletionListener {
            startActivity(Intent(this, StartActivity::class.java))
            finish() // Avoid that the splash screen is shown again after been redirected to the other activity
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Free the resources of the MediaPlayer when the activity is destroyed
        md.release()
    }
}