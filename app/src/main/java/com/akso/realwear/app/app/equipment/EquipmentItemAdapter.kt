package com.akso.realwear.app.app.equipment

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.akso.realwear.R


class EquipmentItemAdapter(private val dataSet: ArrayList<EquipmentData>)
    : RecyclerView.Adapter<EquipmentItemAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.equipment_title)
        val material : TextView = view.findViewById(R.id.equipment_material)
        val serialNo : TextView = view.findViewById(R.id.equipment_serial_no)
        val funcLoc : TextView = view.findViewById(R.id.equipment_func_loc)
        val equipmentNo : TextView = view.findViewById(R.id.equipment_no)

        val btnExpand = view.findViewById<Button>(R.id.btn_expand_equipment_info)
        val equipmentInfo = view.findViewById<LinearLayout>(R.id.equipment_information)
        val link = view.findViewById<TextView>(R.id.link)


        init {
            btnExpand.setOnClickListener {
                equipmentInfo.isVisible = !equipmentInfo.isVisible
                if(btnExpand.rotation == 0F) {
                    btnExpand.rotation= 90F
                } else {
                    btnExpand.rotation= 0F
                }

            }
            //TODO(URI to equipment details? based on equipment id)
            link.setOnClickListener { v ->
                val context = v.context
                context.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        //TODO find equipment detail links
                        Uri.parse("https://www.google.com")
                    )
                )
            }

        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.equipment_listitem, viewGroup, false)

        return ViewHolder(view)
    }
    //TODO bind these
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = dataSet[position]
        holder.title.text = currentItem.title
        holder.equipmentNo.text = currentItem.equipment
        holder.material.text = currentItem.materialNo + "(" + currentItem.materialDesc + ")"
        holder.serialNo.text = currentItem.serialNo
        holder.funcLoc.text = currentItem.funcLocation + currentItem.storageLocation
    //TODO find link to equipment page?
    //holder.link.text = currentItem.link
    }

    override fun getItemCount() = dataSet.size

}