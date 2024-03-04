package com.example.f1bets.ui.driver

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import android.Manifest
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.provider.MediaStore
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.example.f1bets.R
import com.example.f1bets.databinding.FragmentCreateDriversBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.*


class CreateDriversFragment : Fragment() {

    private lateinit var binding: FragmentCreateDriversBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private var selectedImageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreateDriversBinding.inflate(inflater, container, false)
        db = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            btnCreateDriver.setOnClickListener {
                val name = txtNameDriver.text.toString().trim()
                val team = txtTeam.text.toString().trim()
                val yearBirth = txtYearBirth.text.toString().toLongOrNull() ?: 0

                if (name.isNotEmpty() && team.isNotEmpty() && yearBirth > 0) {
                    selectedImageUri?.let { uri ->
                        uploadImageAndSaveDriver(uri, name, team, yearBirth)
                    } ?: run {
                        val newDriver = hashMapOf(
                            "name" to name,
                            "team" to team,
                            "yearBirth" to yearBirth
                        )
                        saveDriverToFirestore(newDriver)
                    }
                } else {
                    Snackbar.make(root, R.string.errEmpty, Snackbar.LENGTH_LONG).show()
                }
            }

            imgDriver.setOnClickListener {
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
                // Permisos concedidos, puedes proceder con la lógica de la cámara o la galería
            } else {
                Snackbar.make(binding.root, "", Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun selectImage() {
        val options = arrayOf("Tomar Foto", "Elegir de la Galería", "Cancelar")
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Seleccionar Fuente de Imagen")
        builder.setItems(options) { dialog, which ->
            when (which) {
                0 -> dispatchTakePictureIntent()
                1 -> {
                    val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    startActivityForResult(intent, PICK_IMAGE_REQUEST)
                }
                2 -> dialog.dismiss()
            }
        }
        builder.show()
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
                    .into(binding.imgDriver)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            selectedImageUri = getImageUri(requireContext(), imageBitmap)
            try {
                Glide.with(requireContext())
                    .load(selectedImageUri)
                    .into(binding.imgDriver)
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

    private fun uploadImageAndSaveDriver(imageUri: Uri?, name: String, team: String, yearBirth: Long) {
        if (imageUri != null) {
            val storageRef = storage.reference
            val imagesRef = storageRef.child("f1Bets-images/${UUID.randomUUID()}")

            val uploadTask = imagesRef.putFile(imageUri)
            uploadTask.addOnSuccessListener { taskSnapshot ->
                taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->
                    val newDriver = hashMapOf(
                        "name" to name,
                        "team" to team,
                        "yearBirth" to yearBirth,
                        "picture" to uri.toString() // Guarda la URL de la imagen en Firebase Storage
                    )
                    saveDriverToFirestore(newDriver)

                }
            }.addOnFailureListener {
                Snackbar.make(binding.root, R.string.errImageUpload, Snackbar.LENGTH_LONG).show()
            }
        } else {
            val newDriver = hashMapOf(
                "name" to name,
                "team" to team,
                "yearBirth" to yearBirth
            )
            saveDriverToFirestore(newDriver)
        }
    }

    private fun saveDriverToFirestore(driverData: Map<String, Any>) {
        db.collection("driver")
            .add(driverData)
            .addOnSuccessListener { documentReference ->
                val driverId = documentReference.id
                // Aquí devolvemos el ID del piloto
                updateDriverId(driverId)
            }
            .addOnFailureListener {
                Snackbar.make(binding.root, "", Snackbar.LENGTH_LONG).show()
            }
    }

    private fun updateDriverId(driverId: String) {
        db.collection("driver")
            .document(driverId)
            .update("id", driverId)
            .addOnSuccessListener {
                // El campo id del piloto ha sido actualizado con el ID generado
                Snackbar.make(binding.root, "El campo id del piloto ha sido actualizado con el ID generado", Snackbar.LENGTH_LONG).show()
                Navigation.findNavController(requireView()).navigate(R.id.action_createDriversFragment_to_nav_Drivers)
            }
            .addOnFailureListener {
                Snackbar.make(binding.root, "error_updating_driver_id", Snackbar.LENGTH_LONG).show()
            }
    }

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
        private const val REQUEST_IMAGE_CAPTURE = 2
        private const val REQUEST_PERMISSION_CODE = 100
    }
}