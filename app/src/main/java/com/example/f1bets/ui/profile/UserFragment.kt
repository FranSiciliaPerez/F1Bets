package com.example.f1bets.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.f1bets.R
import com.example.f1bets.StartActivity
import com.example.f1bets.databinding.FragmentUserBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class UserFragment : Fragment() {
    private lateinit var binding: FragmentUserBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserBinding.inflate(inflater, container, false)

        auth = Firebase.auth
        return binding.root
    }

    override fun onResume() {

        with(binding){
            btnDeleteAccount.setOnClickListener { userDelete() }
            btnLogOut.setOnClickListener { showConfirmationDialog() }
        }
        super.onResume()
    }




    private fun userDelete(){
        val user = Firebase.auth.currentUser!!
        if (user != null) {
            user.delete().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val dialogBuilder = MaterialAlertDialogBuilder(binding.root.context)
                        .setTitle(R.string.app_name)
                        .setMessage(getString(R.string.txtDeleteAccount))
                        .setPositiveButton(getString(R.string.txtContinue)) { dialog, _ ->
                            val i = Intent(requireActivity(), StartActivity::class.java)
                            startActivity(i)
                            auth.signOut()
                            requireActivity().finish()
                            dialog.dismiss()
                        }
                        .setNegativeButton(getString(R.string.txtCancel)) { dialog, _ ->
                            dialog.dismiss()
                        }
                    dialogBuilder.show()
                } else {
                    Snackbar.make(binding.root, R.string.errDeleteAccount, Snackbar.LENGTH_LONG).show()
                }
            }
        } else {
        }

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