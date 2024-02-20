package com.example.f1bets.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.f1bets.databinding.ActivityStartBinding
import com.google.firebase.auth.FirebaseAuth

class StartActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityStartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Firebase auth
        auth = FirebaseAuth.getInstance()
    }
    override fun onStart(){
        auth = FirebaseAuth.getInstance()
        val loggedUser = auth.currentUser
        if(loggedUser != null){
            val i = Intent(this@StartActivity, MainActivity::class.java)
            startActivity(i)
        }
        super.onStart()
    }
}