package com.example.f1bets.ui.circuit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.f1bets.databinding.FragmentCircuitBinding

class CircuitFragment : Fragment() {
    private lateinit var binding: FragmentCircuitBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCircuitBinding.inflate(inflater, container, false)
        return binding.root
    }
}