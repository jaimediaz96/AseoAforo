package com.example.aseoaforos.ui.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.aseoaforos.R
import com.example.aseoaforos.mock.entity.Crew
import com.example.aseoaforos.mock.provider.Data
import com.example.aseoaforos.util.ActivityResultCodes
import com.example.aseoaforos.util.DateUtil

class MicroRouteActivity : AppCompatActivity() {

    private lateinit var carLauncher: ActivityResultLauncher<Intent>
    private val microRouteCrew = Data.getMicroRouteCrewList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_micro_route)

        val user = Data.getUser()
        val currentDate = DateUtil.getCurrentDate()
        val microRoute = Data.getMicroRoute()

        microRouteCrew.add(Crew(user.Userid, user.name))

        val tvRouteName = findViewById<TextView>(R.id.tvMicroRouteRouteName)
        tvRouteName.text = microRoute.routeName

        val tvRouteDescription = findViewById<TextView>(R.id.tvMicroRouteRouteDescription)
        tvRouteDescription.text = microRoute.routeDescription

        val tvName = findViewById<TextView>(R.id.tvMicroRouteName)
        tvName.text = microRoute.microRouteName

        val tvDate = findViewById<TextView>(R.id.tvMicroRouteDate)
        tvDate.text = DateUtil.formatDate(currentDate)

        val tvFrequency = findViewById<TextView>(R.id.tvMicroRouteFrequency)
        tvFrequency.text = DateUtil.getDayOfWeek(currentDate)

        val tvClients = findViewById<TextView>(R.id.tvMicroRouteClients)
        tvClients.text = Data.getClientsSize()

        val tvTruck = findViewById<TextView>(R.id.tvMicroRouteTruck)
        tvTruck.text = microRoute.plate

        val tvDriver = findViewById<TextView>(R.id.tvMicroRouteDriver)
        tvDriver.text = user.name

        val btnRouteAccept = findViewById<Button>(R.id.btnRouteAccept)
        btnRouteAccept.setOnClickListener {
            val intent = Intent(this, CarActivity::class.java)
            carLauncher.launch(intent)
        }

        carLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                when (result.resultCode) {
                    Activity.RESULT_OK -> {
                        val intent = Intent(this, ClientActivity::class.java)
                        startActivity(intent)
                        finish()
                    }

                    ActivityResultCodes.CHANGE_PLATE -> tvTruck.text = microRoute.plate
                }
            }
    }
}