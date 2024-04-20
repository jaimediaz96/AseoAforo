package com.example.aseoaforos.ui.activities

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aseoaforos.R
import com.example.aseoaforos.mock.entity.Collect
import com.example.aseoaforos.mock.provider.Data
import com.example.aseoaforos.ui.adapters.CollectAdapter
import com.example.aseoaforos.util.CollectTypeUtil
import com.example.aseoaforos.util.CollectionState
import com.example.aseoaforos.util.DateUtil
import com.example.aseoaforos.util.Validator
import java.math.BigDecimal
import java.math.RoundingMode

class CollectActivity : AppCompatActivity() {

    private lateinit var collectAdapter: CollectAdapter
    private lateinit var photoLauncher: ActivityResultLauncher<Intent>
    private lateinit var signLauncher: ActivityResultLauncher<Intent>
    private lateinit var locationManager: LocationManager
    private lateinit var collect: Collect
    private val recipientTypeList = Data.getRecipientTypeList()
    private val noCollectList = Data.getNotCollectedList()
    private var unit = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collect)

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val clientId = intent.getLongExtra("clientId", 0)
        val collectTypeId = intent.getLongExtra("collectTypeId", 0)

        collect = Data.getCollectList().first {
            it.clientId == clientId && it.collectTypeId == collectTypeId
        }
        val client = Data.getClientList().first {
            it.clientId == clientId && it.collectTypeId == collectTypeId
        }

        val tvValue = findViewById<TextView>(R.id.tvCollectValue)
        tvValue.text = collect.totalCollected.toString()

        val tvName = findViewById<TextView>(R.id.tvCollectClientName)
        tvName.text = client.companyName

        val tvAddress = findViewById<TextView>(R.id.tvCollectClientAddress)
        tvAddress.text = client.address

        val especialId = Data.getCollectTypeId(CollectTypeUtil.ESPECIAL.toString())
        val textCollectTypeUtil = if (client.collectTypeId == especialId) {
            getString(R.string.especialCollect)
        } else "${getString(R.string.CollectBy)} ${client.collectTypeId}"

        val tvAforo = findViewById<TextView>(R.id.tvCollectCollect)
        tvAforo.text = textCollectTypeUtil

        val cbCheck = findViewById<CheckBox>(R.id.cbCollectCheck)
        cbCheck.isChecked = collect.isCollected

        val spinNoCollect = findViewById<Spinner>(R.id.spinCollectCodeNoCollect)
        val adapterSpinNoCollect = ArrayAdapter(
            this,
            R.layout.custom_spinner_items,
            spinLabels()
        ).apply {
            setDropDownViewResource(R.layout.custom_spinner_items_drop)
        }
        spinNoCollect.adapter = adapterSpinNoCollect

        val index = noCollectList.indexOfFirst { it.noCollectId == collect.noCollectedId }
        spinNoCollect.setSelection(index)

        val recyclerView = findViewById<RecyclerView>(R.id.rcCollectList)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val onItemUpdate: () -> Unit = {
            val sum = BigDecimal(collect.recipientList.sumOf { it.total }.toString())
            val total = sum.setScale(4, RoundingMode.HALF_UP)
            tvValue.text = "$total"
        }

        collectAdapter = CollectAdapter(
            recipientTypeList.filter { it.collectTypeId == client.collectTypeId },
            collect.recipientList,
            onItemUpdate
        )

        recyclerView.adapter = collectAdapter

        val tvTypeTotal = findViewById<TextView>(R.id.tvCollectTypeTotal)
        tvTypeTotal.text = textCollectTypeUtil

        val tvUnits = findViewById<TextView>(R.id.tvCollectUnits)
        unit = when (Data.getCollectTypeName(collect.collectTypeId)) {
            CollectTypeUtil.PESO.toString() -> "TON"
            else -> "M^3"
        }
        tvUnits.text = unit

        val btnPhoto = findViewById<ImageButton>(R.id.btnCollectPhoto)
        btnPhoto.setOnClickListener {
            val intent = Intent(this, PhotoActivity::class.java).apply {
                putExtra("photoList", ArrayList(collect.photoList))
            }
            photoLauncher.launch(intent)
        }

        val btnSign = findViewById<Button>(R.id.btnCollectSign)
        btnSign.setOnClickListener {
            val intent = Intent(this, SignActivity::class.java).apply {
                putExtra("sign", collect.collectSign)
            }
            signLauncher.launch(intent)
        }

        val btnSave = findViewById<Button>(R.id.btnCollectSave)
        btnSave.setOnClickListener {
            if (collect.recipientList.isNotEmpty() && !cbCheck.isChecked) {
                cbCheck.isChecked = true
            }
            val indexNoCollect = spinNoCollect.selectedItemPosition
            val isCollect = cbCheck.isChecked
            if (validateData(isCollect, indexNoCollect)) {
                client.state = if (isCollect) CollectionState.COLLECTED
                else if (indexNoCollect != 0) CollectionState.NOT_COLLECTED
                else CollectionState.UNDEFINED

                saveAforo(
                    tvValue.text.toString().toDouble(),
                    isCollect,
                    noCollectList[indexNoCollect].noCollectId
                )

                val data = Intent().apply {
                    putExtra("clientId", clientId)
                    putExtra("typeAforo", collectTypeId)
                }
                setResult(RESULT_OK, data)
                finish()
            }
        }

        val btnCancel = findViewById<Button>(R.id.btnCollectCancel)
        btnCancel.setOnClickListener {
            finish()
        }

        photoLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data

                    val photoArrayList = data?.getStringArrayListExtra("photoList")
                    if (photoArrayList != null) {
                        collect.photoList.clear()
                        collect.photoList.addAll(photoArrayList)
                    }
                }
            }

        signLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data

                    val signPath = data?.getStringExtra("sign") ?: ""
                    if (data != null) {
                        collect.collectSign = signPath
                    }
                }
            }
    }

    private fun spinLabels(): List<String> {
        val labels = mutableListOf<String>()
        for (noCollect in noCollectList) {
            labels.add("${noCollect.code}   ${noCollect.description}")
        }
        return labels
    }

    private fun saveAforo(total: Double, isCollect: Boolean, noCollectId: Long) {
        collect.totalCollected = total
        collect.isCollected = isCollect
        collect.collectDate = DateUtil.getCurrentDate()
        collect.noCollectedId = noCollectId
    }

    private fun validateData(isCollect: Boolean, indexNoCollect: Int): Boolean {
        if (isCollect && indexNoCollect != 0) {
            Toast.makeText(
                this,
                getString(R.string.errorCollectNoCollect),
                Toast.LENGTH_LONG
            ).show()
            return false
        } else if (collect.recipientList.isEmpty() && isCollect) {
            Toast.makeText(
                this,
                getString(R.string.errorRecipientEmpty),
                Toast.LENGTH_LONG
            ).show()
            return false
        } else if (!getLocation()) {
            Toast.makeText(
                this,
                getString(R.string.errorPermissionLocation),
                Toast.LENGTH_LONG
            ).show()
            return false
        }
        return Validator.notEmptyPhotoList(this, collect.photoList)
    }

    private fun getLocation(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            val permission = arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            ActivityCompat.requestPermissions(this, permission, 0)
            return false
        }

        val location: Location? = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            ?: locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        location?.let {
            collect.latitude = it.latitude
            collect.longitude = it.longitude
        }
        return true
    }
}