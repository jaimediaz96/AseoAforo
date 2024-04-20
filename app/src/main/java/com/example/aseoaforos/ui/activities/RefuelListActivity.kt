package com.example.aseoaforos.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aseoaforos.R
import com.example.aseoaforos.mock.provider.Data
import com.example.aseoaforos.ui.adapters.RefuelAdapter

class RefuelListActivity : AppCompatActivity() {

    private lateinit var refuelAdapter: RefuelAdapter
    private val refuelList = Data.getRefuelList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_refuel_list)

        val recyclerView = findViewById<RecyclerView>(R.id.rvRefuelList)
        recyclerView.layoutManager = LinearLayoutManager(this)

        refuelAdapter = RefuelAdapter(this, refuelList)

        recyclerView.adapter = refuelAdapter

        val bntEdit = findViewById<Button>(R.id.bntRefuelListEdit)
        bntEdit.setOnClickListener {
            finish()
        }

        val bntFinish = findViewById<Button>(R.id.bntRefuelListFinish)
        bntFinish.setOnClickListener {
            setResult(RESULT_OK)
            finish()
        }
    }
}