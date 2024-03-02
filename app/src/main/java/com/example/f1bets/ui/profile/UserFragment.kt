package com.example.f1bets.ui.profile

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.f1bets.R
import com.example.f1bets.activities.StartActivity
import com.example.f1bets.databinding.FragmentUserBinding
import com.example.f1bets.entities.User
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase

class UserFragment : Fragment() {
    private lateinit var binding: FragmentUserBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserBinding.inflate(inflater, container, false)

        auth = FirebaseAuth.getInstance()
        firestore = Firebase.firestore
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Verify if there is an autenticated user
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // If there is an autenticated user get the Id
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
                        binding.textViewUserEmail.text = user.email
                        binding.textViewUserName.text = user.userName
                        user.picture?.let { pictureUrl ->
                            Glide.with(requireContext())
                                .load(pictureUrl)
                                .into(binding.imageViewUser)
                        }
                    }
                } else {
                    Log.d(TAG, "El documento no existe")
                }
            }.addOnFailureListener { exception ->
                Log.d(TAG, "Error obteniendo el documento:", exception)
            }
        } else {
            binding.textViewUserEmail.text = "Usuario no autenticado"
        }
    }

    override fun onResume() {

        with(binding){
            btnDeleteAccount.setOnClickListener { userDeleteRedirect() }
            btnLogOut.setOnClickListener { showConfirmationDialog() }
        }
        super.onResume()
    }
    private fun userDeleteRedirect() {


        val dialogBuilder = MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.app_name)
            .setMessage(getString(R.string.txtDeleteAccount))
            .setPositiveButton(getString(R.string.txtContinue)) { dialog, _ ->
                findNavController().navigate(R.id.action_nav_User_to_checkLoginFragment)
                dialog.dismiss()
            }
            .setNegativeButton(getString(R.string.txtCancel)) { dialog, _ ->
                dialog.dismiss()
            }
        dialogBuilder.show()
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