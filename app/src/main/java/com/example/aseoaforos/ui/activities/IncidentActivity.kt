package com.example.aseoaforos.ui.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.aseoaforos.R
import com.example.aseoaforos.mock.entity.Incident
import com.example.aseoaforos.mock.provider.CallAPI
import com.example.aseoaforos.mock.provider.Data
import com.example.aseoaforos.util.ActivityResultCodes
import com.example.aseoaforos.util.DateUtil
import com.example.aseoaforos.util.Validator

class IncidentActivity : AppCompatActivity() {

    private lateinit var photoLauncher: ActivityResultLauncher<Intent>
    private lateinit var incidentListLauncher: ActivityResultLauncher<Intent>
    private val photoList: MutableList<String> = mutableListOf()
    private val incidentList = Data.getIncidentList()
    private val incidentTypeList = Data.getIncidentTypeList()
    private var incidentId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_incident)

        val stateId = Data.getStateMicroRouteId("INCIDENTE")
        if (stateId != null) {
            if (CallAPI.updateStateMicroRoute(this, Data.getMicroRouteId(), stateId)) {
                Data.updateStateMicroRoute(stateId)
            }
        }

        val date = DateUtil.getCurrentDate()

        val startTime = System.currentTimeMillis()

        val spinType = findViewById<Spinner>(R.id.spinIncidentType)
        val adapterSpin = ArrayAdapter(
            this,
            R.layout.custom_spinner_items,
            incidentTypeList.map { it.name }
        ).apply {
            setDropDownViewResource(R.layout.custom_spinner_items_drop)
        }
        spinType.adapter = adapterSpin

        val etPlace = findViewById<EditText>(R.id.etIncidentPlace)

        val etDescription = findViewById<EditText>(R.id.etIncidentDescription)

        val cbAbandon = findViewById<CheckBox>(R.id.cbIncidentAbandon)

        val btnSave = findViewById<Button>(R.id.btnIncidentSave)
        btnSave.setOnClickListener {
            val incidentTypeId = incidentTypeList[spinType.selectedItemPosition].incidentTypeId
            val place = etPlace.text.toString().trim()
            val description = etDescription.text.toString().trim()

            if (validateData(incidentTypeId, place, description)) {
                incidentList.add(
                    0,
                    Incident(
                        description,
                        date,
                        incidentTypeId,
                        Data.getMicroRouteId(),
                        place,
                        photoList
                    )
                )
                if (cbAbandon.isChecked) {
                    Data.updateDescriptionAbandon(description)
                    setResult(ActivityResultCodes.FINISH_ROUTE)
                    finish()
                } else {
                    val intent = Intent(this, IncidentListActivity::class.java)
                    incidentListLauncher.launch((intent))
                }
            }
        }

        val imgBtnPhoto = findViewById<ImageButton>(R.id.imgBtnIncidentPhoto)
        imgBtnPhoto.setOnClickListener {
            val intent = Intent(this, PhotoActivity::class.java).apply {
                putExtra("photoList", ArrayList(photoList))
            }
            photoLauncher.launch(intent)
        }

        val btnCancel = findViewById<Button>(R.id.btnIncidentCancel)
        btnCancel.setOnClickListener {
            finish()
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

        incidentListLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val endTime = System.currentTimeMillis()
                    val elapsedTime = endTime - startTime
                    val time = elapsedTime / 1000

                    incidentList[0].incidentTime = time

                    val incidentId: Long?

                    if (this.incidentId == null) {
                        incidentId = CallAPI.createIncident(this, incidentList[0])
                        this.incidentId = incidentId
                    } else incidentId = this.incidentId

                    if (incidentId != null) {
                        if (CallAPI.addPhotosIncident(this, incidentId, photoList)) {
                            val data = Intent().apply {
                                putExtra("time", time)
                            }
                            setResult(RESULT_OK, data)
                            finish()
                        }
                    }
                } else {
                    incidentList.removeFirst()
                }
            }
    }

    private fun validateData(incidentTypeId: Long, place: String, description: String): Boolean {
        return Validator.graterThanZero(this, incidentTypeId, R.string.typeIncident) &&
                Validator.notEmptyField(this, place, R.string.incidentPlace) &&
                Validator.notEmptyField(this, description, R.string.incidentDescription) &&
                Validator.notEmptyPhotoList(this, photoList)
    }
}