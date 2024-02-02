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
            btnRegister.setOnClickListener {
                val email = binding.txtEmail.text.toString()
                val pass = binding.txtPassword.text.toString()
                val confirmPass = binding.txtConfPas.text.toString()

                if (email.isNotEmpty() && pass.isNotEmpty() && confirmPass.isNotEmpty()) {
                    if (pass == confirmPass) {

                        firebaseAuth.createUserWithEmailAndPassword(email, pass)
                            .addOnCompleteListener {
                                if (it.isSuccessful) {
                                    // If login is successful, redirect to the main activity
                                    val i = Intent(this@SignUpActivity, MainActivity::class.java)
                                    i.putExtra((R.string.succesLog.toString()), (R.string.succesLog.toString()))
                                    startActivity(i)
                                    finish()
                                } else {
                                    Snackbar.make(root, R.string.errEmail, Snackbar.LENGTH_LONG).show()

                                }
                            }
                    } else {
                        Snackbar.make(root, R.string.errPasw, Snackbar.LENGTH_LONG).show()
                    }
                } else {
                    Snackbar.make(root, R.string.errEmpty, Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }
}