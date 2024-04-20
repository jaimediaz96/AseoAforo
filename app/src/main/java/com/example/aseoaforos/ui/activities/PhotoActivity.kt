package com.example.aseoaforos.ui.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aseoaforos.R
import com.example.aseoaforos.ui.adapters.PhotoAdapter
import com.example.aseoaforos.util.PhotoUtil

class PhotoActivity : AppCompatActivity() {

    private val photoList = mutableListOf<String>()
    private lateinit var photoAdapter: PhotoAdapter
    private lateinit var photoUtil: PhotoUtil
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var takePhotoLauncher: ActivityResultLauncher<Uri>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo)

        val photoArrayList = intent.getStringArrayListExtra("photoList") ?: ArrayList()
        if (photoArrayList.isNotEmpty()) photoList.addAll(photoArrayList)

        val recyclerView = findViewById<RecyclerView>(R.id.rvPhoto)
        recyclerView.layoutManager = LinearLayoutManager(this)

        photoAdapter = PhotoAdapter(photoList)

        recyclerView.adapter = photoAdapter

        val imgBtnPhoto = findViewById<ImageView>(R.id.imgBtnPhotoPhoto)
        imgBtnPhoto.setOnClickListener {
            when (PackageManager.PERMISSION_GRANTED) {
                ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) -> {
                    val photoURI: Uri? = photoUtil.getPhotoUri()
                    photoURI?.let {
                        takePhotoLauncher.launch(it)
                    }
                }

                else -> requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }

        val bntAccept = findViewById<Button>(R.id.bntPhotoAccept)
        bntAccept.setOnClickListener {
            val data = Intent().apply {
                putExtra("photoList", ArrayList(photoList))
            }
            setResult(RESULT_OK, data)
            finish()
        }

        val bntCancel = findViewById<Button>(R.id.bntPhotoCancel)
        bntCancel.setOnClickListener {
            finish()
        }

        photoUtil = PhotoUtil(this)

        requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
                if (isGranted) {
                    val photoURI: Uri? = photoUtil.getPhotoUri()
                    photoURI?.let {
                        takePhotoLauncher.launch(it)
                    }
                } else {
                    Toast.makeText(
                        this,
                        getString(R.string.errorPermission),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

        takePhotoLauncher =
            registerForActivityResult(ActivityResultContracts.TakePicture()) { success: Boolean ->
                if (success) {
                    val photo = photoUtil.currentPhotoPath

                    photoList.add(0, photo)

                    photoAdapter.notifyItemInserted(0)
                }
            }
    }
}