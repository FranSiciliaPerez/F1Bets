package com.example.f1bets

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.f1bets.databinding.ActivitySignUpBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        with(binding) {
            btnSignUp.setOnClickListener {
                val user = binding.textUser.text.toString()
                val email = binding.textEmail.text.toString()
                val password = binding.txtPassword2.text.toString()

                // Comprobamos si todos los campos tienen información llamando a la clase funciones
                if (Funciones.allFilled(user, email, password)) {
                    if (checkBox.isChecked){
                        firebaseAuth
                            .createUserWithEmailAndPassword(email, password)
                            .addOnSuccessListener {
                                    // If login is successful, redirect to the main activity
                                    val i = Intent(this@SignUpActivity, MainActivity::class.java)
                                    i.putExtra((R.string.succesLog.toString()), (R.string.succesLog.toString()))
                                    startActivity(i)
                                    finish()
                                }
                            .addOnFailureListener{
                                Snackbar.make(root, R.string.errEmail, Snackbar.LENGTH_LONG).show()
                            }
                    } else {
                        // Comprobamos si está marcado o no el checkBox para continuar ya que es obligatorio
                        Snackbar.make(root, R.string.msgCheckError, Snackbar.LENGTH_LONG)
                            .show()
                    }
                } else {
                    // Algún campo está vacío: mostramos error
                    Snackbar.make(root, R.string.errEmpty, Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }
}