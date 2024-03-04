package com.example.f1bets.fragments.ui.home

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.VideoView
import com.example.f1bets.R
import com.example.f1bets.databinding.FragmentInfoBinding


class InfoFragment : Fragment() {
    private lateinit var binding: FragmentInfoBinding
    private var videoView: VideoView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInfoBinding.inflate(inflater, container, false)
        val view = binding.root

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
        return view
    }

}