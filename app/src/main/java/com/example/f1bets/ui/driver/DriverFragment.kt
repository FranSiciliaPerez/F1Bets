package com.example.f1bets.ui.driver

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
    private val driversLiveData: MutableLiveData<List<Driver>> = MutableLiveData()

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

        // Initialize the RecyclerView and adapter
        driverAdapter = DriverAdapter(mutableListOf()) { driverId ->
            // Format el ID driver to ensure that has no bad caracters
            val formattedDriverId = driverId.replace(Regex("[^a-zA-Z0-9]"), "")
            val driver = Driver(formattedDriverId) // Get the driver from the list

            // Call the function editDriver with the selected driver
            editDriver(driver)
        }

        // Initialize RecyclerView
        setupRecyclerView()

        // Observe changes in circuitsLiveData
        driversLiveData.observe(viewLifecycleOwner) { drivers ->
            drivers?.let {
                // Update the adapter with the new data
                driverAdapter.driversList = it as MutableList<Driver>
                driverAdapter.notifyDataSetChanged()
            }
        }
        // Fetch driver data from Firestore
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
                driversLiveData.value = driversList
            }
            .addOnFailureListener { exception ->
            }
    }

    private fun editDriver(driver: Driver) {
        // Make a Bundle to give the ID of the driver to EditDriverFragment
        val bundle = Bundle()
        bundle.putString("driverId", driver.id)
        // Navigate with the Bundle
        findNavController().navigate(R.id.editDriversFragment, bundle)
    }
}