package com.example.f1bets.fragments.ui.circuit

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.example.f1bets.R
import com.example.f1bets.databinding.FragmentCreateCircuitsBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.*


class CreateCircuitsFragment : Fragment() {

    private lateinit var binding: FragmentCreateCircuitsBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private var selectedImageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreateCircuitsBinding.inflate(inflater, container, false)
        db = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            btnAddCircuit.setOnClickListener {
                val name = txtNameCircuit.text.toString().trim()
                val country = txtCountry.text.toString().trim()
                val laps = txtLaps.text.toString().toLongOrNull() ?: 0
                val length = txtLegth.text.toString().toLongOrNull() ?: 0

                if (name.isNotEmpty() && country.isNotEmpty() && laps > 0 && length > 0) {
                    selectedImageUri?.let { uri ->
                        uploadImageAndSaveCircuit(uri, name, country, laps, length)
                    } ?: run {
                        val newCircuit = hashMapOf(
                            "nameCircuit" to name,
                            "country" to country,
                            "laps" to laps,
                            "length" to length,
                        )
                        saveCircuitToFirestore(newCircuit)
                    }
                } else {
                    Snackbar.make(root, R.string.errEmpty, Snackbar.LENGTH_LONG).show()
                }
            }

            imgCircuit.setOnClickListener {
                selectImage()
            }
        }
    }

    private fun requestPermissions() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                REQUEST_PERMISSION_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                selectImage()
            } else {
                Snackbar.make(binding.root, "", Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun selectImage() {
        val options = arrayOf("Tomar Foto", "Elegir de la Galería", "Cancelar")
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Seleccionar Fuente de Imagen")
            .setItems(options) { dialog, which ->
                when (which) {
                    0 -> dispatchTakePictureIntent()
                    1 -> {
                        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                        startActivityForResult(intent, PICK_IMAGE_REQUEST)
                    }
                    2 -> dialog.dismiss()
                }
            }
            .show()
    }

    private fun dispatchTakePictureIntent() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } else {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), REQUEST_PERMISSION_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            selectedImageUri = data.data
            try {
                Glide.with(requireContext())
                    .load(selectedImageUri)
                    .into(binding.imgCircuit)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            selectedImageUri = getImageUri(requireContext(), imageBitmap)
            try {
                Glide.with(requireContext())
                    .load(selectedImageUri)
                    .into(binding.imgCircuit)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun getImageUri(inContext: Context, inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }

    private fun uploadImageAndSaveCircuit(imageUri: Uri?, name: String, country: String, laps: Long, length: Long) {
        if (imageUri != null) {
            val storageRef = storage.reference
            val imagesRef = storageRef.child("f1Bets-images/${UUID.randomUUID()}")

            val uploadTask = imagesRef.putFile(imageUri)
            uploadTask.addOnSuccessListener { taskSnapshot ->
                taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->
                    val newCircuit = hashMapOf(
                        "nameCircuit" to name,
                        "country" to country,
                        "laps" to laps,
                        "length" to length,
                        "picture" to uri.toString() // Save the URL of the img in Firebase Storage
                    )
                    saveCircuitToFirestore(newCircuit)

                }
            }.addOnFailureListener {
                Snackbar.make(binding.root, R.string.errImageUpload, Snackbar.LENGTH_LONG).show()
            }
        } else {
            val newCircuit = hashMapOf(
                "nameCircuit" to name,
                "country" to country,
                "laps" to laps,
                "length" to length
            )
            saveCircuitToFirestore(newCircuit)
        }
    }
    private fun updateCircuitId(circuitId: String) {
        db.collection("circuit")
            .document(circuitId)
            .update("id", circuitId)
            .addOnSuccessListener {
                // The id driver field has been updated with the generated id
                Snackbar.make(binding.root, "Cicuit succsesfully created", Snackbar.LENGTH_LONG).show()
                Navigation.findNavController(requireView()).navigate(R.id.action_createCircuitsFragment_to_nav_Circuits)
            }
            .addOnFailureListener {
                Snackbar.make(binding.root, "error_updating_circuit_id", Snackbar.LENGTH_LONG).show()
            }
    }
    private fun saveCircuitToFirestore(circuitData: Map<String, Any>) {
        db.collection("circuit")
            .add(circuitData)
            .addOnSuccessListener { documentReference ->
                val circuitId = documentReference.id
                // Give back the ID of the circuit
                updateCircuitId(circuitId)
            }
            .addOnFailureListener {
                Snackbar.make(binding.root, "", Snackbar.LENGTH_LONG).show()
            }
    }

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
        private const val REQUEST_IMAGE_CAPTURE = 2
        private const val REQUEST_PERMISSION_CODE = 100
    }
}
