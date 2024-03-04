package com.example.f1bets

import android.content.ContentValues
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.f1bets.databinding.DriverItemBinding
import com.example.f1bets.entities.Driver
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore

class DriverAdapter(
    var driversList: MutableList<Driver>,
    private val onDriverEditClick: (String) -> Unit
) : RecyclerView.Adapter<DriverAdapter.DriverViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DriverViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DriverItemBinding.inflate(inflater, parent, false)
        return DriverViewHolder(binding)
    }

    /**
     * @param holder: DriverViewHolder
     * @param position: Int
     */
    override fun onBindViewHolder(holder: DriverViewHolder, position: Int) {
        holder.bindDriver(driversList[position])
    }
    // Size of the driverList collection
    override fun getItemCount(): Int = driversList.size

    inner class DriverViewHolder(private val binding: DriverItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private fun deleteDialogConfirm(driver: Driver) {
            val dialogBuilder = MaterialAlertDialogBuilder(binding.root.context)
                .setTitle("F1Bets")
                .setMessage(R.string.textConfirDeleteCont)
                .setPositiveButton(R.string.txtYes) { dialog, _ ->
                    deleteDriver(driver)
                    dialog.dismiss()
                }
                .setNegativeButton(R.string.txtNo) { dialog, _ ->
                    dialog.dismiss()
                }
            dialogBuilder.show()
        }

        fun bindDriver(driver: Driver) {
            with(binding) {
                // Load the driver photo using Glide library if exist, otherwise use a default image
                if (!driver.picture.isNullOrEmpty()) {
                    Glide.with(root.context)
                        .load(driver.picture)
                        //.override(600, 400) // Set standard size for the image
                        .into(imgDriver)
                } else {
                    // Set a placeholder image if no picture is available
                    imgDriver.setImageResource(R.drawable.ic_person)
                }

                // Set driver name and team
                nameDriver.text = driver.name
                teamDriver.text = driver.team

                btnDelete.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val driver = driversList[position]
                        deleteDialogConfirm(driver)
                    }
                }
                btnEdit.setOnClickListener {
                    onDriverEditClick(driver.id)
                }
            }
        }
        private fun deleteDriver(driver: Driver) {
            val db = FirebaseFirestore.getInstance()
            val idDriver = driver.id

            // Verify if the driver is assigned into at least one bet
            db.collection("bets")
                .whereEqualTo("idDriver", idDriver)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    if (querySnapshot.isEmpty) {
                        // Driver is not assigned to any bets so it can be deleted
                        val driverDocRef = db.collection("driver").document(idDriver)
                        driverDocRef.delete()
                            .addOnSuccessListener {
                                driversList.remove(driver)
                                notifyDataSetChanged()
                            }
                            .addOnFailureListener { exception ->
                                Log.e(ContentValues.TAG, "Error deleting the driver", exception)
                            }
                    } else {
                        // Show a message that the driver is assigned to a bet and that cannot be deleted
                        Snackbar.make(binding.root, (R.string.txtErrDeleteDriverAssigned), Snackbar.LENGTH_LONG).show()
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e(ContentValues.TAG, "Error checking if the driver is assigned to a bet", exception)
                }
        }
    }
}

