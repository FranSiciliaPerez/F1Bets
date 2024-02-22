package com.example.f1bets.ui.home

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.VideoView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.example.f1bets.R
import com.example.f1bets.databinding.FragmentHomeBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth

class HomeFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentHomeBinding
    private var videoView: VideoView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
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
        })


        // Initialize the VideoView after the inflate of the dessign
        videoView = view.findViewById(R.id.videoView)

        // Media controler to play, pause, go fast forguard etc..
        val mediaController = MediaController(requireContext())
        mediaController.setAnchorView(videoView)

        // Get the path of the video uri from the resources
        val videoUri = Uri.parse("android.resource://" + requireActivity().packageName + "/" + R.raw.betf1)

        videoView?.apply {
            setMediaController(mediaController)
            // Config the path to play it at the VideoView
            setVideoURI(videoUri)
            // Initialize the video
            start()
        }

        return view
    }
}
