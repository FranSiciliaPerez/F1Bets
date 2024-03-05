package com.example.f1bets.fragments.ui.circuit

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.f1bets.R
import com.example.f1bets.databinding.FragmentEditCircuitBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.util.*


class EditCircuitFragment : Fragment() {

    private lateinit var binding: FragmentEditCircuitBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private var selectedImageUri: Uri? = null
    private lateinit var circuitId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditCircuitBinding.inflate(inflater, container, false)
        db = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()
        circuitId = requireArguments().getString("circuitId")!!
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       binding.imgCircuit.setOnClickListener {
            selectImage()
        }
        binding.btnSaveEdit.setOnClickListener {
            // Get the circuit details from the input fields
            val name = binding.txtNameCircuit.text.toString().trim()
            val country = binding.txtCountry.text.toString().trim()
            val laps = binding.txtLaps.text.toString().toLongOrNull() ?: 0
            val length = binding.txtLength.text.toString().toLongOrNull() ?: 0

            // Check if all required fields are filled
            if (name.isNotEmpty() && country.isNotEmpty() && laps > 0 && length > 0) {
                // Check if an image is selected
                selectedImageUri?.let { uri ->
                    // Upload the image and save the circuit with the image URL
                    uploadImageAndSaveCircuit(uri, name, country, laps, length)
                } ?: run {
                    // If no image is selected, save the circuit without an image
                    val newCircuit = mapOf(
                        "nameCircuit" to name,
                        "country" to country,
                        "laps" to laps,
                        "length" to length
                    )
                    saveCircuitToFirestore(newCircuit)
                }
            } else {
                Snackbar.make(binding.root, R.string.errEmpty, Snackbar.LENGTH_LONG).show()
            }
        }

        // Fetch the current circuit details and display them
        fetchCircuitFromFirestore()
    }

    private fun fetchCircuitFromFirestore() {
        db.collection("circuit").document(circuitId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    // Retrieve circuit details from Firestore document
                    val currentCircuit = document.data
                    // Display circuit details in the input fields
                    currentCircuit?.let { circuit ->
                        binding.apply {
                            txtNameCircuit.setText(circuit["nameCircuit"].toString())
                            txtCountry.setText(circuit["country"].toString())
                            txtLaps.setText(circuit["laps"].toString())
                            txtLength.setText(circuit["length"].toString())
                        }
                        // Load the image into the image view using Glide
                        val imageUrl = circuit["picture"].toString()
                        Glide.with(requireContext())
                            .load(imageUrl)
                            .into(binding.imgCircuit)
                    }
                    binding.btnCancel.setOnClickListener {
                        findNavController().navigate(R.id.action_editCircuitFragment_to_nav_Circuits)
                    }
                } else {
                    // Document does not exist
                    Snackbar.make(requireView(), R.string.txterrDocuNotExist, Snackbar.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Snackbar.make(requireView(), "Error fetching circuit: ${exception.message}", Snackbar.LENGTH_SHORT).show()
            }
    }

    private fun uploadImageAndSaveCircuit(imageUri: Uri?, name: String, country: String, laps: Long, length: Long) {
        if (imageUri != null) {
            // Reference to the Firebase Storage location
            val storageRef = storage.reference
            val imagesRef = storageRef.child("f1Bets-images/${UUID.randomUUID()}")

            // Upload the image file to Firebase Storage
            val uploadTask = imagesRef.putFile(imageUri)
            uploadTask.addOnSuccessListener { taskSnapshot ->
                // Retrieve its the image url
                taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->
                    // Create a new circuit with the image URL
                    val newCircuit = mapOf(
                        "nameCircuit" to name,
                        "country" to country,
                        "laps" to laps,
                        "length" to length,
                        "picture" to uri.toString()
                    )
                    // Save the new circuit to Firestore
                    saveCircuitToFirestore(newCircuit)
                }
            }.addOnFailureListener {
                Snackbar.make(binding.root, R.string.errImageUpload, Snackbar.LENGTH_LONG).show()
            }
        } else {
            val newCircuit = mapOf(
                "nameCircuit" to name,
                "country" to country,
                "laps" to laps,
                "length" to length
            )
            saveCircuitToFirestore(newCircuit)
        }
    }

    private fun saveCircuitToFirestore(circuitData: Map<String, Any>) {
        db.collection("circuit")
            .document(circuitId)
            .update(circuitData)
            .addOnSuccessListener {
                findNavController().navigate(R.id.action_editCircuitFragment_to_nav_Circuits)
                Snackbar.make(binding.root, R.string.txtCircuitUpdateSuccess, Snackbar.LENGTH_LONG).show()
            }
            .addOnFailureListener {
                Snackbar.make(binding.root, R.string.txtErrUpdateCircuit, Snackbar.LENGTH_LONG).show()
            }
    }

    // Select an image from the gallery
    private fun selectImage() {
        val options = arrayOf("Choose from Gallery", "Cancel")
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Select Image Source")
            .setItems(options) { dialog, which ->
                when (which) {
                    0 -> dispatchGalleryIntent()
                    1 -> dialog.dismiss()
                }
            }
            .show()
    }

    // Pick an image from the gallery
    private fun dispatchGalleryIntent() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_PICK_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                REQUEST_IMAGE_CAPTURE -> {
                    val imageBitmap = data?.extras?.get("data") as Bitmap
                    selectedImageUri = getImageUri(requireContext(), imageBitmap)
                }
                REQUEST_PICK_IMAGE -> {
                    data?.data?.let { uri ->
                        selectedImageUri = uri
                    }
                }
            }
            // Load the image into the image view using Glide
            selectedImageUri?.let { uri ->
                Glide.with(requireContext())
                    .load(uri)
                    .into(binding.imgCircuit)
            }
        }
    }

    // Get the uri of an image bitmap
    private fun getImageUri(inContext: Context, inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }

    companion object {
        private const val REQUEST_IMAGE_CAPTURE = 0
        private const val REQUEST_PICK_IMAGE = 1
    }
}