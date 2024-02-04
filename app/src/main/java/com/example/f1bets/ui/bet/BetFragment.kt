package com.example.f1bets.ui.bet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.f1bets.databinding.FragmentBetBinding

class BetFragment : Fragment() {
    private lateinit var binding: FragmentBetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBetBinding.inflate(inflater, container, false)
        return binding.root
    }
}