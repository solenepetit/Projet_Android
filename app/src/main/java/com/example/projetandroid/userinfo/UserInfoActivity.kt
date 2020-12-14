package com.example.projetandroid.userinfo

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import coil.load
import coil.transform.CircleCropTransformation
import com.example.projetandroid.BuildConfig
import com.example.projetandroid.R
import com.example.projetandroid.network.UserInfo
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
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
/* var sendImageButton = findViewById<Button>(R.id.send_user_info)

        var firstName = findViewById<EditText>(R.id.edit_first_name)
        var lastName = findViewById<EditText>(R.id.edit_last_name)
        var email = findViewById<EditText>(R.id.edit_email)
        var image = findViewById<ImageView>(R.id.test_image)

        takePictureButton.setOnClickListener {
            askCameraPermissionAndOpenCamera()
@ -55,6 +76,13 @@ class UserInfoActivity : AppCompatActivity() {
            // use
            pickInGallery.launch("image")
        }

        sendImageButton.setOnClickListener {
            val newUserInfo = UserInfo(email.text.toString(), firstName.text.toString(),lastName.text.toString(), userViewModel.userInfo?.avatar.toString())
            lifecycleScope.launch {
                userViewModel.updateInfo(newUserInfo)
            }
        }*/
        val takePictureButton = findViewById<Button>(R.id.take_picture_button)
        val uploadImageButton = findViewById<Button>(R.id.upload_image_button)
        val sendImageButton = findViewById<Button>(R.id.send_user_info)
        val firstName = findViewById<EditText>(R.id.edit_first_name)
        val lastName = findViewById<EditText>(R.id.edit_last_name)
        val email = findViewById<EditText>(R.id.edit_email)
        val image = findViewById<ImageView>(R.id.test_image)

        takePictureButton.setOnClickListener {
            askCameraPermissionAndOpenCamera()
        }
        uploadImageButton.setOnClickListener {
            pickInGallery.launch("image/*")
        }

        sendImageButton.setOnClickListener {
            val newUserInfo = UserInfo(email.text.toString(), firstName.text.toString(), lastName.text.toString(), userViewModel.userInfo.value?.avatar.toString())
            lifecycleScope.launch {
                userViewModel.updateInfo(newUserInfo)
            }
        }
        userViewModel.userInfo.observe(this) {
            firstName?.setText(it.firstName)
            lastName?.setText(it.lastName)
            email?.setText(it.email)
            image?.load(it.avatar) {
                crossfade(true)
                crossfade(1000)
                transformations(CircleCropTransformation())
            }
        }
    }

    override fun onResume() {
        super.onResume()
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

    private val photoUri by lazy {
        FileProvider.getUriForFile(
                this,
                BuildConfig.APPLICATION_ID +".fileprovider",
                File.createTempFile("avatar", ".jpeg", externalCacheDir)

        )
    }

    // register
    private val takePicture = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) handleImage(photoUri)
        else Toast.makeText(this, "Erreur ! 😢", Toast.LENGTH_LONG).show()
    }

    // use
    private fun openCamera() = takePicture.launch(photoUri)

    // convert
    private fun convert(uri: Uri) =
            MultipartBody.Part.createFormData(
                    name = "avatar",
                    filename = "temp.jpeg",
                    body = contentResolver.openInputStream(uri)!!.readBytes().toRequestBody()
            )

    private fun handleImage(uri : Uri) {
        lifecycleScope.launch {
            userViewModel.updateAvatar(convert(uri))
            userViewModel.refresh()
        }
    }
}