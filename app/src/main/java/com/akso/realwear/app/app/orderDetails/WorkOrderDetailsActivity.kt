package com.akso.realwear.app.app.orderDetails

import WorkOrderDetailsFragmentAdapter
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.viewpager.widget.ViewPager
import com.akso.realwear.R
import com.akso.realwear.app.app.attachments.AttachmentData
import com.akso.realwear.app.app.equipment.EquipmentData
import com.akso.realwear.app.app.operations.Operation
import com.akso.realwear.app.app.subOperations.SuboperationsFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.sap.cloud.android.odata.zfiori_eam_app_srv_entities.*
import com.sap.cloud.android.odata.zfiori_eam_app_srv_entities.WorkOrderDetail
import com.sap.cloud.android.odata.zfiori_eam_app_srv_entities.ZFIORI_EAM_APP_SRV_Entities.workOrderDetailSet
import com.sap.cloud.mobile.odata.DataQuery
import com.sap.cloud.mobile.odata.OnlineODataProvider

class WorkOrderDetailsActivity: AppCompatActivity()  {
    private lateinit var dataService : ZFIORI_EAM_APP_SRV_Entities
    private lateinit var query : DataQuery
    val provider = OnlineODataProvider("Test", "https://devapimgmnt-dev-test.cfapps.eu20.hana.ondemand.com/Test")

    // Set global variables for use in fragments
    var orderDetailsData: WorkOrderDetail = WorkOrderDetail()
    var operationData = ArrayList<Operation>()
    var equipmentData = ArrayList<EquipmentData>()
    var attachmentData = ArrayList<AttachmentData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.work_order_details)
        dataService = ZFIORI_EAM_APP_SRV_Entities(provider)

        //Get order detail data
        val orderID = intent.getStringExtra("OrderID")
        query = DataQuery().from(workOrderDetailSet).withKey(WorkOrderDetail.key(orderID.toString()))

        orderDetailsData = dataService.getWorkOrderDetail(query)
        Log.i("orderquery", query.toString())
        Log.i("orderdetails", orderDetailsData.toString())
        //Get operation data
        val operationsQuery = DataQuery().from(workOrderDetailSet).withURL("$query/to_WorkOrderOper")
        val operationsResult = dataService.getZeamIWoOperationsList(operationsQuery)
        val nextOperationList = ArrayList<String>()

        for((index, operation ) in operationsResult.withIndex()) {
            if(index <= operationsResult.size -2) {
                nextOperationList.add(operationsResult[index + 1].operationNumber)
            }
        }
        var nextOperation = ""
        for((index, operation) in operationsResult.withIndex()) {
            nextOperation = if(index < nextOperationList.size) {
                nextOperationList[index]

            } else {
                "no"
            }
            operationData.addAll(listOf(Operation(operation.operationDescription.toString(), operation.operationNumber, operation.confirmation.toString(), operation.orderNumber, operation.maintOrderRoutingNumber.toString(), operation.maintOrderOperationCounter.toString(), nextOperation, operation.finalConfirmed as Boolean, operation.executingWorkCenter.toString(), operation.planningPlant.toString())))
        }
        Log.i("operationdata", operationsResult.toString())
        //Get equipment data
        val equipmentQuery = DataQuery().from(workOrderDetailSet).withURL("$query/to_WorkOrderObjectList")
        val equipmentResult = dataService.getZeamIWoObjectList(equipmentQuery)
        for(equipment in equipmentResult) {
        equipmentData.addAll(listOf(EquipmentData(equipment.equipmentDesc.toString(), equipment.equipment, equipment.material.toString(), equipment.materialDesc.toString(), equipment.serialNumber.toString(), equipment.functionalLocation, equipment.storageLocationName.toString() )))
        }

        // Get attachment data
        val attachmentQuery = DataQuery().from(workOrderDetailSet).withURL("$query/to_WorkOrderAttach")
        Log.i("attachmentquery", query.toString() + attachmentQuery.toString())
        val attachmentResult = dataService.getZeamIWoAttachmentShow(attachmentQuery)
        for(attachment in attachmentResult) {
            attachmentData.addAll(listOf(AttachmentData(attachment.attachDesc.toString() + ".${attachment.attachType}", attachment.createdBy.toString(), attachment.createdDate.toString(), attachment.attachURL.toString(), attachment.maintenanceOrder.toString())))
        }
        Log.i("attachmentres", attachmentResult.toString())

        val detailsTitle = findViewById<TextView>(R.id.details_title)
        val detailsPriority = findViewById<TextView>(R.id.details_priority)
        val detailsLocation = findViewById<TextView>(R.id.details_location)
        val detailsWorkCenter = findViewById<TextView>(R.id.details_workcenter)
        val detailsOrderID = findViewById<TextView>(R.id.details_id)
        val detailsOrderType = findViewById<TextView>(R.id.details_order_type)

        detailsTitle.text = orderDetailsData.maintenanceOrderDesc
        detailsPriority.text = getString(R.string.work_orders_priority) + " " + orderDetailsData.maintPriorityDesc
        detailsLocation.text = getString(R.string.details_location) + " " + orderDetailsData.functionalLocation
        detailsWorkCenter.text = orderDetailsData.mainWorkCenter
        detailsOrderID.text = orderID
        detailsOrderType.text = orderDetailsData.maintenanceOrderType


        val viewPager: ViewPager = findViewById(R.id.viewPager)
        viewPager.adapter = WorkOrderDetailsFragmentAdapter(supportFragmentManager)

        val tabLayout = findViewById<View>(R.id.work_order_details_tablayout) as TabLayout // get the reference of TabLayout
        tabLayout.setupWithViewPager(viewPager)
        tabLayout.isNestedScrollingEnabled = false

        // Draw dividers between tabs
        val root = tabLayout.getChildAt(0)
        if (root is LinearLayout) {
            root.showDividers = LinearLayout.SHOW_DIVIDER_MIDDLE
            val drawable = GradientDrawable()
            drawable.setColor(Color.parseColor("#e5e5e5"))
            drawable.setSize(1, 1)
            root.dividerPadding = 10
            root.dividerDrawable = drawable
        }

        tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
               val scroll = findViewById<NestedScrollView>(R.id.work_order_details_fragment_scroll)
                scroll.smoothScrollTo(0, 0)

            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })


        //TODO(set correct icons, check if round background is possible)
        tabLayout.getTabAt(0)?.setIcon(R.drawable.ic_info_black_24dp)
        tabLayout.getTabAt(0)?.setText(R.string.order_details_title)
        tabLayout.getTabAt(1)?.setIcon(R.drawable.ic_assignment_turned_in_black_24dp)
        tabLayout.getTabAt(1)?.setText(R.string.operations_title)
        tabLayout.getTabAt(2)?.setIcon(R.drawable.ic_attach_file_black_24dp)
        tabLayout.getTabAt(2)?.setText(R.string.equipment_title)
        tabLayout.getTabAt(3)?.setIcon(R.drawable.ic_attachment)
        tabLayout.getTabAt(3)?.setText(R.string.attachments_title)
        tabLayout.getTabAt(0)?.contentDescription = "hf_use_description|Order details"


        val btn = findViewById<Button>(R.id.btn_back)
        btn.setOnClickListener {
            finish()
        }
    }
}