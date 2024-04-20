package com.example.aseoaforos.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.example.aseoaforos.R
import com.example.aseoaforos.mock.entity.Toll

class TollAdapter(private val tollList: MutableList<Toll>) :
    RecyclerView.Adapter<TollAdapter.ViewHolder>() {

    private var isEditingEnabled = true

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val etToll: EditText = view.findViewById(R.id.etToll)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_toll, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val toll = tollList[position]
        holder.etToll.setText(toll.name)
        holder.etToll.isEnabled = isEditingEnabled

        holder.etToll.addTextChangedListener {
            val text = it.toString()

            if (text.isNotEmpty()) {
                tollList[position].name = text
            }
        }
    }

    override fun getItemCount() = tollList.size

    fun setEditingEnabled(enabled: Boolean) {
        isEditingEnabled = enabled
        notifyDataSetChanged()
    }
}