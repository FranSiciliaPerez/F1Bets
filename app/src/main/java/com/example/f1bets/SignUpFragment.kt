package com.example.f1bets

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.f1bets.activities.MainActivity
import com.example.f1bets.databinding.FragmentSignUpBinding
import com.example.f1bets.entities.User
import com.example.f1bets.functions.Funciones
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.io.IOException

class SignUpFragment : Fragment() {
    private lateinit var binding: FragmentSignUpBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private var selectedImageUri: Uri? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            btnSignUp.setOnClickListener {
                val user = textUser.text.toString()
                val email = textEmail.text.toString()
                val password = txtPassword2.text.toString()

                if (Funciones.allFilled(user, email, password)) {
                    if (checkBox.isChecked) {
                        auth.createUserWithEmailAndPassword(email, password)
                            .addOnSuccessListener { authResult ->
                                selectedImageUri?.let { uri ->
                                    uploadImageAndSaveUser(uri, user, email)
                                } ?: run {
                                    val newUser =
                                        User(authResult.user?.uid ?: "", user, email, "")
                                    saveUserToFirestore(newUser)
                                }
                            }
                            .addOnFailureListener {
                                Snackbar.make(root, R.string.errEmail, Snackbar.LENGTH_LONG).show()
                            }
                    } else {
                        Snackbar.make(root, R.string.msgCheckError, Snackbar.LENGTH_LONG).show()
                    }
                } else {
                    Snackbar.make(root, R.string.errEmpty, Snackbar.LENGTH_LONG).show()
                }
            }

            imageView.setOnClickListener {
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
                Snackbar.make(binding.root, "Los permisos no fueron concedidos", Snackbar.LENGTH_LONG).show()
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
                    .into(binding.imageView)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            selectedImageUri = getImageUri(requireContext(), imageBitmap)
            try {
                Glide.with(requireContext())
                    .load(selectedImageUri)
                    .into(binding.imageView)
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

    private fun uploadImageAndSaveUser(imageUri: Uri, userName: String, email: String) {
        val storageRef = storage.reference
        val imagesRef = storageRef.child("f1Bets-images/${auth.currentUser?.uid}")

        val uploadTask = imagesRef.putFile(imageUri)
        uploadTask.addOnSuccessListener { taskSnapshot ->
            taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->
                val newUser = User(auth.currentUser?.uid ?: "", userName, email, "", uri.toString())
                saveUserToFirestore(newUser)
            }
        }.addOnFailureListener {
            Snackbar.make(binding.root, R.string.errImageUpload, Snackbar.LENGTH_LONG).show()
        }
    }

    private fun saveUserToFirestore(user: User) {
        db.collection("user")
            .document(user.uid)
            .set(user)
            .addOnSuccessListener {
                Snackbar.make(binding.root, R.string.succesLog, Snackbar.LENGTH_LONG).show()
                val i = Intent(requireContext(), MainActivity::class.java)
                startActivity(i)
                requireActivity().finish()
            }
            .addOnFailureListener {
                Snackbar.make(binding.root, R.string.errSignFirestore, Snackbar.LENGTH_LONG).show()
            }
    }

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
        private const val REQUEST_IMAGE_CAPTURE = 2
        private const val REQUEST_PERMISSION_CODE = 100
    }
}