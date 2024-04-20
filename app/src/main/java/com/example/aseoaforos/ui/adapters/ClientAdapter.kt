package com.example.aseoaforos.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.aseoaforos.R
import com.example.aseoaforos.mock.entity.Client
import com.example.aseoaforos.mock.provider.Data
import com.example.aseoaforos.util.CollectionState
import com.example.aseoaforos.mock.interfaces.OnItemClickListener

class ClientAdapter(private val clientList: List<Client>) :
    RecyclerView.Adapter<ClientAdapter.ViewHolder>() {

    private var onItemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvType: TextView = view.findViewById(R.id.tvClientCardType)
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

        holder.tvType.text = Data.getCollectTypeName(client.collectTypeId) ?: ""
        holder.tvName.text = client.companyName
        holder.tvAddress.text = client.address

        when (client.state) {
            CollectionState.COLLECTED ->
                holder.imgVCheck.setImageResource(R.drawable.vector_double_check)

            CollectionState.NOT_COLLECTED ->
                holder.imgVCheck.setImageResource(R.drawable.vector_check)

            CollectionState.UNDEFINED ->
                holder.imgVCheck.setImageResource(R.drawable.shape_border)
        }

        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(client.clientId, client.collectTypeId)
        }
    }

    override fun getItemCount() = clientList.size
}