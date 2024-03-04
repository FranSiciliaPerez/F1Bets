package com.example.f1bets.fragments.ui.home

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import android.widget.PopupMenu
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.f1bets.R
import com.example.f1bets.activities.StartActivity
import com.example.f1bets.databinding.FragmentHomeBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HomeFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentHomeBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true) // to make posible the user settings menu
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        // Initialize SharedPreferences
        sharedPreferences = requireContext().getSharedPreferences("prefs", Context.MODE_PRIVATE)
        // Verify that the user exist before letting acces to other functions
        userAuthCheck()

        // Handle back button press
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Show confirmation dialog when back button pressed
                val dialogBuilder = MaterialAlertDialogBuilder(binding.root.context)
                    .setTitle(R.string.app_name)
                    .setMessage(R.string.txtCloseApp)
                    .setPositiveButton(R.string.txtYes) { dialog, _ ->
                        dialog.dismiss()
                        requireActivity().finishAffinity() // Method to close the current activity and all the conected around
                    }
                    .setNegativeButton(R.string.txtNo) { dialog, _ ->
                        dialog.dismiss()
                    }
                    .create()

                dialogBuilder.show()
            }
        })

        // With the long click apears the context menu
        binding.root.setOnLongClickListener {
            contextMenu()
            true
        }
        return view
    }

    // Check if user is logged in
    private fun userAuthCheck() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            // if user is not autenticated, redirect to login
            val intent = Intent(requireContext(), StartActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        } else {
            // Check if the user exist in the bbdd in firebase
            val db = FirebaseFirestore.getInstance()
            db.collection("user").document(currentUser.uid)
                .get()
                .addOnSuccessListener { document ->
                    if (!document.exists()) {
                        // If the user document, doesn t exist in Firestore, it will signOut and redirect to the login activity
                        FirebaseAuth.getInstance().signOut()
                        val intent = Intent(requireContext(), StartActivity::class.java)
                        startActivity(intent)
                        requireActivity().finish()
                    } else {
                        // If user is logged in for the first time in this device, show the warning age message
                        val userId = currentUser.uid
                        if (isLoggedIn() && showWarning(userId)) {
                            showMessageDialog()
                        }
                    }
                }
        }
    }
    // Check if user is logged in
    private fun isLoggedIn(): Boolean {
        auth = FirebaseAuth.getInstance()
        return auth.currentUser != null
    }

    private fun showMessageDialog() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (userId != null && showWarning(userId)) {
            // Everytime the user logs into the app the first time in the device
            // it will show the warning message
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(R.string.ageAlert))
                .setMessage(getString(R.string.txtAgeAlert))
                .setPositiveButton(getString(R.string.txtAcceptAgeAlert)) { _, _ ->
                    // Save into SharedPreferences that the user have seen the message and it doesnt have to be shown again
                    val editor = sharedPreferences.edit()
                    editor.putBoolean("showWarning_$userId", false)
                    editor.apply()
                }
                // It logs out the user, and redirect him to the main activity
                .setNegativeButton(getString(R.string.txtDeclineAgeAlert)) { _, _ ->
                    FirebaseAuth.getInstance().signOut()
                    val intent = Intent(requireContext(), StartActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                }
                .setCancelable(false)
                .show()
        }
    }

    private fun showWarning(userId: String): Boolean {
        return sharedPreferences.getBoolean("showWarning_$userId", true)
    }

    // Menu user settings
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.user_options_menu, menu)
        true
    }
    // It control the click on the item of the user settings menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                findNavController().navigate(R.id.action_nav_Home_to_userSettingsFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun contextMenu() {
        val popupMenu = PopupMenu(requireContext(), binding.textView)
        popupMenu.menuInflater.inflate(R.menu.context_menu_home, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.contextMenuInfo -> {
                    findNavController().navigate(R.id.action_nav_Home_to_infoFragment)
                    true
                }
                R.id.contextMenuSettings -> {
                    findNavController().navigate(R.id.action_nav_Home_to_userSettingsFragment)
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }
}
