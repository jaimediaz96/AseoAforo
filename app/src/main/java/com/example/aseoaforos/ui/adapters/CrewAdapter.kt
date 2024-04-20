package com.example.aseoaforos.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.aseoaforos.R
import com.example.aseoaforos.mock.entity.Crew

class CrewAdapter(private val crewList: MutableList<Crew>) :
    RecyclerView.Adapter<CrewAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvCewMemberName)
        val imgBtnDelete: ImageButton = view.findViewById(R.id.imgBtnCrewMemberDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_crew_member, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val crew = crewList[position]
        holder.tvName.text = crew.name
        holder.imgBtnDelete.setOnClickListener {
            crewList.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, crewList.size)
        }
    }

    override fun getItemCount() = crewList.size
}