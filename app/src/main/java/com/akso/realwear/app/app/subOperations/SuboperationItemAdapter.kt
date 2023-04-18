package com.akso.realwear.app.app.subOperations

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.RecyclerView
import com.akso.realwear.R
import kotlinx.android.synthetic.main.suboperation_item.view.*

class SuboperationItemAdapter(private val dataSet: ArrayList<SubOperationData>) :
    RecyclerView.Adapter<SuboperationItemAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */

    class ViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {

        val checkbox : CheckBox = view.findViewById(R.id.suboperation_checkbox)
        val title : TextView = view.findViewById(R.id.suboperation_title)
        val description : TextView = view.findViewById(R.id.suboperation_description)

    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.suboperation_item, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        val currentItem = dataSet[position]
        var isAllChecked = false
        viewHolder.title.text = currentItem.title
        viewHolder.description.text = currentItem.description

        if(currentItem.isCompleted) {
            viewHolder.checkbox.setChecked(true)
        }

        viewHolder.checkbox.setOnClickListener {
            if(it.suboperation_checkbox.isChecked) {
                currentItem.isCompleted = true
                for(data in dataSet) {
                    isAllChecked = dataSet.all { data.isCompleted}
                }
                val intent = Intent("lastChecked")
                intent.putExtra("lastChecked", currentItem.title)
                intent.putExtra("isAllChecked", isAllChecked)
                LocalBroadcastManager.getInstance(viewHolder.itemView.context).sendBroadcast(intent);
            } else {
                currentItem.isCompleted = false
                for(data in dataSet) {
                    isAllChecked = dataSet.all { data.isCompleted}
                }
            }
        }


    }


    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}
