package com.example.projetandroid.userinfo

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
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
import coil.load
import coil.transform.CircleCropTransformation
import com.example.projetandroid.R
import com.example.projetandroid.network.Api
import com.example.projetandroid.network.UserInfo
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class UserInfoActivity : AppCompatActivity() {

    // register
    private val pickInGallery =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            handleImage1(uri)
        }
    private val userViewModel : UserInfoViewModel by viewModels()


    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        return super.onCreateView(name, context, attrs)
        lifecycleScope.launch {
            userViewModel.userInfo = Api.userService.getInfo().body()
        }
    }
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)

        var takePictureButton = findViewById<Button>(R.id.take_picture_button)
        var uploadImageButton = findViewById<Button>(R.id.upload_image_button)
        var sendImageButton = findViewById<Button>(R.id.send_user_info)

        var firstName = findViewById<EditText>(R.id.edit_first_name)
        var lastName = findViewById<EditText>(R.id.edit_last_name)
        var email = findViewById<EditText>(R.id.edit_email)
        var image = findViewById<ImageView>(R.id.test_image)

        takePictureButton.setOnClickListener {
            askCameraPermissionAndOpenCamera()
        }
        uploadImageButton.setOnClickListener {
            // use
            pickInGallery.launch("image/*")
        }

        sendImageButton.setOnClickListener {
            val newUserInfo = UserInfo(email.text.toString(), firstName.text.toString(),lastName.text.toString(), userViewModel.userInfo?.avatar.toString())
            lifecycleScope.launch {
                userViewModel.updateInfo(newUserInfo)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            var firstName = findViewById<EditText>(R.id.edit_first_name)
            var lastName = findViewById<EditText>(R.id.edit_last_name)
            var email = findViewById<EditText>(R.id.edit_email)
            var image = findViewById<ImageView>(R.id.test_image)
            userViewModel.userInfo = Api.userService.getInfo().body()
            val userInfo = userViewModel.userInfo
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
/*
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
        else Toast.makeText(
                this,
                "Erreur ! 😢",
                Toast.LENGTH_LONG
        ).show()
    }

    // use
    private fun openCamera() = takePicture.launch(photoUri)
*/
    // register
    private val takePicture = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        val tmpFile = File.createTempFile("avatar", "jpeg")
        tmpFile.outputStream().use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
        }
        handleImage(tmpFile.toUri())
    }

    // use
    private fun openCamera() = takePicture.launch()

    private fun convert(uri: Uri) =
            MultipartBody.Part.createFormData(
                    name = "avatar",
                    filename = "temp.jpeg",
                    body = uri.toFile().asRequestBody()
            )
    // convert
    private fun convert1(uri: Uri) =
            MultipartBody.Part.createFormData(
                    name = "avatar",
                    filename = "temp.jpeg",
                    body = contentResolver.openInputStream(uri)!!.readBytes().toRequestBody()
            )

    private fun handleImage(uri : Uri) {
        lifecycleScope.launch {
            userViewModel.updateAvatar(convert(uri))
        }
    }
    private fun handleImage1(uri : Uri) {
        lifecycleScope.launch {
            userViewModel.updateAvatar(convert1(uri))
        }
    }
}