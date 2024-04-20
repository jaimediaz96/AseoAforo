package com.example.aseoaforos.ui.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aseoaforos.R
import com.example.aseoaforos.mock.provider.Data
import com.example.aseoaforos.ui.adapters.SelectClientAdapter
import com.example.aseoaforos.mock.interfaces.OnItemClickListener

class SelectClientActivity : AppCompatActivity(), OnItemClickListener {

    private lateinit var newClientLauncher: ActivityResultLauncher<Intent>
    private lateinit var selectClientAdapter: SelectClientAdapter
    private var clientList = Data.getClientList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_client)

        val collectTypeId = intent.getLongExtra("collectTypeId", 0L)

        val etSearch = findViewById<EditText>(R.id.etSelectClientSearch)
        etSearch.addTextChangedListener {
            searchClient(etSearch.text.toString())
        }

        val recyclerView = findViewById<RecyclerView>(R.id.rcSelectClientList)
        recyclerView.layoutManager = LinearLayoutManager(this)

        selectClientAdapter = SelectClientAdapter(clientList, collectTypeId)

        recyclerView.adapter = selectClientAdapter
        selectClientAdapter.setOnItemClickListener(this)

        val btnNewClient = findViewById<Button>(R.id.btnSelectClientNewClient)
        btnNewClient.setOnClickListener {
            val intent = Intent(this, NewClientActivity::class.java).apply {
                putExtra("collectTypeId", collectTypeId)
            }
            newClientLauncher.launch(intent)
        }

        val btnCancel = findViewById<Button>(R.id.btnSelectClientCancel)
        btnCancel.setOnClickListener {
            finish()
        }

        newClientLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    setResult(RESULT_OK)
                    finish()
                }
            }
    }

    override fun onItemClick(clientId: Long, collectTypeId: Long) {
        setResult(RESULT_OK)
        finish()
    }

    private fun searchClient(search: String) {
        Log.i("help", "searchClient: Not yet implemented")
        Log.i("help", "search: $search")
    }
}