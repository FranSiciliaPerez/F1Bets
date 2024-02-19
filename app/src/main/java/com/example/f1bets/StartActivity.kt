package com.example.f1bets

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.f1bets.databinding.ActivityStartBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class StartActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityStartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()


        with(binding){
            btnLogin.setOnClickListener {
                val email = binding.email.text.toString()
                val password = binding.password.text.toString()

                // Comprobamos si todos los campos tienen información llamando a la clase funciones
                if (Funciones.allFilled(email, password)) {
                    auth
                        .signInWithEmailAndPassword(email, password)
                        .addOnSuccessListener {
                            // If login is successful, redirect to the main activity
                            val i = Intent(this@StartActivity, MainActivity::class.java)
                            i.putExtra((R.string.succesLog.toString()), (R.string.succesLog.toString()))
                            startActivity(i)
                            finish()
                        }
                        .addOnFailureListener{
                            Snackbar.make(root, R.string.errEmail, Snackbar.LENGTH_LONG).show()
                        }
                } else {
                    // Algún campo está vacío: mostramos error
                    Snackbar.make(root, R.string.errEmpty, Snackbar.LENGTH_LONG).show()
                }
            }
            btnSingUp.setOnClickListener {
                val i = Intent(this@StartActivity, SignUpActivity::class.java)
                startActivity(i)
            }

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
    override fun onBackPressed() {
        showExitConfirmationDialog()
        super.onBackPressed()
    }
    private fun showExitConfirmationDialog() {
        val alertDialog = AlertDialog.Builder(this)
            .setTitle(R.string.app_name)
            .setMessage(R.string.txtCloseApp)
            .setPositiveButton(R.string.txtYes) { dialog, _ ->
                dialog.dismiss()
                finishAffinity() // Este método, cierra la actividad actual y todas las actividades asociadas a ella
            }
            .setNegativeButton(R.string.txtNo) { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        alertDialog.show()
    }
}