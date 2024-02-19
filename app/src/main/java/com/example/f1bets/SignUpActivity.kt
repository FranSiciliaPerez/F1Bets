package com.example.f1bets

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.f1bets.databinding.ActivitySignUpBinding
import com.example.f1bets.entities.User
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        with(binding) {
            btnSignUp.setOnClickListener {
                val user = textUser.text.toString()
                val email = textEmail.text.toString()
                val password = txtPassword2.text.toString()

                if (Funciones.allFilled(user, email, password)) {
                    if (checkBox.isChecked) {
                        auth.createUserWithEmailAndPassword(email, password)
                            .addOnSuccessListener { authResult ->
                                // Si el registro es exitoso, guarda información adicional en Firestore
                                val newUser =
                                    User(authResult.user?.uid ?: "", user, email, "")
                                saveAdditionalUserData(newUser)
                            }
                            .addOnFailureListener {
                                Snackbar.make(root, R.string.errEmail, Snackbar.LENGTH_LONG).show()
                            }
                    } else {
                        Snackbar.make(root, R.string.msgCheckError, Snackbar.LENGTH_LONG).show()
                    }
                } else {
                    Snackbar.make(root, R.string.errEmpty, Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun saveAdditionalUserData(user: User) {
        db.collection("user")
            .document(user.uid)
            .set(user)
            .addOnSuccessListener {
                Snackbar.make(binding.root, R.string.succesLog, Snackbar.LENGTH_LONG).show()
                val i = Intent(this@SignUpActivity, MainActivity::class.java)
                startActivity(i)
                finish()
            }
            .addOnFailureListener {
                Snackbar.make(binding.root, R.string.errSignFirestore, Snackbar.LENGTH_LONG).show()
            }
    }
}