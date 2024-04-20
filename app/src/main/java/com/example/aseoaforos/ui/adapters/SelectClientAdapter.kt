package com.example.aseoaforos.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.aseoaforos.R
import com.example.aseoaforos.mock.entity.Client
import com.example.aseoaforos.mock.entity.Collect
import com.example.aseoaforos.mock.provider.Data
import com.example.aseoaforos.mock.interfaces.OnItemClickListener
import com.example.aseoaforos.util.DateUtil

class SelectClientAdapter(
    private val clientList: MutableList<Client>,
    private val collectTypeId: Long
) :
    RecyclerView.Adapter<SelectClientAdapter.ViewHolder>() {

    private var onItemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvId: TextView = view.findViewById(R.id.tvClientCardType)
        val tvName: TextView = view.findViewById(R.id.tvClientCardName)
        val tvAddress: TextView = view.findViewById(R.id.tvClientCardAddress)
        val imgVCheck: ImageView = view.findViewById(R.id.imgVClientCardCheck)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_client, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val client = clientList[position]
        holder.tvId.text = client.clientId.toString()
        holder.tvName.text = client.companyName
        holder.tvAddress.text = client.address
        holder.imgVCheck.visibility = View.GONE

        holder.itemView.setOnClickListener {
            clientList.add(
                Client(
                    client.clientId,
                    client.companyName,
                    client.address,
                    collectTypeId
                )
            )

            Data.addCollect(Collect(
                collectTypeId = client.collectTypeId,
                collectDate = DateUtil.getCurrentDate(),
                microRouteId = Data.getMicroRouteId(),
                clientId = client.clientId,
            ))

            onItemClickListener?.onItemClick(client.clientId, collectTypeId)
        }
    }

    override fun getItemCount() = clientList.size
}