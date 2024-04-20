package com.example.aseoaforos.ui.adapters

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.aseoaforos.R
import com.example.aseoaforos.mock.entity.Refuel
import com.example.aseoaforos.util.DateUtil
import java.io.File

class RefuelAdapter(private val context: Context, private val refuelList: List<Refuel>) :
    RecyclerView.Adapter<RefuelAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvPlace: TextView = view.findViewById(R.id.tvCardRefuelPlaceValue)
        val tvQuantity: TextView = view.findViewById(R.id.tvCardRefuelQuantityValue)
        val tvCost: TextView = view.findViewById(R.id.tvCardRefuelCostValue)
        val tvDate: TextView = view.findViewById(R.id.tvCardRefuelDateValue)
        val tvTime: TextView = view.findViewById(R.id.tvCardRefuelTimeValue)
        val imgVPhoto: ImageView = view.findViewById(R.id.imgVCardRefuel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_refuel, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val refuel = refuelList[position]

        holder.tvPlace.text = refuel.place

        val textQuantity = "${refuel.quantity} ${context.getString(R.string.gallons)}"
        holder.tvQuantity.text = textQuantity

        val textCost = "$ ${refuel.cost}"
        holder.tvCost.text = textCost

        val date = DateUtil.formatDate(refuel.refuelDate)
        holder.tvDate.text = date

        val time = DateUtil.formatTime(refuel.refuelDate)
        holder.tvTime.text = time

        if (refuel.photos[0].isNotEmpty()) {
            val imgFile = File(refuel.photos[0])
            if (imgFile.exists()) {
                val bitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
                holder.imgVPhoto.setImageBitmap(bitmap)
            }
        }
    }

    override fun getItemCount() = refuelList.size
}