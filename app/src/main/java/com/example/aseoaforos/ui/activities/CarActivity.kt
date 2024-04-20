package com.example.aseoaforos.ui.activities

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.aseoaforos.R
import com.example.aseoaforos.mock.entity.Truck
import com.example.aseoaforos.mock.provider.CallAPI
import com.example.aseoaforos.mock.provider.Data
import com.example.aseoaforos.util.ActivityResultCodes
import com.example.aseoaforos.util.Validator

class CarActivity : AppCompatActivity() {

    private lateinit var crewLauncher: ActivityResultLauncher<Intent>
    private var isPlateChange = false
    private var truckId: Long? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_car)

        val tvPlate = findViewById<TextView>(R.id.tvCarPlate)
        tvPlate.text = Data.getMicroRoute().plate

        val etKm = findViewById<TextView>(R.id.etCarKm)

        val spinStateWheels = findViewById<Spinner>(R.id.spinCarTireCondition)
        val adapterSpinStateWheels: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
            this,
            R.array.spinTireCondition,
            R.layout.custom_spinner_items
        ).apply {
            setDropDownViewResource(R.layout.custom_spinner_items_drop)
        }
        spinStateWheels.adapter = adapterSpinStateWheels

        val spinFuel = findViewById<Spinner>(R.id.spinCarFuelLevel)
        val adapterSpinFuel: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
            this,
            R.array.spinFuelLevel,
            R.layout.custom_spinner_items
        ).apply {
            setDropDownViewResource(R.layout.custom_spinner_items_drop)
        }
        spinFuel.adapter = adapterSpinFuel

        val spinLights = findViewById<Spinner>(R.id.spinCarBreakLights)
        val adapterSpinLights: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
            this,
            R.array.spinBreakLights,
            R.layout.custom_spinner_items
        ).apply {
            setDropDownViewResource(R.layout.custom_spinner_items_drop)
        }
        spinLights.adapter = adapterSpinLights

        val spinHorn = findViewById<Spinner>(R.id.spinCarHornCondition)
        val adapterSpinHorn: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
            this,
            R.array.spinHornCondition,
            R.layout.custom_spinner_items
        ).apply {
            setDropDownViewResource(R.layout.custom_spinner_items_drop)
        }
        spinHorn.adapter = adapterSpinHorn

        val spinBrake = findViewById<Spinner>(R.id.spinCarBrakeFluid)
        val adapterSpinBrake: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
            this,
            R.array.spinBrakeFluid,
            R.layout.custom_spinner_items
        ).apply {
            setDropDownViewResource(R.layout.custom_spinner_items_drop)
        }
        spinBrake.adapter = adapterSpinBrake

        val spinOil = findViewById<Spinner>(R.id.spinCarOilLevel)
        val adapterSpinOil: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
            this,
            R.array.spinOilLevel,
            R.layout.custom_spinner_items
        ).apply {
            setDropDownViewResource(R.layout.custom_spinner_items_drop)
        }
        spinOil.adapter = adapterSpinOil

        val cbDocument = findViewById<CheckBox>(R.id.cbCarCarDocument)

        val etObservation = findViewById<EditText>(R.id.etCarAddObservation)

        val btnCrew = findViewById<Button>(R.id.btnCarCrew)
        btnCrew.setOnClickListener {
            if (Validator.notEmptyField(this, etKm.text.toString(), R.string.kmStart)) {
                val intent = Intent(this, CrewActivity::class.java)
                crewLauncher.launch(intent)
            }
        }

        val btnChange = findViewById<Button>(R.id.btnCarChange)
        btnChange.setOnClickListener {
            showDialogChangeCar()
        }

        val btnCancel = findViewById<Button>(R.id.btnCarCancel)
        btnCancel.setOnClickListener {
            if (isPlateChange) setResult(ActivityResultCodes.CHANGE_PLATE)
            finish()
        }

        crewLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    if (!isPlateChange) truckId = CallAPI.getByPlate(this, tvPlate.text.toString())

                    val capacity = CallAPI.getTruckCapacity(this, truckId!!)

                    if (capacity != null) {
                        saveCarState(
                            Truck(
                                truckId!!,
                                tvPlate.text.toString(),
                                cbDocument.isChecked,
                                etKm.text.toString().toLong(),
                                spinStateWheels.selectedItem.toString(),
                                spinStateWheels.selectedItem.toString(),
                                spinFuel.selectedItem.toString(),
                                spinLights.selectedItem.toString(),
                                spinHorn.selectedItem.toString(),
                                spinBrake.selectedItem.toString(),
                                spinOil.selectedItem.toString(),
                                etObservation.text.toString(),
                                capacity
                            )
                        )

                        setResult(RESULT_OK)
                        finish()
                    }
                }
            }
    }

    private fun showDialogChangeCar() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_change_car)
        dialog.setCanceledOnTouchOutside(true)

        val window = dialog.window
        if (window != null) {
            window.attributes = window.attributes.apply {
                dimAmount = 0.5f
            }
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        }

        val etPlate = dialog.findViewById<EditText>(R.id.etDialogChangeCarPlate)

        val btnAccept = dialog.findViewById<Button>(R.id.btnDialogChangeCarAccept)
        btnAccept.setOnClickListener {
            val plateText = etPlate.text.toString().trim().uppercase()

            if (Validator.plate(this, plateText)) {
                val truckId = CallAPI.getByPlate(this, plateText)

                if (truckId != null) {
                    if (CallAPI.updateMicroRouteTruckId(this, truckId)) {
                        Data.updateRoutePlate(plateText)

                        val tvCarPlate = findViewById<TextView>(R.id.tvCarPlate)
                        tvCarPlate.text = plateText

                        isPlateChange = true

                        this.truckId = truckId

                        dialog.dismiss()
                    }
                }
            }
        }

        dialog.show()
    }

    private fun saveCarState(truck: Truck) {
        if (CallAPI.updateTruck(this, truck)) {
            Data.setTruck(truck)
        }
    }
}