package com.example.aseoaforos.ui.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.aseoaforos.R
import com.example.aseoaforos.mock.entity.Collect
import com.example.aseoaforos.mock.entity.Client
import com.example.aseoaforos.mock.entity.TempClient
import com.example.aseoaforos.mock.provider.CallAPI
import com.example.aseoaforos.mock.provider.Data
import com.example.aseoaforos.util.DateUtil
import com.example.aseoaforos.util.Validator

class NewClientActivity : AppCompatActivity() {

    private lateinit var photoLauncher: ActivityResultLauncher<Intent>
    private val photoList: MutableList<String> = mutableListOf()
    private val clientList = Data.getClientList()
    private val tempClientList = Data.getTempClientList()
    private var tempClientId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_client)

        val collectTypeId = intent.getLongExtra("collectTypeId", 0L)

        val etNit = findViewById<EditText>(R.id.etNewClientNit)

        val etName = findViewById<EditText>(R.id.etNewClientCompanyName)

        val etAddress = findViewById<EditText>(R.id.etNewClientAddress)

        val etPhone = findViewById<EditText>(R.id.etNewClientPhone)

        val etContactName = findViewById<EditText>(R.id.etNewClientContactName)

        val etContactPhone = findViewById<EditText>(R.id.etNewClientContactPhone)

        val etEmail = findViewById<EditText>(R.id.etNewClientEmail)

        val cbDocument = findViewById<CheckBox>(R.id.cbNewClientDocument)

        val btnAccept = findViewById<Button>(R.id.btnNewClientAccept)
        btnAccept.setOnClickListener {
            val nit = etNit.text.toString()
            val name = etName.text.toString()
            val address = etAddress.text.toString()
            val phone = etPhone.text.toString()
            val contactName = etContactName.text.toString()
            val contactPhone = etContactPhone.text.toString()
            val email = etEmail.text.toString()
            val document = cbDocument.isChecked

            if (validate(nit, name, address, phone, contactName, contactPhone, email, document)) {

                val tempClient = TempClient(
                    null,
                    name,
                    nit,
                    phone,
                    address,
                    email,
                    contactName,
                    contactPhone,
                    collectTypeId,
                    photoList
                )

                val tempClientId: Long?

                if (this.tempClientId == null) {
                    tempClientId = CallAPI.addTempClient(this, tempClient)
                    this.tempClientId = tempClientId
                } else tempClientId = this.tempClientId

                if (tempClientId != null) {
                    if (CallAPI.addPhotosTempClient(this, tempClientId, photoList)) {
                        tempClient.tempClientId = tempClientId

                        clientList.add(
                            Client(
                                tempClientId,
                                name,
                                address,
                                collectTypeId,
                                tempClientId = tempClientId
                            )
                        )

                        Data.addCollect(
                            Collect(
                                collectTypeId = collectTypeId,
                                collectDate = DateUtil.getCurrentDate(),
                                microRouteId = Data.getMicroRouteId(),
                                tempClientId = tempClientId,
                            )
                        )

                        tempClientList.add(tempClient)

                        setResult(RESULT_OK)
                        finish()
                    }
                }
            }
        }

        val imgBtnPhoto = findViewById<ImageButton>(R.id.imgBtnNewClientPhoto)
        imgBtnPhoto.setOnClickListener {
            val intent = Intent(this, PhotoActivity::class.java).apply {
                putExtra("photoList", ArrayList(photoList))
            }
            photoLauncher.launch(intent)
        }

        val btnCancel = findViewById<Button>(R.id.btnNewClientCancel)
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
    }

    private fun validate(
        nit: String,
        name: String,
        address: String,
        phone: String,
        contactName: String,
        contactPhone: String,
        email: String,
        document: Boolean
    ): Boolean {
        val notEmptyField = Validator.notEmptyField(this, nit, R.string.nit) &&
                Validator.notEmptyField(this, name, R.string.companyName) &&
                Validator.notEmptyField(this, address, R.string.address) &&
                Validator.notEmptyField(this, phone, R.string.phone) &&
                Validator.notEmptyField(this, contactName, R.string.contactName) &&
                Validator.notEmptyField(this, contactPhone, R.string.contactPhone) &&
                Validator.notEmptyField(this, email, R.string.email)

        val needPhoto = if (document) Validator.notEmptyPhotoList(this, photoList) else false

        val validateEmail = Validator.email(this, email)

        return notEmptyField && needPhoto && validateEmail
    }
}