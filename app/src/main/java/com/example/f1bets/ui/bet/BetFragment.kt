package com.example.f1bets.ui.bet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.f1bets.R
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

        binding.btnAddContact.setOnClickListener() {
            Navigation.findNavController(it).navigate(R.id.action_nav_Bets_to_createBetsFragment)
        }
        return binding.root
    }
}