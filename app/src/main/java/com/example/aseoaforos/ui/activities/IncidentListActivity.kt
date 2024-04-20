package com.example.aseoaforos.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aseoaforos.R
import com.example.aseoaforos.mock.provider.Data
import com.example.aseoaforos.ui.adapters.IncidentAdapter

class IncidentListActivity : AppCompatActivity() {

    private lateinit var incidentAdapter: IncidentAdapter
    private val incidentList = Data.getIncidentList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_incident_list)

        val recyclerView = findViewById<RecyclerView>(R.id.rvIncidentList)
        recyclerView.layoutManager = LinearLayoutManager(this)

        incidentAdapter = IncidentAdapter(incidentList)

        recyclerView.adapter = incidentAdapter

        val bntEdit = findViewById<Button>(R.id.bntIncidentListEdit)
        bntEdit.setOnClickListener {
            finish()
        }

        val bntFinish = findViewById<Button>(R.id.bntIncidentListFinish)
        bntFinish.setOnClickListener {
            setResult(RESULT_OK)
            finish()
        }
    }
}