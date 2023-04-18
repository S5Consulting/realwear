package com.akso.realwear.app.app.equipment

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.akso.realwear.R


class EquipmentListActivity: AppCompatActivity() {
    private val equipmentList = ArrayList<EquipmentData>()
    private lateinit var equipmentItemAdapter: EquipmentItemAdapter

    //TODO fragment listactivity classes useless maybe? remove

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_equipment)

        val recyclerView : RecyclerView = findViewById(R.id.equipment_list)
        equipmentItemAdapter = EquipmentItemAdapter(equipmentList)
        val layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = equipmentItemAdapter



    }
}