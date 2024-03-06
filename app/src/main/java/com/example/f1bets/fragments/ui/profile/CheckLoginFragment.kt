package com.example.f1bets.fragments.ui.profile

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.f1bets.R
import com.example.f1bets.activities.StartActivity
import com.example.f1bets.databinding.FragmentCheckLoginBinding
import com.example.f1bets.entities.User
import com.example.f1bets.functions.Funciones
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase


class CheckLoginFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentCheckLoginBinding
    private lateinit var firestore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCheckLoginBinding.inflate(inflater, container, false)

        auth = FirebaseAuth.getInstance()
        firestore = Firebase.firestore

        with(binding) {
            btnDeleteAccount.setOnClickListener {
                val email = binding.email.text.toString()
                val password = binding.password.text.toString()

                // Check if all the fields have information calling the function class
                if (Funciones.allFilled(email, password)) {
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnSuccessListener {
                            // If login is successful, delete user
                            userDelete()
                            Snackbar.make(root, R.string.succesDeleteAccount, Snackbar.LENGTH_LONG).show()
                        }
                        .addOnFailureListener {
                            // If email or password are incorrect it will show a message
                            Snackbar.make(root, R.string.errPaswAndEmail, Snackbar.LENGTH_LONG).show()
                        }
                } else {
                    // If some field is empty show error
                    Snackbar.make(root, R.string.errEmpty, Snackbar.LENGTH_LONG).show()
                }
            }
            btnCancel.setOnClickListener { findNavController().navigate(R.id.action_checkLoginFragment_to_nav_Home) }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val currentUser = auth.currentUser
        if (currentUser != null) {
        val userId = currentUser.uid
        // Get the document reference of the current user
        val userDocRef = firestore.collection("user").document(userId)

        // Query the information of the current user
        userDocRef.get().addOnSuccessListener { document ->
            if (document != null && document.exists()) {
                // If the document exist, get the user data
                val user = document.toObject<User>()
                // Show the user data in the user interface
                user?.let { user ->
                    binding.email.setText(user.email)
                }
            } else {
                Log.d(ContentValues.TAG, "The document doesnt exist")
            }
        }
        } else {

        }
    }

    private fun userDelete() {
        val user = Firebase.auth.currentUser

        user?.delete()?.addOnCompleteListener { authTask ->
            if (authTask.isSuccessful) {
                // Successful deletion of the user from Authentication
                // Now, delete the user's information from Firebase database
                user?.uid?.let { deleteUserDataFromDatabase(it) }
            } else {
                // Error in deleting the user from Authentication
                Log.e(ContentValues.TAG, "Error deleting user from Authentication: ${authTask.exception}")
                // Show an error message
                Snackbar.make(requireView(), R.string.errDeleteAccount, Snackbar.LENGTH_LONG).show()
            }
        } ?: run {
            // If the user is null
            Snackbar.make(requireView(), R.string.errEmpty, Snackbar.LENGTH_LONG).show()
        }

    }

    private fun deleteUserDataFromDatabase(userId: String) {
        val db = FirebaseFirestore.getInstance()
        val userDocRef = db.collection("user").document(userId)

        userDocRef.delete()
            .addOnSuccessListener {// Delete the user from Firebase Authentication
                // Successful deletion of the user's information from the database
                Log.d(ContentValues.TAG, "User data deleted successfully from Firestore")
                // Sign out and redirect to the start screen
                Firebase.auth.signOut()
                val intent = Intent(requireActivity(), StartActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                requireActivity().finish()
            }
            .addOnFailureListener { exception ->
                // Error in deleting the user's information from the database
                Log.e(ContentValues.TAG, "Error deleting user data from Firestore: $exception")
                // Mostrar un mensaje de error
                Snackbar.make(requireView(), R.string.errDeleteAccount, Snackbar.LENGTH_LONG).show()
            }
    }
}