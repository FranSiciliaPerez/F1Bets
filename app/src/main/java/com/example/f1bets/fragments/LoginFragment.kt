package com.example.f1bets.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.f1bets.R
import com.example.f1bets.activities.MainActivity
import com.example.f1bets.databinding.FragmentLoginBinding
import com.example.f1bets.functions.Funciones
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)

        auth = FirebaseAuth.getInstance()

        with(binding) {
            btnLogin.setOnClickListener {
                val email = binding.email.text.toString()
                val password = binding.password.text.toString()

                // Check if all the fields have information calling the function class
                if (Funciones.allFilled(email, password)) {
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnSuccessListener {
                            // If login is successful, redirect main activity
                            val i = Intent(requireContext(), MainActivity::class.java)
                            i.putExtra(getString(R.string.succesLog), getString(R.string.succesLog))
                            startActivity(i)
                            requireActivity().finish()
                        }
                        .addOnFailureListener {
                            // If email or password are incorrect it will show a message
                            Snackbar.make(root, R.string.errPaswAndEmail, Snackbar.LENGTH_LONG).show()
                        }
                } else {
                    // If some field is empty show an error
                    Snackbar.make(root, R.string.errEmpty, Snackbar.LENGTH_LONG).show()
                }
            }
            btnSingUp.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            showExitConfirmationDialog()
        }
    }

    private fun showExitConfirmationDialog() {
        val dialogBuilder = MaterialAlertDialogBuilder(binding.root.context)
            .setTitle(R.string.app_name)
            .setMessage(R.string.txtCloseApp)
            .setPositiveButton(R.string.txtYes) { dialog, _ ->
                dialog.dismiss()
                requireActivity().finishAffinity() // This method close the actual activity and all asociated to it
            }
            .setNegativeButton(R.string.txtNo) { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        dialogBuilder.show()
    }
}
