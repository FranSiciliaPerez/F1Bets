package com.example.f1bets

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.f1bets.databinding.CircuitItemBinding
import com.example.f1bets.entities.Circuit
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.firestore.FirebaseFirestore

class CircuitAdapter(
    var circuitsList: MutableList<Circuit>,
    private val onCircuitShortClick: (Circuit) -> Boolean

) : RecyclerView.Adapter<CircuitAdapter.CircuitViewHolder>()
{
    private fun deleteCircuit(circuit: Circuit) {
        val db = FirebaseFirestore.getInstance()
        db.collection("circuit").document(circuit.id.toString()).delete()
            .addOnSuccessListener {
                circuitsList.remove(circuit)
                notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                //R.string.txtNo
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CircuitViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CircuitItemBinding.inflate(inflater, parent, false)
        return CircuitViewHolder(binding)
    }

    /**
     * @param holder: CircuitViewHolder
     * @param position: Int
     */
    override fun onBindViewHolder(holder: CircuitViewHolder, position: Int) {
        holder.bindCircuit(circuitsList[position])
    }

    override fun getItemCount(): Int = circuitsList.size

    inner class CircuitViewHolder(private val binding: CircuitItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private fun deleteDialogConfirm(circuit: Circuit) {
            val dialogBuilder = MaterialAlertDialogBuilder(binding.root.context)
                .setTitle("F1Bets")
                .setMessage(R.string.textConfirDeleteCont)
                .setPositiveButton(R.string.txtYes) { dialog, _ ->
                    deleteCircuit(circuit)
                    dialog.dismiss()
                }
                .setNegativeButton(R.string.txtNo) { dialog, _ ->
                    dialog.dismiss()
                }
            dialogBuilder.show()
        }

        fun bindCircuit(circuit: Circuit) {
            with(binding) {
                // Load circuit's photo using Glide library if exist
                if (!circuit.picture.isNullOrEmpty()) {
                    Glide.with(root.context).load(circuit.picture).into(imgCircuit)
                } else {
                    // Set a placeholder image if no picture is available
                    imgCircuit.setImageResource(R.drawable.ic_person)
                }

                // Set circuit name, country, laps
                nameCircuit.text = circuit.nameCircuit
                circuitCountry.text = circuit.country
                circuitLaps.text = circuit.laps.toString()

                // Handle click on circuit item
                btnDelete.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val circuit = circuitsList[position]
                        deleteDialogConfirm(circuit)
                    }
                }
            }
        }
    }
}