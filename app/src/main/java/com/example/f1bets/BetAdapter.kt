package com.example.f1bets

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.f1bets.databinding.BetItemBinding
import com.example.f1bets.entities.Bets
import com.example.f1bets.entities.Circuit
import com.example.f1bets.entities.Driver
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.firestore.FirebaseFirestore

class BetAdapter(
    var betsList: MutableList<Bets>,
    private val onDeleteClickListener: (Bets) -> Unit,
    private val driversMap: Map<String, Driver>,
    private val circuitsMap: Map<String, Circuit>

) : RecyclerView.Adapter<BetAdapter.BetViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BetViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = BetItemBinding.inflate(inflater, parent, false)
        return BetViewHolder(binding)
    }

    /**
     * @param holder: BetViewHolder
     * @param position: Int
     */
    override fun onBindViewHolder(holder: BetViewHolder, position: Int) {
        holder.bindBet(betsList[position])
    }
    // Size of the betList collection
    override fun getItemCount(): Int = betsList.size

    inner class BetViewHolder(private val binding: BetItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private fun deleteDialogConfirm(bet: Bets) {
            val dialogBuilder = MaterialAlertDialogBuilder(binding.root.context)
                .setTitle("F1Bets")
                .setMessage(R.string.textConfirDeleteCont)
                .setPositiveButton(R.string.txtYes) { dialog, _ ->
                    deleteBet(bet)
                    dialog.dismiss()
                }
                .setNegativeButton(R.string.txtNo) { dialog, _ ->
                    dialog.dismiss()
                }
            dialogBuilder.show()
        }
        private fun deleteBet(bet: Bets) {
            val db = FirebaseFirestore.getInstance()
            db.collection("bets").document(bet.id.toString()).delete()
                .addOnSuccessListener {
                    betsList.remove(bet)
                    notifyDataSetChanged()
                }
                .addOnFailureListener { exception ->
                    //R.string.txtNo
                }
        }

        fun bindBet(bet: Bets) {
            with(binding) {
                val driver = driversMap[bet.idDriver]
                val circuit = circuitsMap[bet.idCircuit]

                // Set the names of the driver and the circuit and the money bet
                if (driver != null) {
                    nameDriverBet.text = driver.name
                }
                if (circuit != null) {
                    nameCircuitBet.text = circuit.nameCircuit
                }
                moneyBet.text = bet.betMoney.toString()

                // Handle delete click on bet item
                btnDelete.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val bet = betsList[position]
                        deleteDialogConfirm(bet)
                    }
                }
            }
        }
    }
}