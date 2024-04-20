package com.example.aseoaforos.ui.activities

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.aseoaforos.R
import com.example.aseoaforos.mock.provider.CallAPI
import com.example.aseoaforos.mock.provider.Data
import com.example.aseoaforos.util.ActivityResultCodes

class ReturnHomeActivity : AppCompatActivity() {

    private lateinit var refuelLauncher: ActivityResultLauncher<Intent>
    private lateinit var incidentLauncher: ActivityResultLauncher<Intent>
    private var cutTime: Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_return_home)

        val stateId = Data.getStateMicroRouteId("REGRESANDO A BASE")
        if (stateId != null) {
            if (CallAPI.updateStateMicroRoute(this, Data.getMicroRouteId(), stateId)) {
                Data.updateStateMicroRoute(stateId)
            }
        }

        val startTime = System.currentTimeMillis()

        val btnAccept = findViewById<Button>(R.id.btnReturnHomeAccept)
        btnAccept.setOnClickListener {
            val endTime = System.currentTimeMillis()
            val elapsedTime = endTime - startTime
            val time = elapsedTime / 1000

            Data.updateMicroRouteTripTime(time)

            val idFinish = Data.getStateMicroRouteId("FINALIZADA")
            if (idFinish != null) {
                if (CallAPI.updateStateMicroRoute(this, Data.getMicroRouteId(), idFinish)) {
                    Data.updateStateMicroRoute(idFinish)
                }
            }

            val intent = Intent(this, FinishRouteActivity::class.java)
            startActivity(intent)
            finish()
        }

        val btnIncident = findViewById<ImageButton>(R.id.imgBtnReturnHomeIncident)
        btnIncident.setOnClickListener {
            showDialogIncident()
        }

        refuelLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data

                    val time = data?.getLongExtra("time", 0L) ?: 0L

                    cutTime += time
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
                        val intent = Intent(this, FinishRouteActivity::class.java)
                        startActivity(intent)
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
}