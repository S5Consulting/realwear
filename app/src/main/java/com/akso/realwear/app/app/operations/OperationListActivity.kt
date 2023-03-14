package com.akso.realwear.app.app.operations

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.akso.realwear.R

class OperationListActivity: AppCompatActivity() {
    private val operationList = ArrayList<Operation>()
    private lateinit var operationItemAdapter: OperationItemAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_operations)

        val recyclerView : RecyclerView = findViewById(R.id.operations_list)
        operationItemAdapter = OperationItemAdapter(operationList)
        val layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = operationItemAdapter
        recyclerView.smoothScrollToPosition(0)

    }


}