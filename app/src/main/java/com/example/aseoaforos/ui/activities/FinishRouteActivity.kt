package com.example.aseoaforos.ui.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.aseoaforos.MainActivity
import com.example.aseoaforos.R
import com.example.aseoaforos.mock.provider.CallAPI
import com.example.aseoaforos.mock.provider.Data

class FinishRouteActivity : AppCompatActivity() {

    private lateinit var getSignLauncher: ActivityResultLauncher<Intent>
    private var sign = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finish_route)

        val etKm = findViewById<EditText>(R.id.etFinishRouteKm)

        val etObservation = findViewById<EditText>(R.id.etFinishRouteFinishObservation)

        val btnSign = findViewById<Button>(R.id.btnFinishRouteSign)
        btnSign.setOnClickListener {
            val intent = Intent(this, SignActivity::class.java)
            getSignLauncher.launch(intent)
        }

        val btnFinish = findViewById<Button>(R.id.btnFinishRouteFinish)
        btnFinish.setOnClickListener {

            if (save(etKm.text.toString().toLong(), etObservation.text.toString())) {
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
        }

        getSignLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data

                    val signPath = data?.getStringExtra("sign") ?: ""
                    if (data != null) {
                        sign = signPath
                    }
                }
            }
    }

    private fun save(kmFinish: Long, observation: String): Boolean {
        Data.updateMicroRouteSign(sign)
        CallAPI.updateTruckKm(this, kmFinish)

        val micro = Data.getMicroRoute()

        if (micro.descriptionAbandon == null) micro.descriptionAbandon = observation
        else micro.descriptionAbandon += "\n ${getString(R.string.finishObservation)} $observation"

        return CallAPI.updateMicroRoute(this, micro)
    }
}