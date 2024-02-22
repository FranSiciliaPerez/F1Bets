package com.example.f1bets.ui.profile

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.f1bets.R
import com.example.f1bets.activities.StartActivity
import com.example.f1bets.databinding.FragmentUserBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class UserFragment : Fragment() {
    private lateinit var binding: FragmentUserBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserBinding.inflate(inflater, container, false)

        auth = FirebaseAuth.getInstance()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Verifica si hay un usuario autenticado
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // Si hay un usuario autenticado accedo a la información
            val userEmail = currentUser.email
            val userName = currentUser.displayName
            val userPhotoUrl = currentUser.photoUrl

            // Información del usuario
            binding.textViewUserEmail.text = userEmail
            binding.textViewUserName.text = userName
            userPhotoUrl?.let { url ->
                Glide.with(requireContext())
                    .load(url)
                    .into(binding.imageViewUser)
            }
        } else {
            binding.textViewUserEmail.text = "Usuario no autenticado"
        }
    }

    override fun onResume() {

        with(binding){
            btnDeleteAccount.setOnClickListener { userDelete() }
            btnLogOut.setOnClickListener { showConfirmationDialog() }
        }
        super.onResume()
    }


    private fun userDelete() {
        val user = Firebase.auth.currentUser

        val dialogBuilder = MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.app_name)
            .setMessage(getString(R.string.txtDeleteAccount))
            .setPositiveButton(getString(R.string.txtContinue)) { dialog, _ ->
                // Eliminar al usuario de Firebase Authentication
                user?.delete()?.addOnCompleteListener { authTask ->
                    if (authTask.isSuccessful) {
                        // Eliminación exitosa del usuario de Authentication
                        // Ahora, eliminar la información del usuario de la base de datos de Firebase
                        user?.uid?.let { deleteUserDataFromDatabase(it) }
                    } else {
                        // Error en la eliminación del usuario de Authentication
                        Log.e(TAG, "Error deleting user from Authentication: ${authTask.exception}")
                        // Mostrar un mensaje de error
                        Snackbar.make(requireView(), R.string.errDeleteAccount, Snackbar.LENGTH_LONG).show()
                    }
                } ?: run {
                    // Si el usuario es nulo
                    Snackbar.make(requireView(), R.string.errEmpty, Snackbar.LENGTH_LONG).show()
                }
                dialog.dismiss()
            }
            .setNegativeButton(getString(R.string.txtCancel)) { dialog, _ ->
                dialog.dismiss()
            }
        dialogBuilder.show()
    }

    private fun deleteUserDataFromDatabase(userId: String) {
        val db = FirebaseFirestore.getInstance()
        val userDocRef = db.collection("user").document(userId)

        userDocRef.delete()
            .addOnSuccessListener {
                // Eliminación exitosa de la información del usuario de la base de datos
                Log.d(TAG, "User data deleted successfully from Firestore")
                // Cerrar sesión y redirigir a la pantalla de inicio
                Firebase.auth.signOut()
                val intent = Intent(requireActivity(), StartActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                requireActivity().finish()
            }
            .addOnFailureListener { exception ->
                // Error en la eliminación de la información del usuario de la base de datos
                Log.e(TAG, "Error deleting user data from Firestore: $exception")
                // Mostrar un mensaje de error
                Snackbar.make(requireView(), R.string.errDeleteAccount, Snackbar.LENGTH_LONG).show()
            }
    }

    private fun showConfirmationDialog() {
        val dialogBuilder = MaterialAlertDialogBuilder(binding.root.context)
            .setTitle("F1Bets")
            .setMessage(getString(R.string.txtLogOut))
            .setPositiveButton(getString(R.string.txtYes)) { dialog, _ ->
                val i = Intent(requireActivity(), StartActivity::class.java)
                startActivity(i)
                auth.signOut()
                requireActivity().finish()
                dialog.dismiss()
            }
            .setNegativeButton(getString(R.string.txtNo)) { dialog, _ ->
                dialog.dismiss()
            }
        dialogBuilder.show()
    }
}