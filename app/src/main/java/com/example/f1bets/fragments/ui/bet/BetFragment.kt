package com.example.f1bets.fragments.ui.bet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.f1bets.adapters.BetAdapter
import com.example.f1bets.R
import com.example.f1bets.databinding.FragmentBetBinding
import com.example.f1bets.entities.Bets
import com.example.f1bets.entities.Circuit
import com.example.f1bets.entities.Driver
import com.google.firebase.firestore.FirebaseFirestore

class BetFragment : Fragment() {
    private lateinit var binding: FragmentBetBinding
    private lateinit var betAdapter: BetAdapter
    private lateinit var betRecyclerView: RecyclerView
    private val db = FirebaseFirestore.getInstance()
    private val betsLiveData: MutableLiveData<List<Bets>> = MutableLiveData()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBetBinding.inflate(inflater, container, false)

        binding.btnAddBet.setOnClickListener() {
            Navigation.findNavController(it).navigate(R.id.action_nav_Bets_to_createBetsFragment)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        betsLiveData.observe(viewLifecycleOwner) { bets ->
            bets?.let {
                val driversMap = mutableMapOf<String, Driver>()
                val circuitsMap = mutableMapOf<String, Circuit>()

                getDriversData { drivers ->
                    driversMap.putAll(drivers)
                    getCircuitsData { circuits ->
                        circuitsMap.putAll(circuits)
                        betAdapter = BetAdapter(
                            it as MutableList<Bets>,
                            driversMap,
                            circuitsMap
                        )
                        setupRecyclerView()
                    }
                }
            }
        }

        getBetData()
    }

    private fun setupRecyclerView() {
        betRecyclerView = binding.betRecyclerView
        betRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = betAdapter
        }
    }

    private fun getBetData() {
        db.collection("bets")
            .get()
            .addOnSuccessListener { documents ->
                val betsList = mutableListOf<Bets>()
                for (document in documents) {
                    val bet = document.toObject(Bets::class.java)
                    betsList.add(bet)
                }
                betsLiveData.value = betsList
            }
            .addOnFailureListener { exception ->
            }
    }

    private fun getDriversData(callback: (Map<String, Driver>) -> Unit) {
        db.collection("driver")
            .get()
            .addOnSuccessListener { documents ->
                val driverMap = mutableMapOf<String, Driver>()
                for (document in documents) {
                    val driver = document.toObject(Driver::class.java)
                    driverMap[document.id] = driver
                }
                callback(driverMap)
            }
            .addOnFailureListener { exception ->
            }
    }

    private fun getCircuitsData(callback: (Map<String, Circuit>) -> Unit) {
        db.collection("circuit")
            .get()
            .addOnSuccessListener { documents ->
                val circuitMap = mutableMapOf<String, Circuit>()
                for (document in documents) {
                    val circuit = document.toObject(Circuit::class.java)
                    circuitMap[document.id] = circuit
                }
                callback(circuitMap)
            }
            .addOnFailureListener { exception ->
            }
    }
}