package com.example.aseoaforos.ui.activities

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aseoaforos.R
import com.example.aseoaforos.mock.entity.Crew
import com.example.aseoaforos.mock.provider.CallAPI
import com.example.aseoaforos.mock.provider.Data
import com.example.aseoaforos.ui.adapters.CrewAdapter

class CrewActivity : AppCompatActivity() {

    private lateinit var crewAdapter: CrewAdapter
    private val microRouteCrewList = Data.getMicroRouteCrewList()
    private val crewList: List<Crew> = Data.getCrewList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crew)

        val recyclerView = findViewById<RecyclerView>(R.id.rcCrewMemberList)
        recyclerView.layoutManager = LinearLayoutManager(this)

        crewAdapter = CrewAdapter(microRouteCrewList)

        recyclerView.adapter = crewAdapter

        val btnNew = findViewById<Button>(R.id.btnCrewNew)
        btnNew.setOnClickListener {
            showDialogNewCrew()
        }

        val btnStart = findViewById<Button>(R.id.btnCrewContinue)
        btnStart.setOnClickListener {
            if (CallAPI.addCrew(this, microRouteCrewList.map { it.crewId })) {
                val stateId = Data.getStateMicroRouteId("INICIADA")
                if (stateId != null) {
                    if (CallAPI.updateStateMicroRoute(this, Data.getMicroRouteId(), stateId)) {
                        Data.updateStateMicroRoute(stateId)

                        Data.setCollectList()

                        setResult(RESULT_OK)
                        finish()
                    }
                }
            }
        }

        val btnCancel = findViewById<Button>(R.id.btnCrewCancel)
        btnCancel.setOnClickListener {
            finish()
        }
    }

    private fun showDialogNewCrew() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_new_crew)
        dialog.setCanceledOnTouchOutside(true)

        val window = dialog.window
        if (window != null) {
            window.attributes = window.attributes.apply {
                dimAmount = 0.5f
            }
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        }

        val spinName = dialog.findViewById<Spinner>(R.id.spinDialogNewCrewName)
        val adapterSpinDialogNewCrewName = ArrayAdapter(
            this,
            R.layout.custom_spinner_items_sisze_34,
            crewList.map { it.name }
        ).apply {
            setDropDownViewResource(R.layout.custom_spinner_items_drop)
        }
        spinName.adapter = adapterSpinDialogNewCrewName

        val btnAccept = dialog.findViewById<Button>(R.id.btnDialogNewCrewAccept)
        btnAccept.setOnClickListener {
            val position = spinName.selectedItemPosition
            val crewMember = crewList[position]

            if (!microRouteCrewList.contains(crewMember)) microRouteCrewList.add(crewMember)

            crewAdapter.notifyItemInserted(microRouteCrewList.size)

            dialog.dismiss()
        }

        dialog.show()
    }
}