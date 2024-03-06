package com.example.f1bets.fragments.ui.circuit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.f1bets.adapters.CircuitAdapter
import com.example.f1bets.R
import com.example.f1bets.databinding.FragmentCircuitBinding
import com.example.f1bets.entities.Circuit
import com.google.firebase.firestore.FirebaseFirestore

class CircuitFragment : Fragment() {
    private lateinit var binding: FragmentCircuitBinding
    private lateinit var circuitAdapter: CircuitAdapter
    private lateinit var circuitRecyclerView: RecyclerView
    private val db = FirebaseFirestore.getInstance()
    private val circuitsLiveData: MutableLiveData<List<Circuit>> = MutableLiveData()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCircuitBinding.inflate(inflater, container, false)

        binding.btnAddCircuit.setOnClickListener() {
            Navigation.findNavController(it).navigate(R.id.action_nav_Circuits_to_createCircuitsFragment)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the RecyclerView and adapter
        circuitAdapter = CircuitAdapter(mutableListOf()) { circuitId ->
            // Format el ID circuit to ensure that has no bad caracters
            val formattedCircuitId = circuitId.replace(Regex("[^a-zA-Z0-9]"), "")
            val circuit = Circuit(formattedCircuitId) // Obtain the circuit from the list

            // Call the function editCircuit with the selected circuit
            editCircuit(circuit)
        }

        // Initialize RecyclerView
        setupRecyclerView()

        // Observe changes in circuitsLiveData
        circuitsLiveData.observe(viewLifecycleOwner) { circuits ->
            circuits?.let {
                // Update the adapter with the new data
                circuitAdapter.circuitsList = it as MutableList<Circuit>
                circuitAdapter.notifyDataSetChanged()
            }
        }
        // Fetch circuit data from Firestore
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
                circuitsLiveData.value = circuitsList
            }
            .addOnFailureListener { exception ->
                // Handle failure
            }
    }

    private fun editCircuit(circuit: Circuit) {
        // Make a Bundle to give the ID of the circuit to EditCircuitFragment
        val bundle = Bundle()
        bundle.putString("circuitId", circuit.id)
        // Navigate with the Bundle
        findNavController().navigate(R.id.editCircuitFragment, bundle)
    }
}