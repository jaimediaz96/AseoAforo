package com.example.aseoaforos.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.example.aseoaforos.R
import com.example.aseoaforos.mock.entity.FillPercentage
import com.example.aseoaforos.mock.entity.Recipient
import com.example.aseoaforos.mock.entity.RecipientType
import com.example.aseoaforos.mock.provider.Data
import com.example.aseoaforos.util.CollectTypeUtil
import java.math.BigDecimal
import java.math.RoundingMode

class CollectAdapter(
    private val recipientTypeList: List<RecipientType>,
    private val recipientList: MutableList<Recipient>,
    private val onItemUpdate: () -> Unit
) :
    RecyclerView.Adapter<CollectAdapter.ViewHolder>() {

    private val fillPercentageList: List<FillPercentage> = Data.getFillPercentageList()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvRecipientCardName)
        val etQuantity: EditText = view.findViewById(R.id.etRecipientCardQuantity)
        val spinPercent: Spinner = view.findViewById(R.id.spinRecipientCardPercent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_recipient, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val type = recipientTypeList[position]

        holder.tvName.text = type.name

        holder.etQuantity.setText("0")

        val adapterSpinPercent = ArrayAdapter(
            holder.itemView.context,
            R.layout.custom_spinner_items,
            fillPercentageList.map { "${it.percent * 100}%" }
        ).apply {
            setDropDownViewResource(R.layout.custom_spinner_items_drop)
        }
        holder.spinPercent.adapter = adapterSpinPercent

        if (type.collectTypeId == Data.getCollectTypeId(CollectTypeUtil.VOLUMEN.toString())) {
            holder.spinPercent.visibility = View.VISIBLE
        }

        val recipient = recipientList.find { it.recipientTypeId == type.recipientTypeId }

        if (recipient != null) {
            holder.etQuantity.setText(recipient.quantity.toString())

            val percentPosition = when (Data.getFillPercentagePercent(recipient.fillPercentId)) {
                0.75 -> 1
                0.5 -> 2
                0.25 -> 3
                else -> 0
            }
            holder.spinPercent.setSelection(percentPosition)
        }

        holder.etQuantity.addTextChangedListener {
            val recipientData = recipientList.find { it.recipientTypeId == type.recipientTypeId }
            val percent = getPercent(holder.spinPercent.selectedItem.toString())

            val text = holder.etQuantity.text.toString()
            val quantity = if (text.isNotEmpty() && text.toDoubleOrNull() != null) {
                if (text.toDouble() <= 0) 0.0
                else text.toDouble()
            } else {
                0.0
            }

            val total = calculateTotal(quantity, type.recipientValue, percent)

            updateRecipientList(
                recipientData,
                type.recipientTypeId,
                quantity,
                Data.getFillPercentageId(percent),
                total
            )
            onItemUpdate()
        }

        holder.spinPercent.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val data = recipientList.find { it.recipientTypeId == type.recipientTypeId }
                val quantity = holder.etQuantity.text.toString().toDouble()

                val textPercent = parent?.getItemAtPosition(position).toString()
                val percent = getPercent(textPercent)

                val total = calculateTotal(quantity, type.recipientValue, percent)

                updateRecipientList(
                    data,
                    type.recipientTypeId,
                    quantity,
                    Data.getFillPercentageId(percent),
                    total
                )
                onItemUpdate()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

    }

    override fun getItemCount() = recipientTypeList.size

    private fun getPercent(text: String): Double {
        return when (text) {
            "75%" -> 0.75
            "50%" -> 0.5
            "25%" -> 0.25
            else -> 1.0
        }
    }

    private fun updateRecipientList(
        recipient: Recipient?,
        recipientTypeId: Long,
        quantity: Double,
        fillPercentId: Long,
        total: Double
    ) {
        if (recipient != null && quantity == 0.0) {
            recipientList.remove(recipient)
        } else if (recipient != null && quantity > 0) {
            recipient.quantity = quantity
            recipient.fillPercentId = fillPercentId
            recipient.total = total
        } else if (recipient == null && quantity > 0) {
            recipientList.add(
                Recipient(
                    quantity,
                    total,
                    fillPercentId,
                    recipientTypeId,
                )
            )
        }
    }

    private fun calculateTotal(quantity: Double, value: Double, percent: Double): Double {
        val result = BigDecimal(quantity) * BigDecimal(value) * BigDecimal(percent)
        return result.setScale(4, RoundingMode.HALF_UP).toDouble()
    }
}
