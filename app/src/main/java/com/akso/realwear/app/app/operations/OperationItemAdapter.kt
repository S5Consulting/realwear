package com.akso.realwear.app.app.operations


import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.akso.realwear.R
import com.akso.realwear.app.app.operationDetails.OperationDetailsActivity


class OperationItemAdapter(private val dataSet: ArrayList<Operation>)
    : RecyclerView.Adapter<OperationItemAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val title: TextView = view.findViewById(R.id.operation_title)
        val operationNumber : TextView = view.findViewById(R.id.operation_number)
        val operationConfirm : TextView = view.findViewById(R.id.operation_confirmed)
        val orderNumber : TextView = view.findViewById(R.id.order_number)
        val orderRoutingNumber : TextView = view.findViewById(R.id.order_routing_number)
        val operationCount : TextView = view.findViewById(R.id.operation_count)
        val nextOperation : TextView = view.findViewById(R.id.next_operation)
        var workCenter: String = ""
        var index = 0
        var finalConfirm = false
        var planningPlant = ""
        init {
            itemView.setOnClickListener { v ->
                val context: Context = v.context
                val intent = Intent(context, OperationDetailsActivity::class.java)

                intent.putExtra("Title", title.text)
                intent.putExtra("OrderNum", orderNumber.text)
                intent.putExtra("OrderRoutingNum", orderRoutingNumber.text)
                intent.putExtra("OperationNum", operationNumber.text)
                intent.putExtra("OperationCount", operationCount.text)
                intent.putExtra("NextOperation", nextOperation.text)
                intent.putExtra("OperationIndex", index)
                intent.putExtra("FinalConfirmed", finalConfirm)
                intent.putExtra("WorkCenter", workCenter)
                intent.putExtra("PlanningPlant", planningPlant)
                context.startActivity(intent)
            }

        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.operation_listitem, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        val currentItem = dataSet[position]
        holder.title.text = currentItem.title
        holder.operationNumber.text = currentItem.operationNumber
        holder.orderNumber.text = currentItem.operationOrderNumber
        holder.operationCount.text = currentItem.operationCount
        holder.orderRoutingNumber.text = currentItem.operationRoutingNumber
        holder.nextOperation.text = currentItem.nextOperation
        holder.index = position
        holder.operationConfirm.text = currentItem.operationConfirm
        holder.finalConfirm = currentItem.finalConfirmed
        holder.workCenter = currentItem.workCenter
        holder.planningPlant = currentItem.planningPlant

    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

    }

