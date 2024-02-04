package com.example.f1bets.ui.driver

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.f1bets.databinding.FragmentDriverBinding

class DriverFragment : Fragment() {
    private lateinit var binding: FragmentDriverBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDriverBinding.inflate(inflater, container, false)
        return binding.root
    }
}