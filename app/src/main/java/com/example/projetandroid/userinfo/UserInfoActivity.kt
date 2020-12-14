package com.example.projetandroid.userinfo

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import coil.load
import coil.transform.CircleCropTransformation
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toFile
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import com.example.projetandroid.R
import com.example.projetandroid.network.Api
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class UserInfoActivity : AppCompatActivity() {

    // register
    private val pickInGallery =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            handleImage(uri)
        }
    private val userViewModel : UserInfoViewModel by viewModels()



    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)

        var takePictureButton = findViewById<Button>(R.id.take_picture_button)
        var uploadImageButton = findViewById<Button>(R.id.upload_image_button)

        takePictureButton.setOnClickListener {
            askCameraPermissionAndOpenCamera()
        }
        uploadImageButton.setOnClickListener {
            // use
            pickInGallery.launch("image/*")
        }
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            var firstName = findViewById<EditText>(R.id.edit_first_name)
            var lastName = findViewById<EditText>(R.id.edit_last_name)
            var email = findViewById<EditText>(R.id.edit_email)
            var image = findViewById<ImageView>(R.id.test_image)
            val userInfo = Api.userService.getInfo().body()
            //val userInfo = UserInfo("", "", "")
            firstName?.setText(userInfo?.firstName)
            lastName?.setText(userInfo?.lastName)
            email?.setText(userInfo?.email)
            image?.load(userInfo?.avatar) {
                crossfade(true)
                crossfade(1000)
                transformations(CircleCropTransformation())
            }
        }
        lifecycleScope.launch {
            userViewModel.refresh()
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) openCamera()
            else showExplanationDialog()
        }

    private fun requestCameraPermission() =
        requestPermissionLauncher.launch(Manifest.permission.CAMERA)

    @RequiresApi(Build.VERSION_CODES.M)
    private fun askCameraPermissionAndOpenCamera() {
        when {
            ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED -> openCamera()
            shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> showExplanationDialog()
            else -> requestCameraPermission()
        }
    }

    private fun showExplanationDialog() {
        AlertDialog.Builder(this).apply {
            setMessage("On a besoin de la caméra sivouplé ! 🥺")
            setPositiveButton("Bon, ok") { _, _ ->
                requestCameraPermission()
            }
            setCancelable(true)
            show()
        }
    }

    // create a temp file and get a uri for it
    //private val photoUri = getContentUri("temp")

    // register
    private val takePicture = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { picture ->
        val tmpFile = File.createTempFile("avatar", "jpeg")
        tmpFile.outputStream().use {
            picture.compress(Bitmap.CompressFormat.JPEG, 100, it)
        }
        handleImage(tmpFile.toUri())
    }


    // use
    private fun openCamera() = takePicture.launch()

    // convert
    private fun convert(uri: Uri) =
        MultipartBody.Part.createFormData(
            name = "avatar",
            filename = "temp.jpeg",
            body = uri.toFile().asRequestBody()
        )

    private fun handleImage(uri : Uri) {
        lifecycleScope.launch {
            userViewModel.updateAvatar(convert(uri))
        }
    }
}