package com.example.aseoaforos.ui.activities

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.Chronometer
import android.widget.EditText
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aseoaforos.R
import com.example.aseoaforos.mock.provider.Data
import com.example.aseoaforos.ui.adapters.ClientAdapter
import com.example.aseoaforos.util.ActivityResultCodes
import com.example.aseoaforos.util.CollectionState
import com.example.aseoaforos.util.DateUtil
import com.example.aseoaforos.mock.interfaces.OnItemClickListener
import com.example.aseoaforos.mock.provider.CallAPI
import com.example.aseoaforos.util.CollectTypeUtil
import java.math.BigDecimal
import java.math.RoundingMode

class ClientActivity : AppCompatActivity(), OnItemClickListener {

    private lateinit var clientAdapter: ClientAdapter
    private lateinit var collectLauncher: ActivityResultLauncher<Intent>
    private lateinit var selectClientLauncher: ActivityResultLauncher<Intent>
    private lateinit var refuelLauncher: ActivityResultLauncher<Intent>
    private lateinit var incidentLauncher: ActivityResultLauncher<Intent>
    private lateinit var provisionLauncher: ActivityResultLauncher<Intent>
    private val clientList = Data.getClientList()
    private val totalCapacity = Data.getTruckCapacity()
    private var costumerCollected = 0
    private var currentCapacity = 0.0
    private var cutProvision: Double = 0.0
    private lateinit var chronometer: Chronometer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client)

        val currentDate = DateUtil.getCurrentDate()
        val microRoute = Data.getMicroRoute()

        val tvTitle = findViewById<TextView>(R.id.tvClientTitle)
        val textTitle = "${getString(R.string.microRoute)} ${microRoute.microRouteName}"
        tvTitle.text = textTitle

        val tvRoute = findViewById<TextView>(R.id.tvClientRouteValue)
        tvRoute.text = microRoute.routeName

        val tvCMunicipality = findViewById<TextView>(R.id.tvClientMunicipalityValue)
        tvCMunicipality.text = microRoute.routeDescription

        chronometer = findViewById(R.id.tvClientMicroRouteTime)
        chronometer.base = SystemClock.elapsedRealtime()
        chronometer.start()

        val tvCapacity = findViewById<TextView>(R.id.tvClientCapacityValue)
        calculatePercent(tvCapacity)

        val tvDate = findViewById<TextView>(R.id.tvClientDateValue)
        tvDate.text = DateUtil.formatDate(currentDate)

        val tvProgress = findViewById<TextView>(R.id.tvClientProgressValue)
        displayProgress(tvProgress)

        val etSearch = findViewById<EditText>(R.id.etClientSearch)
        etSearch.addTextChangedListener {
            searchClient(etSearch.text.toString())
        }

        val recyclerView = findViewById<RecyclerView>(R.id.rcClientList)
        recyclerView.layoutManager = LinearLayoutManager(this)

        clientAdapter = ClientAdapter(clientList)
        clientAdapter.setOnItemClickListener(this)

        recyclerView.adapter = clientAdapter

        val btnNewAforo = findViewById<Button>(R.id.btnClientNewAforo)
        btnNewAforo.setOnClickListener {
            showDialogNewAforo()
        }

        val btnPauseAforo = findViewById<Button>(R.id.btnClientPauseAforo)
        btnPauseAforo.setOnClickListener {
            showDialogPauseRoute()
        }

        val btnFinishRoute = findViewById<Button>(R.id.btnClientFinishRoute)
        btnFinishRoute.setOnClickListener {
            if (costumerCollected < clientList.size) showDialogFinishRoute()
            else showDialogDispose()
        }

        collectLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data

                    val clientId = data?.getLongExtra("clientId", 0L) ?: 0L
                    val collectTypeId = data?.getLongExtra("collectType", 0L) ?: 0L

                    calculatePercent(tvCapacity)
                    displayProgress(tvProgress)

                    val position = clientList.indexOfFirst {
                        it.clientId == clientId && it.collectTypeId == collectTypeId
                    }
                    clientAdapter.notifyItemChanged(position)
                }
            }

        selectClientLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val position = clientList.lastIndex

                    clientAdapter.notifyItemInserted(position)

                    displayProgress(tvProgress)

                    val client = clientList[position]
                    onItemClick(client.clientId, client.collectTypeId)
                }
            }

        refuelLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                when (result.resultCode) {
                    Activity.RESULT_OK -> {
                        val data: Intent? = result.data

                        val time = data?.getLongExtra("time", 0L) ?: 0L
                        Log.i("help", "$time")
                        // cutTime(chronometer, time)

                        displayProgress(tvProgress)
                    }

                    ActivityResultCodes.FINISH_ROUTE -> {
                        sendToFinishRoute()
                    }
                }
            }

        incidentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                when (result.resultCode) {
                    Activity.RESULT_OK -> {
                        val data: Intent? = result.data

                        val time = data?.getLongExtra("time", 0L) ?: 0L

                        cutTime(chronometer, time)

                        displayProgress(tvProgress)
                    }

                    ActivityResultCodes.FINISH_ROUTE -> {
                        sendToFinishRoute()
                    }
                }
            }

        provisionLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                when (result.resultCode) {
                    Activity.RESULT_OK -> {
                        val data: Intent? = result.data

                        val time = data?.getLongExtra("time", 0L) ?: 0L

                        cutTime(chronometer, time)

                        displayProgress(tvProgress)
                    }

                    ActivityResultCodes.FINISH_ROUTE -> {
                        showDialogReturnHome()
                    }
                }
            }
    }

    override fun onItemClick(clientId: Long, collectTypeId: Long) {
        val intent = Intent(this, CollectActivity::class.java).apply {
            putExtra("clientId", clientId)
            putExtra("collectTypeId", collectTypeId)
        }
        collectLauncher.launch(intent)
    }

    private fun showDialogNewAforo() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_new_collect)
        dialog.setCanceledOnTouchOutside(true)

        val window = dialog.window
        if (window != null) {
            window.attributes = window.attributes.apply {
                dimAmount = 0.5f
            }
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        }

        val btnVolume = dialog.findViewById<Button>(R.id.btnDialogNewCollectVolume)
        btnVolume.setOnClickListener {
            dialog.dismiss()
            sendAforoToSelectClient(Data.getCollectTypeId(CollectTypeUtil.VOLUMEN.toString())!!)
        }

        val btnWeight = dialog.findViewById<Button>(R.id.btnDialogNewCollectWeight)
        btnWeight.setOnClickListener {
            dialog.dismiss()
            sendAforoToSelectClient(Data.getCollectTypeId(CollectTypeUtil.PESO.toString())!!)
        }

        val btnEspecial = dialog.findViewById<Button>(R.id.btnDialogNewCollectEspecial)
        btnEspecial.setOnClickListener {
            dialog.dismiss()
            sendAforoToSelectClient(Data.getCollectTypeId(CollectTypeUtil.ESPECIAL.toString())!!)
        }

        dialog.show()
    }

    private fun sendAforoToSelectClient(collectTypeId: Long) {
        val intent = Intent(this, SelectClientActivity::class.java).apply {
            putExtra("collectTypeId", collectTypeId)
        }
        selectClientLauncher.launch(intent)
    }

    private fun showDialogPauseRoute() {
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
        btnProvision.setOnClickListener {
            dialog.dismiss()
            val intent = Intent(this, ProvisionActivity::class.java)
            provisionLauncher.launch(intent)
        }

        dialog.show()
    }

    private fun showDialogFinishRoute() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_finish_with_pending)
        dialog.setCanceledOnTouchOutside(true)

        val window = dialog.window
        if (window != null) {
            window.attributes = window.attributes.apply {
                dimAmount = 0.5f
            }
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        }

        val tvSubTitle = dialog.findViewById<TextView>(R.id.tvDialogFinishWithPendingSubTitle)
        tvSubTitle.text = getString(R.string.questionMissingClients)

        val btnYes = dialog.findViewById<Button>(R.id.btnDialogFinishWithPendingYes)

        val btnNo = dialog.findViewById<Button>(R.id.btnDialogFinishWithPendingNo)

        val etDescription = dialog.findViewById<EditText>(R.id.etDialogFinishWithPendingDescription)

        val btnAccept = dialog.findViewById<Button>(R.id.btnDialogFinishWithPendingAccept)

        btnYes.setOnClickListener {
            tvSubTitle.text = getString(R.string.descriptionWhyAbandon)
            etDescription.visibility = View.VISIBLE
            btnAccept.visibility = View.VISIBLE
            btnYes.visibility = View.GONE
            btnNo.visibility = View.GONE
        }

        btnNo.setOnClickListener {
            dialog.dismiss()
        }

        btnAccept.setOnClickListener {
            Data.updateDescriptionAbandon(etDescription.text.toString())

            dialog.dismiss()
            showDialogDispose()
        }

        dialog.show()
    }

    private fun showDialogDispose() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_provision)
        dialog.setCanceledOnTouchOutside(true)

        val window = dialog.window
        if (window != null) {
            window.attributes = window.attributes.apply {
                dimAmount = 0.5f
            }
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        }

        val btnYes = dialog.findViewById<Button>(R.id.btnDialogProvisionYes)
        btnYes.setOnClickListener {
            dialog.dismiss()
            val intent = Intent(this, ProvisionActivity::class.java).apply {
                putExtra("isFinish", true)
            }
            provisionLauncher.launch(intent)
        }

        val btnNo = dialog.findViewById<Button>(R.id.btnDialogProvisionNo)
        btnNo.setOnClickListener {
            dialog.dismiss()
            showDialogReturnHome()
        }

        dialog.show()
    }

    private fun showDialogReturnHome() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_return)

        val window = dialog.window
        if (window != null) {
            window.attributes = window.attributes.apply {
                dimAmount = 0.5f
            }
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        }

        val btnYes = dialog.findViewById<Button>(R.id.btnDialogReturnYes)
        btnYes.setOnClickListener {
            dialog.dismiss()
            sendToReturnHome()
        }

        val btnNo = dialog.findViewById<Button>(R.id.btnDialogReturnNo)
        btnNo.setOnClickListener {
            dialog.dismiss()
            sendToFinishRoute()
        }

        dialog.show()
    }

    private fun sendToReturnHome() {
        updateMicroRoute()

        val intent = Intent(this, ReturnHomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun sendToFinishRoute() {
        updateStateFinish()
        updateMicroRoute()

        val intent = Intent(this, FinishRouteActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun calculatePercent(tvCapacity: TextView) {
        currentCapacity = Data.getCollectList().sumOf { it.totalCollected }
        currentCapacity -= cutProvision

        val percent = BigDecimal((currentCapacity * 100) / totalCapacity)
        val rounded = percent.setScale(4, RoundingMode.HALF_UP)
        val textPercent = "$rounded %"
        tvCapacity.text = textPercent
    }

    private fun displayProgress(progress: TextView) {
        costumerCollected = clientList.count {
            it.state != CollectionState.UNDEFINED
        }
        val textProgress = "$costumerCollected ${getString(R.string.collected)} ${clientList.size}"
        progress.text = textProgress

        updateState(costumerCollected, clientList.size)
    }

    private fun updateState(current: Int, total: Int) {
        val percent = (current.toDouble() / total.toDouble()) * 100

        val stateId= when {
            percent >= 75 -> Data.getStateMicroRouteId("75% completado")
            percent >= 50 -> Data.getStateMicroRouteId("50% completado")
            percent >= 25 -> Data.getStateMicroRouteId("25% completado")
            else -> null
        }
        if (stateId != null) {
            if (CallAPI.updateStateMicroRoute(this, Data.getMicroRouteId(), stateId)) {
                Data.updateStateMicroRoute(stateId)
            }
        }
    }

    private fun updateStateFinish() {
        val stateId = Data.getStateMicroRouteId("FINALIZADA CON PENDIENTES")
        if (stateId != null) {
            if (CallAPI.updateStateMicroRoute(this, Data.getMicroRouteId(), stateId)) {
                Data.updateStateMicroRoute(stateId)
            }
        }
    }

    private fun cutTime(chronometer: Chronometer, cutTime: Long) {
        val actualTimeMilliseconds = chronometer.base - SystemClock.elapsedRealtime()

        val actualTime = actualTimeMilliseconds / 1000

        val minusTime = actualTime - cutTime

        val minusTimeMillisecond = minusTime * 1000

        chronometer.base = SystemClock.elapsedRealtime() - minusTimeMillisecond
    }

    private fun updateMicroRoute() {
        val timeMilli = chronometer.base - SystemClock.elapsedRealtime()

        val time = timeMilli / 1000

        Data.updateMicroRouteCollectTime(time)

        val collectList = Data.getCollectList()

        val totalCollect = collectList.filter {
            it.collectTypeId != Data.getCollectTypeId(CollectTypeUtil.PESO.toString())
        }.sumOf { it.totalCollected }

        Data.updateMicroRouteTotalCollect(totalCollect)

        val weight = Data.getProvisionList().sumOf { it.weight }

        val density = totalCollect / weight

        Data.updateMicroRouteDensity(density)
    }

    @Deprecated("")
    override fun onBackPressed() {
        if (costumerCollected < clientList.size) showDialogFinishRoute()
        else showDialogDispose()
    }

    private fun searchClient(search: String) {
        Log.i("help", "searchClient: Not yet implemented")
        Log.i("help", "search: $search")
    }
}
