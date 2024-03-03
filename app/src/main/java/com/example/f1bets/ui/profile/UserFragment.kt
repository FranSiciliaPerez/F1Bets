package com.example.f1bets.ui.profile

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.f1bets.R
import com.example.f1bets.activities.StartActivity
import com.example.f1bets.databinding.FragmentUserBinding
import com.example.f1bets.entities.User
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
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
        setHasOptionsMenu(true) // to make posible the options menu
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

    // Menu settings
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.user_options_menu, menu)
        true
    }
    // It control the click on the item of the settings menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                findNavController().navigate(R.id.action_nav_User_to_userSettingsFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}