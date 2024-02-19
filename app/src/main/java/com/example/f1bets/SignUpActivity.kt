package com.example.f1bets

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.example.f1bets.databinding.ActivitySignUpBinding
import com.example.f1bets.entities.User
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.IOException

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()

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

    private fun selectImage() {
        val options = arrayOf("Tomar Foto", "Elegir de la Galería", "Cancelar")
        val builder = AlertDialog.Builder(this)
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
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            selectedImageUri = data.data
            try {
                Glide.with(this)
                    .load(selectedImageUri)
                    .into(binding.imageView)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageUri = data?.extras?.get("data") as Uri
            selectedImageUri = imageUri
            try {
                Glide.with(this)
                    .load(selectedImageUri)
                    .into(binding.imageView)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun uploadImageAndSaveUser(imageUri: Uri, userName: String, email: String) {
        val storageRef = storage.reference
        val imagesRef = storageRef.child("f1Bets-images/${auth.currentUser?.uid}")

        val uploadTask = imagesRef.putFile(imageUri)
        uploadTask.addOnSuccessListener { taskSnapshot ->
            taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->
                val newUser = User(auth.currentUser?.uid ?: "", userName, email, uri.toString())
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
                val i = Intent(this@SignUpActivity, MainActivity::class.java)
                startActivity(i)
                finish()
            }
            .addOnFailureListener {
                Snackbar.make(binding.root, R.string.errSignFirestore, Snackbar.LENGTH_LONG).show()
            }
    }

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
        private const val REQUEST_IMAGE_CAPTURE = 2
    }
}