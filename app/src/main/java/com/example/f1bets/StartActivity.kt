package com.example.f1bets

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class StartActivity : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        firebaseAuth = FirebaseAuth.getInstance()

        val btnLogin: Button = findViewById(R.id.btnLogin)
        val emailEditText: TextInputEditText = findViewById(R.id.email)
        val passwordEditText: TextInputEditText = findViewById(R.id.password)

        btnLogin.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            signIn(email, password)
        }
    }

    private fun signIn(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Autenticación exitosa, redirige a MainActivity
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish() // Cierra la actividad actual para que no pueda volver atrás con el botón de retroceso
                } else {
                    // Mostrar mensaje de error en caso de fallo
                    Snackbar.make(findViewById(android.R.id.content), "Inicio de sesión fallido", Snackbar.LENGTH_LONG).show()
                }
            }
    }
}