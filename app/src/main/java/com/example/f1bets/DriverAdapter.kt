package com.example.f1bets

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.f1bets.databinding.DriverItemBinding
import com.example.f1bets.entities.Driver

class DriverAdapter(
    var driversList: MutableList<Driver>,
    private val onDeleteClickListener: (Driver) -> Unit
) : RecyclerView.Adapter<DriverAdapter.DriverViewHolder>(

) {

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
                    onDeleteClickListener.invoke(driver)
                }
            }
        }
    }
}

