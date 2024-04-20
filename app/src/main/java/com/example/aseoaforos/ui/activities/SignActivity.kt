package com.example.aseoaforos.ui.activities

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import com.example.aseoaforos.R
import com.example.aseoaforos.ui.view.SignatureView
import com.example.aseoaforos.util.SignUtil
import java.io.File

class SignActivity : AppCompatActivity() {

    private lateinit var signUtil: SignUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign)

        signUtil = SignUtil(this)

        val imgPathSign = intent.getStringExtra("sign") ?: ""

        val imgSign = findViewById<ImageView>(R.id.signImage)

        val signatureView = findViewById<SignatureView>(R.id.signatureView)

        val imgFile = File(imgPathSign)
        if (imgFile.exists()) {
            val bitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
            imgSign.setImageBitmap(bitmap)
            imgSign.visibility = View.VISIBLE
            signatureView.visibility = View.GONE
        }

        val tvCanvasName = findViewById<TextView>(R.id.tvSignCanvasName)

        val etName = findViewById<EditText>(R.id.etSignName)
        etName.addTextChangedListener {
            tvCanvasName.text = etName.text
        }

        val btnChange = findViewById<Button>(R.id.btnSignChange)
        btnChange.setOnClickListener {
            signatureView.clear()
            imgSign.visibility = View.GONE
            signatureView.visibility = View.VISIBLE
        }

        val btnAccept = findViewById<Button>(R.id.btnSignAccept)
        btnAccept.setOnClickListener {
            val file = signUtil.createImageFile()
            val path = signatureView.saveSignature(file, signUtil.currentPath)
            val data = Intent().apply {
                putExtra("sign", path)
            }
            setResult(RESULT_OK, data)
            finish()
        }

        val btnCancel = findViewById<Button>(R.id.btnSignCancel)
        btnCancel.setOnClickListener {
            finish()
        }
    }
}