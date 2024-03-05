package com.example.f1bets.fragments.ui.bet

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.lifecycle.MutableLiveData
import androidx.navigation.Navigation
import com.example.f1bets.R
import com.example.f1bets.functions.Funciones
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore


class CreateBetsFragment : Fragment() {

    private lateinit var drivSpin: Spinner
    private lateinit var circSpin: Spinner
    private lateinit var txtBetMoney: EditText
    private val driversLiveData = MutableLiveData<List<String>>()
    private val circuitsLiveData = MutableLiveData<List<String>>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_create_bets, container, false)

        drivSpin = view.findViewById(R.id.spinnerDriver)
        circSpin = view.findViewById(R.id.spinnerCircuit)
        txtBetMoney = view.findViewById(R.id.txtBetMoney)
        val createBetButton: Button = view.findViewById(R.id.btnAddBet)

        driversLiveData.observe(viewLifecycleOwner) { drivers ->
            val adapter =
                ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, drivers)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            drivSpin.adapter = adapter
        }

        circuitsLiveData.observe(viewLifecycleOwner) { circuits ->
            val adapter =
                ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, circuits)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            circSpin.adapter = adapter
        }

        loadDriversAndCircuits()

        createBetButton.setOnClickListener {
            val selectDriver = drivSpin.selectedItem.toString()
            val selectCircuit = circSpin.selectedItem.toString()
            val betMoney = txtBetMoney.text.toString()

            if (Funciones.allFilled(betMoney) && selectDriver != "Select a driver" && selectCircuit != "Tap to select a circuit") {
                val betMoneyValue = betMoney.toLong()
                createNewBet(selectDriver, selectCircuit, betMoneyValue)
            } else {
            if (selectDriver == "Select a driver" && selectCircuit == "Tap to select a circuit") {
                Snackbar.make(view, "You must select a driver and a circuit", Snackbar.LENGTH_LONG).show()
            } else if (selectDriver == "Select a driver") {
                Snackbar.make(view, "You must select a driver", Snackbar.LENGTH_LONG).show()
            } else if (selectCircuit == "Tap to select a circuit") {
                Snackbar.make(view, "You must select a circuit", Snackbar.LENGTH_LONG).show()
            } else {
                Snackbar.make(view, getString(R.string.txtrrEmptyBetMoney), Snackbar.LENGTH_LONG).show()
            }
        }
        }

        return view
    }

    private fun loadDriversAndCircuits() {
        loadDrivers()
        loadCircuits()
    }

    private fun loadDrivers() {
        val db = FirebaseFirestore.getInstance()
        db.collection("driver")
            .get()
            .addOnSuccessListener { documents ->
                val driverList = documents.map { it.getString("name") ?: "" }
                val titleDriver = listOf("Select a driver") + driverList
                driversLiveData.value = titleDriver
            }
            .addOnFailureListener {
            }
    }

    private fun loadCircuits() {
        val db = FirebaseFirestore.getInstance()
        db.collection("circuit")
            .get()
            .addOnSuccessListener { documents ->
                val circuitList = documents.map { it.getString("nameCircuit") ?: "" }
                val titleCircuit = listOf("Tap to select a circuit") + circuitList
                circuitsLiveData.value = titleCircuit
            }
            .addOnFailureListener {
            }
    }

    private fun createNewBet(driver: String, circuit: String, betMoney: Long) {
        val db = FirebaseFirestore.getInstance()

        // Query to get the driver id with his name
        db.collection("driver")
            .whereEqualTo("name", driver)
            .get()
            .addOnSuccessListener { driverSnapshot ->
                if (!driverSnapshot.isEmpty) {
                    val idDriver = driverSnapshot.documents[0].id

                    // Query to get the circuit id with his name
                    db.collection("circuit")
                        .whereEqualTo("nameCircuit", circuit)
                        .get()
                        .addOnSuccessListener { circuitSnapshot ->
                            if (!circuitSnapshot.isEmpty) {
                                val idCircuit = circuitSnapshot.documents[0].id

                                // Make an ibject with the id of the selected driver, pilot and the money to bet
                                val newBet = hashMapOf(
                                    "idDriver" to idDriver,
                                    "idCircuit" to idCircuit,
                                    "betMoney" to betMoney
                                )

                                // Add the new bet, to the bets collection
                                db.collection("bets")
                                    .add(newBet)
                                    .addOnSuccessListener { documentReference ->
                                        // Get the autogenerated id of the bet and add it into the document
                                        val newBetId = documentReference.id

                                        // Update the new document of the new bet, with the field of its id
                                        db.collection("bets").document(newBetId)
                                            .update("id", newBetId)
                                            Navigation.findNavController(requireView()).navigate(R.id.action_createBetsFragment_to_nav_Bets)
                                    }
                                    .addOnFailureListener {
                                        // Error to add the bet into the collection
                                    }
                            } else {
                                // Circuit not found in the bbdd
                            }
                        }
                        .addOnFailureListener {
                            // Error at the query of the circuit
                        }
                } else {
                    // Driver not found in the bbdd
                }
            }
            .addOnFailureListener {
                // Error at the query of the circuit
            }
    }
}