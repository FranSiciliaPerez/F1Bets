package com.example.f1bets.fragments.ui.home

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.MediaController
import android.widget.PopupMenu
import android.widget.VideoView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.f1bets.R
import com.example.f1bets.activities.StartActivity
import com.example.f1bets.databinding.FragmentHomeBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth

class HomeFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentHomeBinding
    private var videoView: VideoView? = null
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

        // Show warning message if user logged in for the first time
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null && isLoggedIn() && showWarning(userId)) {
            showMessageDialog()
        }

        // Handle back button press
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Show confirmation dialog when back button pressed
                val dialogBuilder = MaterialAlertDialogBuilder(binding.root.context)
                    .setTitle(R.string.app_name)
                    .setMessage(R.string.txtCloseApp)
                    .setPositiveButton(R.string.txtYes) { dialog, _ ->
                        dialog.dismiss()
                        requireActivity().finishAffinity() // This method close the actual activity and all associated to it
                    }
                    .setNegativeButton(R.string.txtNo) { dialog, _ ->
                        dialog.dismiss()
                    }
                    .create()

                dialogBuilder.show()
            }
        })

        // // Initialize the VideoView after the inflate of the dessign
        videoView = view.findViewById(R.id.videoView)

        // Initialize mediaController to play, pause, go fast forguard etc..
        val mediaController = MediaController(requireContext())
        mediaController.setAnchorView(videoView)

        // Set video uri  from the resources
        val videoUri = Uri.parse("android.resource://" + requireActivity().packageName + "/" + R.raw.betf1)
        videoView?.apply {
            setMediaController(mediaController)
            // Config the path to play it at the VideoView
            setVideoURI(videoUri)
        }
        // Set the long click in the screen
        binding.root.setOnLongClickListener{
            contextMenu()
            true
        }
        return view
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
                // It logs out the user, and redirect him to the user_options_menu activity
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
        val navController = Navigation.findNavController(requireView())
        val popupMenu = PopupMenu(requireContext(), binding.textView)
        popupMenu.menuInflater.inflate(R.menu.context_menu_home, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.contextMenuDriver -> {
                    navController.navigate(R.id.nav_Drivers)
                    true
                }
                R.id.contextMenuCircuit -> {
                    navController.navigate(R.id.createBetsFragment)
                    true
                }
                R.id.contextMenuBets -> {
                    navController.navigate(R.id.nav_Bets)
                    true
                }
                R.id.contextMenuUser -> {
                    navController.navigate(R.id.nav_User)
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }
}
