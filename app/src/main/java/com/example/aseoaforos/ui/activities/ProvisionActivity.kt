package com.example.aseoaforos.ui.activities

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aseoaforos.R
import com.example.aseoaforos.mock.provider.Data
import com.example.aseoaforos.mock.entity.Provision
import com.example.aseoaforos.mock.entity.Toll
import com.example.aseoaforos.mock.provider.CallAPI
import com.example.aseoaforos.ui.adapters.TollAdapter
import com.example.aseoaforos.util.ActivityResultCodes
import com.example.aseoaforos.util.DateUtil
import com.example.aseoaforos.util.Validator
import java.util.Date

class ProvisionActivity : AppCompatActivity() {

    private lateinit var photoLauncher: ActivityResultLauncher<Intent>
    private lateinit var refuelLauncher: ActivityResultLauncher<Intent>
    private lateinit var incidentLauncher: ActivityResultLauncher<Intent>
    private lateinit var tollAdapter: TollAdapter
    private val photoList = mutableListOf<String>()
    private val tollList = Data.getTollList()
    private val provisionTollList = Data.getTollList()
    private val provisionList = Data.getProvisionList()
    private val typeList = Data.getProvisionTypeList()
    private val placeList = Data.getProvisionPlaceList()
    private val provisionTollIdList: MutableList<Long?> = mutableListOf()
    private var isAcceptClicked = false
    private var cutTimeTrip: Long = 0L
    private var cutTimePlace: Long = 0L
    private var provisionId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_provision)

        val stateId = Data.getStateMicroRouteId("DISPOSICIÓN")
        if (stateId != null) {
            if (CallAPI.updateStateMicroRoute(this, Data.getMicroRouteId(), stateId)) {
                Data.updateStateMicroRoute(stateId)
            }
        }

        val isFinish = intent.getBooleanExtra("isFinish", false)

        val startTimeTrip = System.currentTimeMillis()
        var timeTrip = 0L
        val dateTrip = DateUtil.getCurrentDate()

        val tvDateStart = findViewById<TextView>(R.id.tvProvisionDateTimeStart)
        val textDateStart = "${DateUtil.formatDate(dateTrip)} ${DateUtil.formatTime(dateTrip)}"
        tvDateStart.text = textDateStart

        val etKmStart = findViewById<EditText>(R.id.etProvisionKmStart)

        val clTrip = findViewById<ConstraintLayout>(R.id.clProvisionContainerTrip)

        val clForm = findViewById<ConstraintLayout>(R.id.clProvisionContainerForm)

        val recyclerView = findViewById<RecyclerView>(R.id.rvProvisionTolls)
        recyclerView.layoutManager = LinearLayoutManager(this)

        tollAdapter = TollAdapter(provisionTollList)

        recyclerView.adapter = tollAdapter

        val tvQuantityToll = findViewById<TextView>(R.id.tvProvisionQuantityToll)
        tvQuantityToll.text = provisionList.size.toString()

        val imgBtnAddToll = findViewById<ImageButton>(R.id.imgBtnProvisionAddToll)
        imgBtnAddToll.setOnClickListener {
            provisionTollList.add(Toll(null, ""))

            tvQuantityToll.text = provisionTollList.size.toString()
            tollAdapter.notifyItemInserted(provisionTollList.lastIndex)
        }

        val imgBtnRemoveToll = findViewById<ImageButton>(R.id.imgBtnProvisionRemoveToll)
        imgBtnRemoveToll.setOnClickListener {
            provisionTollList.removeLast()

            tvQuantityToll.text = provisionTollList.size.toString()
            tollAdapter.notifyItemRemoved(provisionTollList.lastIndex)
        }

        var datePlace: Date
        var startTimePlace = 0L

        val tvDateTimePlace = findViewById<TextView>(R.id.tvProvisionDateTimeStartPlace)

        val spinType = findViewById<Spinner>(R.id.sProvisionType)
        val adapterSpinType = ArrayAdapter(
            this,
            R.layout.custom_spinner_items,
            typeList.map { it.name }
        ).apply {
            setDropDownViewResource(R.layout.custom_spinner_items_drop)
        }
        spinType.adapter = adapterSpinType

        val spinPlace = findViewById<Spinner>(R.id.sProvisionPlace)
        val adapterSpinPlace = ArrayAdapter(
            this,
            R.layout.custom_spinner_items,
            placeList.map { it.name }
        ).apply {
            setDropDownViewResource(R.layout.custom_spinner_items_drop)
        }
        spinPlace.adapter = adapterSpinPlace

        val etWeight = findViewById<EditText>(R.id.etProvisionWeight)

        val etKmFinish = findViewById<EditText>(R.id.etProvisionKmFinish)

        val btnPhoto = findViewById<ImageButton>(R.id.imgBtnProvisionPhoto)
        btnPhoto.setOnClickListener {
            val intent = Intent(this, PhotoActivity::class.java).apply {
                putExtra("photoList", ArrayList(photoList))
            }
            photoLauncher.launch(intent)
        }

        val btnAccept = findViewById<Button>(R.id.btnProvisionAccept)
        btnAccept.setOnClickListener {
            if (!isAcceptClicked) {
                stateForm(
                    etKmStart,
                    imgBtnAddToll,
                    imgBtnRemoveToll,
                    clTrip,
                    clForm,
                    btnAccept,
                    true
                )

                datePlace = DateUtil.getCurrentDate()
                startTimePlace = System.currentTimeMillis()

                val text = "${DateUtil.formatDate(datePlace)} ${DateUtil.formatTime(datePlace)}"
                tvDateTimePlace.text = text

                val endTime = System.currentTimeMillis()
                val elapsedTime = endTime - startTimeTrip
                timeTrip = elapsedTime / 1000
            } else {
                val kmStart = etKmStart.text.toString().toLongOrNull() ?: 0L
                val kmFinish = etKmFinish.text.toString().toLongOrNull() ?: 0L
                val weight = etWeight.text.toString().toDoubleOrNull() ?: 0.0

                val names = provisionTollList.map { it.name }.filter { name ->
                    tollList.any { it.name.equals(name, true) }
                }

                if (names.isNotEmpty()) {
                    provisionTollIdList.addAll(CallAPI.createToll(this, names))
                }

                val ids = provisionTollList.filter { provisionList ->
                    tollList.any { it.name.equals(provisionList.name, true) }
                }.map { it.tollId }

                provisionTollIdList.addAll(ids)

                if (validateDate(kmStart, kmFinish, weight)) {
                    val endTime = System.currentTimeMillis()
                    val elapsedTime = endTime - startTimePlace
                    val timePlace = elapsedTime / 1000

                    val typeId = typeList[spinType.selectedItemPosition].provisionTypeId
                    val placeId = placeList[spinPlace.selectedItemPosition].provisionPlaceId
                    val tollIds: List<Long> = provisionTollIdList.filterNotNull()

                    val provision = Provision(
                        kmStart,
                        kmFinish,
                        dateTrip,
                        weight,
                        Data.getMicroRouteId(),
                        placeId,
                        typeId,
                        tollIds,
                        photoList,
                        timeTrip,
                        timePlace
                    )

                    val provisionId: Long?

                    if (this.provisionId == null) {
                        provisionId = CallAPI.createProvision(this, provision)
                        this.provisionId = provisionId
                    } else provisionId = this.provisionId

                    if (provisionId != null) {
                        if (CallAPI.addPhotosProvision(this, provisionId, photoList)) {
                            provisionList.add(0, provision)

                            val data = Intent().apply {
                                putExtra("time", timeTrip + timePlace)
                            }

                            if (isFinish) setResult(ActivityResultCodes.FINISH_ROUTE, data)
                            else setResult(RESULT_OK, data)
                            finish()
                        }
                    }
                }
            }
        }

        val btnCancel = findViewById<Button>(R.id.btnProvisionCancel)
        btnCancel.setOnClickListener {
            if (isAcceptClicked) {
                isAcceptClicked = false
                stateForm(
                    etKmStart,
                    imgBtnAddToll,
                    imgBtnRemoveToll,
                    clTrip,
                    clForm,
                    btnAccept,
                    false
                )
            } else {
                finish()
            }
        }

        val btnIncident = findViewById<ImageButton>(R.id.imgBtnProvisionIncident)
        btnIncident.setOnClickListener {
            showDialogIncident()
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

        refuelLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data

                    val time = data?.getLongExtra("time", 0L) ?: 0L

                    if (isAcceptClicked) cutTimePlace += time
                    else cutTimeTrip += time
                }
            }

        incidentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                when (result.resultCode) {
                    Activity.RESULT_OK -> {
                        val data: Intent? = result.data

                        val time = data?.getLongExtra("time", 0L) ?: 0L

                        if (isAcceptClicked) cutTimePlace += time
                        else cutTimeTrip += time
                    }

                    ActivityResultCodes.FINISH_ROUTE -> {
                        setResult(ActivityResultCodes.FINISH_ROUTE)
                        finish()
                    }
                }
            }
    }

    private fun showDialogIncident() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_pause_route)
        dialog.setCanceledOnTouchOutside(true)

        val window = dialog.window
        if (window != null) {
            window.attributes = window.attributes.apply {
                dimAmount = 0.5f
            }
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        }

        val btnRefuel = dialog.findViewById<Button>(R.id.btnDialogPauseRouteRefuel)
        btnRefuel.setOnClickListener {
            dialog.dismiss()
            val intent = Intent(this, RefuelActivity::class.java)
            refuelLauncher.launch(intent)
        }

        val btnIncidence = dialog.findViewById<Button>(R.id.btnDialogPauseRouteIncidence)
        btnIncidence.setOnClickListener {
            dialog.dismiss()
            val intent = Intent(this, IncidentActivity::class.java)
            incidentLauncher.launch(intent)
        }

        val btnProvision = dialog.findViewById<Button>(R.id.btnDialogPauseRouteProvision)
        btnProvision.visibility = View.GONE

        dialog.show()
    }

    private fun stateForm(
        etKmStart: EditText,
        imgBtnAdd: ImageButton,
        imgBtnRemove: ImageButton,
        containerTrip: ConstraintLayout,
        containerForm: ConstraintLayout,
        btnAccept: Button,
        show: Boolean
    ) {
        isAcceptClicked = show

        etKmStart.isClickable = !show
        imgBtnAdd.isEnabled = !show
        imgBtnRemove.isEnabled = !show

        tollAdapter.setEditingEnabled(!show)

        if (show) {
            containerTrip.visibility = View.GONE
            containerForm.visibility = View.VISIBLE
        } else {
            containerTrip.visibility = View.VISIBLE
            containerForm.visibility = View.GONE
        }

        btnAccept.text = getString(R.string.provisionFinish)
    }

    private fun validateDate(kmStart: Long, kmFinish: Long, weight: Double): Boolean {
        return Validator.graterThanZero(this, kmStart, R.string.provisionKmStart) &&
                Validator.graterThanZero(this, kmFinish, R.string.provisionKmFinish) &&
                Validator.graterThanZero(this, weight, R.string.provisionTon) &&
                Validator.notEmptyPhotoList(this, photoList)
    }
}