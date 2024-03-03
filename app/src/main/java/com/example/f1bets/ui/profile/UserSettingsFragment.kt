package com.example.f1bets.ui.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.f1bets.R
import com.example.f1bets.activities.StartActivity
import com.example.f1bets.databinding.FragmentUserSettingsBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth

class UserSettingsFragment : Fragment() {

    private lateinit var binding : FragmentUserSettingsBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserSettingsBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        return binding.root

    }
    override fun onResume() {

        with(binding){
            btnDeleteAccount.setOnClickListener { userDeleteRedirect() }
            btnLogOut.setOnClickListener { showConfirmationDialog() }
        }
        super.onResume()
    }
    private fun userDeleteRedirect() {

        val dialogBuilder = MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.app_name)
            .setMessage(getString(R.string.txtDeleteAccount))
            .setPositiveButton(getString(R.string.txtContinue)) { dialog, _ ->
                findNavController().navigate(R.id.action_userSettingsFragment_to_checkLoginFragment)
                dialog.dismiss()
            }
            .setNegativeButton(getString(R.string.txtCancel)) { dialog, _ ->
                dialog.dismiss()
            }
        dialogBuilder.show()
    }

    private fun showConfirmationDialog() {
        val dialogBuilder = MaterialAlertDialogBuilder(binding.root.context)
            .setTitle("F1Bets")
            .setMessage(getString(R.string.txtLogOut))
            .setPositiveButton(getString(R.string.txtYes)) { dialog, _ ->
                val i = Intent(requireActivity(), StartActivity::class.java)
                startActivity(i)
                auth.signOut()
                requireActivity().finish()
                dialog.dismiss()
            }
            .setNegativeButton(getString(R.string.txtNo)) { dialog, _ ->
                dialog.dismiss()
            }
        dialogBuilder.show()
    }
}