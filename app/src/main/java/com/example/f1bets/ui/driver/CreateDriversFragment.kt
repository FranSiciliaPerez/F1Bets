package com.example.f1bets.ui.driver

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.f1bets.R
import com.example.f1bets.databinding.FragmentCreateDriversBinding
import com.google.firebase.firestore.FirebaseFirestore


class CreateDriversFragment : Fragment() {

    private lateinit var binding: FragmentCreateDriversBinding
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreateDriversBinding.inflate(inflater, container, false)

        binding.btnCreateDriver.setOnClickListener {
            createDriver()
        }

        return binding.root
    }

    private fun createDriver() {
        val name = binding.txtNameDriver.text.toString().trim()
        val team = binding.txtTeam.text.toString().trim()
        val yearBirth = binding.txtYearBirth.text.toString().toLongOrNull() ?: 0
        //val picture = binding.imgDriver.text.toString().trim()

        // Validate that required fields are not empty
        if (name.isNotEmpty() && team.isNotEmpty() && yearBirth > 0) {
            val newDriver = hashMapOf(
                "name" to name,
                "team" to team,
                "yearBirth" to yearBirth,
                //"picture" to picture
            )

            // Add the new driver to the driver collection
            db.collection("driver")
                .add(newDriver)
                .addOnSuccessListener { documentReference ->
                    // Get the autogenerated id of the driver and add it into the document
                    val newDriverId = documentReference.id
                    // Update the new document of the new driver, with the field of its id
                    db.collection("driver").document(newDriverId)
                        .update("id", newDriverId)
                    Navigation.findNavController(requireView()).navigate(R.id.action_createDriversFragment_to_nav_Drivers)

                }
                .addOnFailureListener {
                }
        } else {
        }
    }
}