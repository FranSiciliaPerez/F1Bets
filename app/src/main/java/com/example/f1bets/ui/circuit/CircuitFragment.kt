package com.example.f1bets.ui.circuit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.f1bets.CircuitAdapter
import com.example.f1bets.databinding.FragmentCircuitBinding
import com.example.f1bets.entities.Circuit
import com.google.firebase.firestore.FirebaseFirestore

class CircuitFragment : Fragment() {
    private lateinit var binding: FragmentCircuitBinding
    private lateinit var circuitAdapter: CircuitAdapter
    private lateinit var circuitRecyclerView: RecyclerView
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCircuitBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        circuitAdapter = CircuitAdapter(mutableListOf()) { _ -> true } // Inicialize the adapter
        setupRecyclerView()
        getCircuitData()
    }

    private fun setupRecyclerView() {
        circuitRecyclerView = binding.circuitRecyclerView
        circuitRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = circuitAdapter
        }
    }

    private fun getCircuitData() {
        db.collection("circuit")
            .get()
            .addOnSuccessListener { documents ->
                val circuitsList = mutableListOf<Circuit>()
                for (document in documents) {
                    val circuit = document.toObject(Circuit::class.java)
                    circuitsList.add(circuit)
                }
                circuitAdapter.circuitsList = circuitsList
                circuitAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
            }
    }
}