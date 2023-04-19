package com.akso.realwear.app.app.workOrders

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.akso.realwear.R
import com.akso.realwear.app.app.CustomProgressBar
import com.akso.realwear.app.app.orderDetails.WorkOrderDetailsActivity
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class WorkOrderItemAdapter(private val dataSet: ArrayList<WorkOrder>) :
    RecyclerView.Adapter<WorkOrderItemAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */

    class ViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {
        val title : TextView = view.findViewById(R.id.work_order_title)
        val priority : TextView = view.findViewById(R.id.work_order_priority)
        val startDate : TextView = view.findViewById(R.id.work_order_start_date)
        val mainWorkCenter : TextView = view.findViewById(R.id.work_order_work_center)
        val orderId : TextView = view.findViewById(R.id.order_id)
        val type : TextView = view.findViewById(R.id.order_type)
        val inspection : TextView = view.findViewById(R.id.order_inspection)
        val progressBar : CustomProgressBar = view.findViewById(R.id.order_progress)

        init {
            itemView.setOnClickListener { v ->
                val context: Context = v.context
                val intent = Intent(context, WorkOrderDetailsActivity::class.java)

                //Add work order id for WorkOrderDetails query
                intent.putExtra("OrderID", orderId.text)

                context.startActivity(intent)
            }
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.work_order_item, viewGroup, false)

        return ViewHolder(view)
    }



    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        val currentItem = dataSet[position]
        val parsedDate = LocalDateTime.parse(currentItem.startDate, DateTimeFormatter.ISO_DATE_TIME)
        val formattedDate = parsedDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
        val context =  viewHolder.title.context
        val start = context.getString(R.string.work_orders_start)
        val priority = context.getString(R.string.work_orders_priority)
        val workCenter = context.getString(R.string.work_orders_main_work_center)

        viewHolder.title.text = currentItem.title
        viewHolder.priority.text = priority + currentItem.priority
        viewHolder.startDate.text =  start + formattedDate
        viewHolder.mainWorkCenter.text = workCenter + currentItem.workcenter
        viewHolder.orderId.text = currentItem.orderId
        viewHolder.type.text = currentItem.orderType

        if(currentItem.inspection == "true") {
            viewHolder.inspection.text = currentItem.inspection
        }
        viewHolder.progressBar.progress = currentItem.percentComplete.toInt()
        viewHolder.progressBar.progressText = currentItem.percentComplete + "%"


    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}
