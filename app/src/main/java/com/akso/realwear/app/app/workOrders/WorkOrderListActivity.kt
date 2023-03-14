package com.akso.realwear.app.app.workOrders

import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.akso.realwear.R
import com.akso.realwear.app.viewmodel.zeamcworkorderlistparameters.ZEAMCWORKORDERLISTParametersViewModel
import com.akso.realwear.databinding.WorkOrdersListBinding
import com.sap.cloud.android.odata.zfiori_eam_app_srv_entities.ZEAMCWORKORDERLISTParameters
import com.sap.cloud.android.odata.zfiori_eam_app_srv_entities.ZFIORI_EAM_APP_SRV_Entities
import com.sap.cloud.android.odata.zfiori_eam_app_srv_entities.ZFIORI_EAM_APP_SRV_Entities.workOrderListSet
import com.sap.cloud.mobile.odata.*


class WorkOrderListActivity: AppCompatActivity() {
    private val workOrderList = ArrayList<WorkOrder>()
    private lateinit var workOrderItemAdapter: WorkOrderItemAdapter
    private lateinit var dataService : ZFIORI_EAM_APP_SRV_Entities
    private lateinit var query : DataQuery
    val provider = OnlineODataProvider("Test", "https://devapimgmnt-dev-test.cfapps.eu20.hana.ondemand.com/Test")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        val binding: WorkOrdersListBinding = DataBindingUtil.setContentView(this, R.layout.work_orders_list)
        binding.lifecycleOwner = this
        setContentView(binding.root)
        provider.serviceOptions.checkVersion = false
        provider.serviceOptions.dataFormat = DataFormat.JSON
        provider.networkOptions.allowTunneling = true

        val recyclerView : RecyclerView = findViewById(R.id.work_orders_recyclerview)
        workOrderItemAdapter = WorkOrderItemAdapter(workOrderList)
        val layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = workOrderItemAdapter

        createList()
    }

    private fun createList()  {
        dataService = ZFIORI_EAM_APP_SRV_Entities(provider)

        val keys = "WorkOrderListSet(Phase1='2',Phase2='2',Phase3='2')/Set?$"
        val filter = "filter=(MaintenanceOrder%20eq%20%274636657%27)"
//        val filter= "filter=(IsUserResponsible%20eq%20true)"
        query = DataQuery().from(workOrderListSet).withURL(keys + filter)

        val result = dataService.getZEAMCWORKORDERLISTSet(query)
        if(result.size == 0) {
           val noAssignedWorkOrders = findViewById<TextView>(R.id.no_assigned_work_orders)
           noAssignedWorkOrders.visibility = View.VISIBLE
        }
        for(workOrder in result) {
        workOrderList.addAll(listOf(WorkOrder(workOrder.maintenanceOrderDesc.toString(), workOrder.maintPriority.toString(), workOrder.maintOrdBasicStartDate.toString(), workOrder.mainWorkCenter.toString(), workOrder.maintenanceOrderType.toString(), workOrder.maintenanceOrder,  workOrder.qualityInspection.toString(), workOrder.percentCompleteWrkQty.toString())))
        }
    }
}