package com.example.aseoaforos.ui.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.aseoaforos.R
import com.example.aseoaforos.mock.provider.Data
import com.example.aseoaforos.mock.entity.Refuel
import com.example.aseoaforos.mock.provider.CallAPI
import com.example.aseoaforos.util.ActivityResultCodes
import com.example.aseoaforos.util.DateUtil
import com.example.aseoaforos.util.Validator

class RefuelActivity : AppCompatActivity() {

    private lateinit var photoLauncher: ActivityResultLauncher<Intent>
    private lateinit var refuelListLauncher: ActivityResultLauncher<Intent>
    private lateinit var incidentLauncher: ActivityResultLauncher<Intent>
    private val photoList = mutableListOf<String>()
    private val refuelList = Data.getRefuelList()
    private var cutTime: Long = 0L
    private var refuelId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_refuel)

        val stateId = Data.getStateMicroRouteId("REPOSTAJE")
        if (stateId != null) {
            if (CallAPI.updateStateMicroRoute(this, Data.getMicroRouteId(), stateId)) {
                Data.updateStateMicroRoute(stateId)
            }
        }

        val startTime = System.currentTimeMillis()
        val date = DateUtil.getCurrentDate()

        val etPlace = findViewById<EditText>(R.id.etRefuelPlace)

        val etQuantity = findViewById<EditText>(R.id.etRefuelQuantity)

        val etCost = findViewById<EditText>(R.id.etRefuelCost)

        val imgBtnPhoto = findViewById<ImageButton>(R.id.imgBtnRefuelPhoto)
        imgBtnPhoto.setOnClickListener {
            val intent = Intent(this, PhotoActivity::class.java).apply {
                putExtra("photoList", ArrayList(photoList))
            }
            photoLauncher.launch(intent)
        }

        val btnAccept = findViewById<Button>(R.id.btnRefuelAccept)
        btnAccept.setOnClickListener {
            val place = etPlace.text.toString().trim()
            val quantity = etQuantity.text.toString().toDoubleOrNull() ?: 0.0
            val cost = etCost.text.toString().toDoubleOrNull() ?: 0.0

            if (validateData(place, quantity, cost)) {
                refuelList.add(
                    0,
                    Refuel(
                        place,
                        quantity,
                        cost,
                        date,
                        Data.getMicroRouteId(),
                        photoList
                    )
                )

                val intent = Intent(this, RefuelListActivity::class.java)
                refuelListLauncher.launch((intent))
            }
        }

        val btnCancel = findViewById<Button>(R.id.btnRefuelCancel)
        btnCancel.setOnClickListener {
            finish()
        }

        val imgBtnIncident = findViewById<ImageButton>(R.id.imgBtnRefuelIncident)
        imgBtnIncident.setOnClickListener {
            val intent = Intent(this, IncidentActivity::class.java)
            incidentLauncher.launch(intent)
        }

        photoLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data

                    val photoArrayList = data?.getStringArrayListExtra("photoList")
                    if (photoArrayList != null) {
                        photoList.clear()
                        photoList.addAll(photoArrayList)
                    }
                }
            }

        refuelListLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val endTime = System.currentTimeMillis()
                    val elapsedTime = endTime - startTime
                    val time = elapsedTime / 1000

                    refuelList[0].refuelTime = time - cutTime

                    val refuelId: Long?

                    if (this.refuelId == null) {
                        refuelId = CallAPI.createRefuel(this, refuelList[0])
                        this.refuelId = refuelId
                    } else refuelId = this.refuelId

                    if (refuelId != null) {
                        if (CallAPI.addPhotosRefuel(this, refuelId, photoList)) {
                            val data = Intent().apply {
                                putExtra("time", time)
                            }
                            setResult(RESULT_OK, data)
                            finish()
                        }
                    }
                } else {
                    refuelList.removeFirst()
                }
            }

        incidentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                when (result.resultCode) {
                    Activity.RESULT_OK -> {
                        val data: Intent? = result.data

                        val time = data?.getLongExtra("time", 0L) ?: 0L

                        cutTime += time
                    }

                    ActivityResultCodes.FINISH_ROUTE -> {
                        setResult(ActivityResultCodes.FINISH_ROUTE)
                        finish()
                    }
                }
            }
    }

    private fun validateData(place: String, quantity: Double, cost: Double): Boolean {
        return Validator.notEmptyField(this, place, R.string.place) &&
                Validator.graterThanZero(this, quantity, R.string.quantity) &&
                Validator.graterThanZero(this, cost, R.string.cost) &&
                Validator.notEmptyPhotoList(this, photoList)
    }
}