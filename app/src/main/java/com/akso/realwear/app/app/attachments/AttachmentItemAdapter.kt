package com.akso.realwear.app.app.attachments

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.akso.realwear.R
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AttachmentItemAdapter(private val dataSet: ArrayList<AttachmentData>)
    : RecyclerView.Adapter<AttachmentItemAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.attachment_name)
        val createdBy : TextView = view.findViewById(R.id.attachment_created_by)
        val createdOn : TextView = view.findViewById(R.id.attachment_created_on)
        var url = ""

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.attachment_listitem, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = dataSet[position]
        val parsedDate = LocalDateTime.parse(currentItem.createdOn, DateTimeFormatter.ISO_DATE_TIME)
        val formattedDate = parsedDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))

        holder.title.text = currentItem.title
        holder.createdBy.text = "Created By: " + currentItem.createdBy
        holder.createdOn.text = "Created Date: $formattedDate"
        holder.url = currentItem.url
        holder.title.setOnClickListener {
            val context = holder.itemView.context
            Log.i("click", currentItem.title)
            //Glide.with(context).load(currentItem.url).into(holder.imageView)

        }
    }

    override fun getItemCount() = dataSet.size

}