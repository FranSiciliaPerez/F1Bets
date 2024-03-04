package com.example.f1bets.ui.circuit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.f1bets.R
import com.example.f1bets.databinding.FragmentEditCircuitBinding
import com.example.f1bets.entities.Circuit
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore


class EditCircuitFragment : Fragment() {

    private lateinit var binding: FragmentEditCircuitBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var circuitId: String
    private lateinit var currentCircuit: Circuit

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditCircuitBinding.inflate(inflater, container, false)
        db = FirebaseFirestore.getInstance()
        circuitId = requireArguments().getString("circuitId")!!
        fetchCircuitFromFirestore()
        return binding.root
    }

    private fun fetchCircuitFromFirestore() {
        db.collection("circuit").document(circuitId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    currentCircuit = document.toObject(Circuit::class.java)!!
                    binding.apply {
                        txtNameCircuit.setText(currentCircuit.nameCircuit)
                        txtCountry.setText(currentCircuit.country)
                        txtLaps.setText(currentCircuit.laps.toString())
                        txtLength.setText(currentCircuit.length.toString())
                    }

                    binding.btnSaveEdit.setOnClickListener {
                        updateCircuit()
                    }
                    binding.btnCancel.setOnClickListener {
                        findNavController().navigate(R.id.action_editCircuitFragment_to_nav_Circuits)
                    }
                } else {
                    // Document does not exist
                    Snackbar.make(requireView(),"Document does not exist",Snackbar.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Snackbar.make(requireView(),"Error fetching circuit: ${exception.message}",Snackbar.LENGTH_SHORT).show()
            }
    }

    private fun updateCircuit() {
        val newName = binding.txtNameCircuit.text.toString().trim()
        val newCountry = binding.txtCountry.text.toString().trim()
        val newLaps = binding.txtLaps.text.toString().toLongOrNull() ?: 0
        val newLength = binding.txtLength.text.toString().toLongOrNull() ?: 0

        Snackbar.make(requireView(),"Updating circuit with name: $newName, country: $newCountry, laps: $newLaps, length: $newLength",Snackbar.LENGTH_SHORT).show()

        if (newName == currentCircuit.nameCircuit &&
            newCountry == currentCircuit.country &&
            newLaps == currentCircuit.laps &&
            newLength == currentCircuit.length
        ) {
            Snackbar.make(requireView(),"No changes made", Snackbar.LENGTH_SHORT).show()
            return
        }

        val updatedCircuitData: Map<String, Any> = hashMapOf(
            "nameCircuit" to newName,
            "country" to newCountry,
            "laps" to newLaps,
            "length" to newLength
        )

        db.collection("circuit")
            .document(circuitId)
            .update(updatedCircuitData)
            .addOnSuccessListener {
                findNavController().navigate(R.id.action_editCircuitFragment_to_nav_Circuits)
                Snackbar.make(requireView(),"Circuit updated successfully", Snackbar.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Snackbar.make(requireView(),"Error updating circuit: ${e.message}", Snackbar.LENGTH_SHORT).show()
            }
    }
}