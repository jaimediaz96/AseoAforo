package com.example.aseoaforos.ui.adapters

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.aseoaforos.R
import java.io.File

class PhotoAdapter(private val photoList: MutableList<String>) :
    RecyclerView.Adapter<PhotoAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgVPhoto: ImageView = view.findViewById(R.id.imgVCardPhotoPhoto)
        val imgBtnDelete: ImageButton = view.findViewById(R.id.imgBtnCardPhotoDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_photo, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val path = photoList[position]

        val imgFile = File(path)
        if (imgFile.exists()) {
            val bitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
            holder.imgVPhoto.setImageBitmap(bitmap)
        }

        holder.imgBtnDelete.setOnClickListener {
            photoList.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, photoList.size)
        }
    }

    override fun getItemCount() = photoList.size
}