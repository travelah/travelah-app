package com.travelah.travelahapp.view.editprofile

import android.content.Intent
import android.graphics.BitmapFactory
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.travelah.travelahapp.databinding.ActivityEditProfileBinding
import com.travelah.travelahapp.utils.createCustomTempFile
import com.travelah.travelahapp.utils.reduceFileImage
import com.travelah.travelahapp.utils.rotateFile
import com.travelah.travelahapp.utils.uriToFile
import com.travelah.travelahapp.view.ViewModelFactory
import com.travelah.travelahapp.view.main.MainActivity
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var factory: ViewModelFactory
    private val editProfileViewModel: EditProfileViewModel by viewModels { factory }
    private var getFile: File? = null
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        factory = ViewModelFactory.getInstance(this)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        setContentView(binding.root)
        setupView()
        setupAction()
    }

    private fun setupView() {
        editProfileViewModel.getProfile().observe(this) { profile ->
            binding.apply {
                etName.setText(profile.fullName)
                etAge.setText(profile.age.toString())
                etOccupation.setText(profile.occupation)
                etLocation.setText(profile.location)
                etAboutMe.setText(profile.aboutMe)
            }
            if (profile.photo != "") {
                Glide.with(this)
                    .load(profile.photo)
                    .into(binding.imageProfilePreview)
            }
        }
    }

    @Suppress("DEPRECATION")
    @RequiresApi(Build.VERSION_CODES.Q)
    private fun setupAction() {
        binding.buttonBack.setOnClickListener {
            onBackPressed()
        }
        binding.buttonCamera.setOnClickListener {
            startTakePhoto()
        }
        binding.buttonGallery.setOnClickListener {
            startIntentGallery()
        }
        binding.buttonSaveChanges.setOnClickListener {
            updateProfile()
        }
    }

    private fun updateProfile() {
        val token = intent.getStringExtra(EXTRA_TOKEN).toString()

        when {
            getFile == null -> {
                var fullName = (binding.etName.text.toString()).toRequestBody("text/plain".toMediaType())
                val ageText = binding.etAge.text.toString()
                var age = ageText.toDoubleOrNull()?.toInt()?.toString()?.toRequestBody("text/plain".toMediaType())
                var occupation = (binding.etOccupation.text.toString()).toRequestBody("text/plain".toMediaType())
                var location = (binding.etLocation.text.toString()).toRequestBody("text/plain".toMediaType())
                var aboutMe = (binding.etAboutMe.text.toString()).toRequestBody("text/plain".toMediaType())
                editProfileViewModel.updateProfile(token, null, fullName, age, occupation, location, aboutMe).observe(this) { response ->
                    Toast.makeText(this, response, Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@EditProfileActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
            else -> {
                val file = reduceFileImage(getFile as File)
                var fullName = (binding.etName.text.toString()).toRequestBody("text/plain".toMediaType())
                val ageText = binding.etAge.text.toString()
                var age = ageText.toDoubleOrNull()?.toInt()?.toString()?.toRequestBody("text/plain".toMediaType())
                var occupation = (binding.etOccupation.text.toString()).toRequestBody("text/plain".toMediaType())
                var location = (binding.etLocation.text.toString()).toRequestBody("text/plain".toMediaType())
                var aboutMe = (binding.etAboutMe.text.toString()).toRequestBody("text/plain".toMediaType())
                val requestImageFile = file.asRequestBody("image/jpeg".toMediaType())
                val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                    "photo",
                    file.name,
                    requestImageFile
                )

                editProfileViewModel.updateProfile(token, imageMultipart, fullName, age, occupation, location, aboutMe).observe(this) { response ->
                    Toast.makeText(this, response, Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@EditProfileActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }

    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImage = result.data?.data as Uri
            selectedImage.let { uri ->
                val myFile = uriToFile(uri, this@EditProfileActivity)
                getFile = myFile
                binding.imageProfilePreview.setImageURI(uri)
            }
        }
    }
    private fun startIntentGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }


    private lateinit var currentPhotoPath: String
    @RequiresApi(Build.VERSION_CODES.Q)
    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)

            val exif = ExifInterface(myFile)
            val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)
            var angle: Float = 0F

            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> {
                    angle = 90F
                }
                ExifInterface.ORIENTATION_ROTATE_180 -> {
                    angle = 180F
                }
                ExifInterface.ORIENTATION_ROTATE_270 -> {
                    angle = 270F
                }
                else -> {
                    angle = 0F
                }
            }

            myFile.let { file ->
                rotateFile(file, angle)
                getFile = file
                binding.imageProfilePreview.setImageBitmap(BitmapFactory.decodeFile(file.path))
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        createCustomTempFile(application).also {
            val photoUri: Uri = FileProvider.getUriForFile(
                this@EditProfileActivity,
                "com.travelah.travelahapp",
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
            launcherIntentCamera.launch(intent)
        }
    }

    companion object {
        const val EXTRA_TOKEN = "extra_token"
    }
}