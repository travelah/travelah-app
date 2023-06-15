package com.travelah.travelahapp.view.post

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.travelah.travelahapp.R
import com.travelah.travelahapp.data.Result
import com.travelah.travelahapp.data.remote.models.Post
import com.travelah.travelahapp.databinding.ActivityAddEditPostBinding
import com.travelah.travelahapp.utils.createCustomTempFile
import com.travelah.travelahapp.utils.reduceFileImage
import com.travelah.travelahapp.utils.rotateFile
import com.travelah.travelahapp.utils.uriToFile
import com.travelah.travelahapp.view.ViewModelFactory
import com.travelah.travelahapp.view.main.MainViewModel
import com.travelah.travelahapp.view.maps.MapsActivity
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class AddEditPostActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddEditPostBinding

    private var currLat: Double? = null
    private var currLong: Double? = null
    private var currAddress: String? = null

    private var getFile: File? = null
    private lateinit var currentPhotoPath: String

    private lateinit var factory: ViewModelFactory
    private val mainViewModel: MainViewModel by viewModels { factory }
    private val postViewModel: PostViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditPostBinding.inflate(layoutInflater)

        factory = ViewModelFactory.getInstance(this)

        setContentView(binding.root)

        val post = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(PostDetailActivity.EXTRA_POST, Post::class.java)
        } else {
            @Suppress("deprecation")
            intent.getParcelableExtra(PostDetailActivity.EXTRA_POST)
        }

        post?.let {
            binding.apply {
                titlePostInput.setText(it.title)
                descriptionInput.setText(it.description)
                locationText.text = it.location
            }

            Glide.with(this).load("${post.postPhotoPath}/${post.postPhotoName}")
                .into(binding.previewImageView)
            currLat = post.latitude
            currLong = post.longitude
        }

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        binding.cameraButton.setOnClickListener { startTakePhoto() }
        binding.galleryButton.setOnClickListener { startGallery() }
        binding.changeLocationButton.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            if (post != null) {
                intent.putExtra(PostDetailActivity.EXTRA_POST, post)
            }

            launcherIntentLocation.launch(intent)
        }
    }

    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        createCustomTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this@AddEditPostActivity,
                getString(R.string.package_name),
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)

            val exif = ExifInterface(myFile)
            val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)
            var angle: Float = 0F

            angle = when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> {
                    90F
                }
                ExifInterface.ORIENTATION_ROTATE_180 -> {
                    180F
                }
                ExifInterface.ORIENTATION_ROTATE_270 -> {
                    270F
                }
                else -> {
                    0F
                }
            }

            myFile.let { file ->
                rotateFile(file, angle)
                getFile = file
                binding.previewImageView.setImageBitmap(BitmapFactory.decodeFile(file.path))
            }
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri

            val myFile = uriToFile(selectedImg, this@AddEditPostActivity)

            getFile = myFile

            binding.previewImageView.setImageURI(selectedImg)
        }
    }

    private val launcherIntentLocation = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == MAPS_RESULT) {
            currLong = result.data?.getDoubleExtra("long", 0.0)
            currLat = result.data?.getDoubleExtra("lat", 0.0)

            currAddress = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                result.data?.getSerializableExtra("address", String::class.java)
            } else {
                @Suppress("DEPRECATION")
                result.data?.getSerializableExtra("address")
            } as? String

            binding.locationText.text = currAddress
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_add_edit_post, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_add_edit -> {
                uploadPost()
                true
            }
            else -> true
        }
    }

    private fun uploadPost() {
        if (getFile != null && binding.descriptionInput.error == null && binding.titlePostInput.error == null) {
            val file = reduceFileImage(getFile as File)

            val title =
                binding.titlePostInput.text.toString().toRequestBody("text/plain".toMediaType())

            val description =
                binding.descriptionInput.text.toString().toRequestBody("text/plain".toMediaType())

            val long = currLong.toString().toRequestBody("text/plain".toMediaType())
            val lat = currLat.toString().toRequestBody("text/plain".toMediaType())

            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile
            )

            mainViewModel.getToken().observe(this) { token ->
                if (token !== "") {

                    val post = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        intent.getParcelableExtra(PostDetailActivity.EXTRA_POST, Post::class.java)
                    } else {
                        @Suppress("deprecation")
                        intent.getParcelableExtra(PostDetailActivity.EXTRA_POST)
                    }

                    if (post != null) {
                        postViewModel.updatePost(
                            post.id,
                            imageMultipart,
                            title,
                            description,
                            token,
                            long,
                            lat
                        ).observe(this) { result ->
                            when (result) {
                                is Result.Loading -> {

                                }
                                is Result.Success -> {
                                    Toast.makeText(
                                        this,
                                        getString(R.string.upload_success),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    finish()
                                }
                                is Result.Error -> {
                                    Toast.makeText(
                                        this,
                                        "Error: ${result.error}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    } else {
                        postViewModel.createPost(
                            imageMultipart,
                            title,
                            description,
                            token,
                            long,
                            lat
                        ).observe(this) { result ->
                            when (result) {
                                is Result.Loading -> {

                                }
                                is Result.Success -> {
                                    Toast.makeText(
                                        this,
                                        getString(R.string.upload_success),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    finish()
                                }
                                is Result.Error -> {
                                    Toast.makeText(
                                        this,
                                        "Error cok: ${result.error}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    }
                }
            }
        } else if (getFile == null) {
            Toast.makeText(
                this@AddEditPostActivity,
                getString(R.string.not_put_the_picture),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Toast.makeText(
                this@AddEditPostActivity,
                getString(R.string.not_put_the_description),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    companion object {
        //        const val CAMERA_X_RESULT = 200
//        const val PICTURE_VALUE = "picture"
//        const val IS_BACK_CAMERA_VALUE = "isBackCamera"
//        const val TAG = "AddEditPostActivity"      const val CAMERA_X_RESULT = 200
//        const val PICTURE_VALUE = "picture"
//        const val IS_BACK_CAMERA_VALUE = "isBackCamera"
//        const val TAG = "AddEditPostActivity"
        const val MAPS_RESULT = 201

        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}