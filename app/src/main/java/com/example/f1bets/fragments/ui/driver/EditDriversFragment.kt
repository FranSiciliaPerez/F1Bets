package com.example.f1bets.fragments.ui.driver

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.f1bets.R
import com.example.f1bets.databinding.FragmentEditDriversBinding
import com.example.f1bets.entities.Driver
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore


class EditDriversFragment : Fragment() {

    private lateinit var binding: FragmentEditDriversBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var driverId: String
    private lateinit var currentDriver: Driver

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditDriversBinding.inflate(inflater, container, false)
        db = FirebaseFirestore.getInstance()
        driverId = requireArguments().getString("driverId")!!
        fetchDriverFromFirestore()
        return binding.root
    }

    private fun fetchDriverFromFirestore() {
        db.collection("driver").document(driverId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    currentDriver = document.toObject(Driver::class.java)!!
                    binding.apply {
                        txtNameDriver.setText(currentDriver.name)
                        txtTeam.setText(currentDriver.team)
                        txtYearBirth.setText(currentDriver.yearBirth.toString())
                        // Aquí configura la visualización de la imagen si fuera necesario
                    }

                    binding.btnSaveEdit.setOnClickListener {
                        updateDriver()
                    }
                    binding.btnCancel.setOnClickListener {
                        findNavController().navigate(R.id.action_editDriversFragment_to_nav_Drivers)
                    }
                } else {
                    // Document does not exist
                    Snackbar.make(requireView(),"Document does not exist",Snackbar.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Snackbar.make(requireView(),"Error fetching driver: ${exception.message}",Snackbar.LENGTH_SHORT).show()
            }
    }

    private fun updateDriver() {
        val newName = binding.txtNameDriver.text.toString().trim()
        val newTeam = binding.txtTeam.text.toString().trim()
        val newYearBirth = binding.txtYearBirth.text.toString().toLongOrNull() ?: 0

        Snackbar.make(requireView(),"Updating driver with name: $newName, team: $newTeam, year of birth: $newYearBirth",Snackbar.LENGTH_SHORT).show()

        if (newName == currentDriver.name &&
            newTeam == currentDriver.team &&
            newYearBirth == currentDriver.yearBirth
        ) {
            Snackbar.make(requireView(),"No changes made", Snackbar.LENGTH_SHORT).show()
            return
        }

        val updatedDriverData: Map<String, Any> = hashMapOf(
            "name" to newName,
            "team" to newTeam,
            "yearBirth" to newYearBirth
            // Si necesitas actualizar la imagen, también puedes incluirlo aquí
        )

        db.collection("driver")
            .document(driverId)
            .update(updatedDriverData)
            .addOnSuccessListener {
                findNavController().navigate(R.id.action_editDriversFragment_to_nav_Drivers)
                Snackbar.make(requireView(),"Driver updated successfully", Snackbar.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Snackbar.make(requireView(),"Error updating driver: ${e.message}", Snackbar.LENGTH_SHORT).show()
            }
    }
}