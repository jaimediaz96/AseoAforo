package com.example.aseoaforos

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.aseoaforos.mock.entity.MicroRoute
import com.example.aseoaforos.mock.provider.CallAPI
import com.example.aseoaforos.mock.provider.CallAPIList
import com.example.aseoaforos.mock.provider.Data
import com.example.aseoaforos.ui.activities.MicroRouteActivity
import com.example.aseoaforos.util.Validator

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getData()

        val etUsername = findViewById<EditText>(R.id.etLoginUsername)

        val etPassword = findViewById<EditText>(R.id.etLoginPassword)

        val tvForgetPassword = findViewById<TextView>(R.id.tvLoginForgetPassword)
        tvForgetPassword.setOnClickListener {
            showDialogForgetPassword()
        }

        val btnLogin = findViewById<Button>(R.id.btnLoginLogin)
        btnLogin.setOnClickListener {
            if (validateLogin(etUsername.text.toString(), etPassword.text.toString())) {
                if (getMicroRoute()) {
                    val intent = Intent(this, MicroRouteActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }

    private fun showDialogForgetPassword() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_forget_password)
        dialog.setCanceledOnTouchOutside(true)

        val window = dialog.window
        if (window != null) {
            window.attributes = window.attributes.apply {
                dimAmount = 0.5f
            }
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        }

        val etEmail = dialog.findViewById<EditText>(R.id.etDialogForgetPassEmail)

        val btnAccept = dialog.findViewById<Button>(R.id.btnDialogForgetPassAccept)
        btnAccept.setOnClickListener {
            val email = etEmail.text.toString()

            if (Validator.email(this, email)) {
                CallAPI.forgetPassword(this, email)
                dialog.dismiss()
            }
        }

        dialog.show()
    }

    private fun validateLogin(username: String, password: String): Boolean {
        val tvError = findViewById<TextView>(R.id.tvLoginError)

        val user = CallAPI.authentication(username, password)

        return if (user != null) {
            tvError.visibility = View.GONE
            Data.setUser(user)
            true
        } else {
            tvError.visibility = View.VISIBLE
            false
        }
    }

    private fun getMicroRoute(): Boolean {
        val micro = CallAPI.getMicroRoute(this, Data.getUserId())

        return if (micro != null) {
            Data.setMicroRoute(
                MicroRoute(
                    micro.microRouteId,
                    micro.routeName,
                    micro.routeDescription,
                    micro.microRouteName,
                    micro.plate
                )
            )

            Data.setClientList(micro.clientList)

            true
        } else {
            false
        }
    }

    private fun getData() {
        Data.setStateMicroRouteList(CallAPIList.getStateMicroRouteList())
        Data.setCrewList(CallAPIList.getCrewList())
        Data.setProvisionTypeList(CallAPIList.getProvisionTypeList())
        Data.setProvisionPlaceList(CallAPIList.getProvisionPlaceList())
        Data.setTollList(CallAPIList.getTollList())
        Data.setIncidentTypeList(CallAPIList.getIncidentTypeList())
        Data.setNotCollectedList(CallAPIList.getNotCollectedList())
        Data.setFillPercentageList(CallAPIList.getFillPercentageList())
        Data.setCollectTypeList(CallAPIList.getCollectTypeList())
        Data.setRecipientTypeList(CallAPIList.getRecipientTypeList())
    }
}
