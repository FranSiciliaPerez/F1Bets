package com.example.f1bets

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class StartActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        auth = FirebaseAuth.getInstance()

        val btnLogin: Button = findViewById(R.id.btnLogin)
        val btnSingUp: Button = findViewById(R.id.btnSingUp)
        val emailEditText: TextInputEditText = findViewById(R.id.email)
        val passwordEditText: TextInputEditText = findViewById(R.id.password)

        btnLogin.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            signIn(email, password)
        }
        btnSingUp.setOnClickListener {
            val i = Intent(this@StartActivity, SignUpActivity::class.java)
            startActivity(i)
        }
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

    private fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
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