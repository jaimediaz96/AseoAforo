package com.example.aseoaforos.ui.adapters

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.aseoaforos.R
import com.example.aseoaforos.mock.entity.Incident
import com.example.aseoaforos.mock.provider.Data
import com.example.aseoaforos.util.DateUtil
import java.io.File

class IncidentAdapter(private val incidentList: List<Incident>) :
    RecyclerView.Adapter<IncidentAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvType: TextView = view.findViewById(R.id.tvCardIncidentTypeValue)
        val tvPlace: TextView = view.findViewById(R.id.tvCardIncidentPlaceValue)
        val tvDateStart: TextView = view.findViewById(R.id.tvCardIncidentDateValue)
        val tvDateFinish: TextView = view.findViewById(R.id.tvCardIncidentTimeValue)
        val imgVPhoto: ImageView = view.findViewById(R.id.imgVCardIncident)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_incident, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val incident = incidentList[position]

        holder.tvType.text = Data.getIncidentTypeName(incident.incidentTypeId)
        holder.tvPlace.text = incident.place
        holder.tvDateStart.text = DateUtil.formatDate(incident.incidentDate)
        holder.tvDateFinish.text = DateUtil.formatTime(incident.incidentDate)

        if (incident.photos[0].isNotEmpty()) {
            val imgFile = File(incident.photos[0])
            if (imgFile.exists()) {
                val bitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
                holder.imgVPhoto.setImageBitmap(bitmap)
            }
        }
    }

    override fun getItemCount() = incidentList.size
}