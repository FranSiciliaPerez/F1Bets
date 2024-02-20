package com.example.f1bets.ui.home

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.example.f1bets.R
import com.example.f1bets.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth

class HomeFragment : Fragment() {
    lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val alertDialog = AlertDialog.Builder(requireContext())
                    .setTitle(R.string.app_name)
                    .setMessage(R.string.txtCloseApp)
                    .setPositiveButton(R.string.txtYes) { dialog, _ ->
                        dialog.dismiss()
                        activity?.finishAffinity() // Este método, cierra la actividad actual y todas las actividades asociadas a ella
                    }
                    .setNegativeButton(R.string.txtNo) { dialog, _ ->
                        dialog.dismiss()
                    }
                    .create()

                alertDialog.show()
            }
        })

        return binding.root
    }
}