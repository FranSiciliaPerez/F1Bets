package com.example.f1bets.ui.driver

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.f1bets.DriverAdapter
import com.example.f1bets.R
import com.example.f1bets.databinding.FragmentDriverBinding
import com.example.f1bets.entities.Driver
import com.google.firebase.firestore.FirebaseFirestore

class DriverFragment : Fragment() {
    private lateinit var binding: FragmentDriverBinding
    private lateinit var driverAdapter: DriverAdapter
    private lateinit var driverRecyclerView: RecyclerView
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDriverBinding.inflate(inflater, container, false)
        binding.btnAddDriver.setOnClickListener() {
            Navigation.findNavController(it).navigate(R.id.action_nav_Drivers_to_createDriversFragment)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        driverAdapter = DriverAdapter(mutableListOf()) { _ -> true } // Inicialize the adapter
        setupRecyclerView()
        getDriverData()
    }

    private fun setupRecyclerView() {
        driverRecyclerView = binding.driverRecyclerView
        driverRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = driverAdapter
        }
    }

    private fun getDriverData() {
        db.collection("driver")
            .get()
            .addOnSuccessListener { documents ->
                val driversList = mutableListOf<Driver>()
                for (document in documents) {
                    val driver = document.toObject(Driver::class.java)
                    driversList.add(driver)
                }
                driverAdapter.driversList = driversList
                driverAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
            }
    }
}