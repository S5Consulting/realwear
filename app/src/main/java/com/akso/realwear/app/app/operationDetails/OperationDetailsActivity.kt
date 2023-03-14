package com.akso.realwear.app.app.operationDetails

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.viewpager.widget.ViewPager
import com.akso.realwear.R
import com.akso.realwear.app.app.attachments.AttachmentData
import com.akso.realwear.app.app.spareParts.SparePartData
import com.akso.realwear.app.app.subOperations.SubOperationData
import com.akso.realwear.app.app.timeLog.TimeLogData
import com.google.android.material.tabs.TabLayout
import com.sap.cloud.android.odata.zfiori_eam_app_srv_entities.*
import com.sap.cloud.mobile.odata.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.onEach

class OperationDetailsActivity: AppCompatActivity() {
    private lateinit var dataService : ZFIORI_EAM_APP_SRV_Entities
    val provider = OnlineODataProvider("Test", "https://devapimgmnt-dev-test.cfapps.eu20.hana.ondemand.com/Test")
    var subOperationData = ArrayList<SubOperationData>()
    var sparePartsData = ArrayList<SparePartData>()
    var attachmentData = ArrayList<AttachmentData>()
    var timeLogData = ArrayList<TimeLogData>()
    var orderNumber : String? = ""
    var operationNumber : String? = ""
    var operationControlKey : String? = ""

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.operation_details)
        dataService = ZFIORI_EAM_APP_SRV_Entities(provider)
        provider.serviceOptions.checkVersion = false

        val titleData = intent.getStringExtra("Title")
        orderNumber = intent.getStringExtra("OrderNum")
        val routingNumber = intent.getStringExtra("OrderRoutingNum")
        operationNumber = intent.getStringExtra("OperationNum")
        val operationCounter = intent.getStringExtra("OperationCount")
        val nextOperation = intent.getStringExtra("NextOperation")
        var operationIndex = intent.getIntExtra("OperationIndex", 0)
        val finalConfirm = intent.getBooleanExtra("FinalConfirmed", false)
        val workCenter = intent.getStringExtra("WorkCenter")
        val planningPlant = intent.getStringExtra("PlanningPlant")
        var lastChecked: String?
        var isAllChecked = false

        val query = DataQuery().from(ZFIORI_EAM_APP_SRV_Entities.workOrderDetailSet).withKey(WorkOrderDetail.key(orderNumber.toString()))
        val nextOpQuery = DataQuery().from(ZFIORI_EAM_APP_SRV_Entities.workOrderDetailSet).withURL("$query/to_WorkOrderOper")
        val nextOpResult = dataService.getZeamIWoOperationsList(nextOpQuery)
        operationControlKey = nextOpResult[0].operationControlKey
        Log.i("operkey", nextOpResult[0].operationControlKey.toString())
        Log.i("nextquery", nextOpQuery.toString())
        Log.i("nextres", nextOpResult.toString())

        //Get suboperation data
        val subOpQuery = DataQuery().withURL("WOSubOperaListSet(OrderNumber='$orderNumber',OperationNumber='$operationNumber')/Set?")
        val subOpResult = dataService.getZEAMCWOSUBOPERATIONSLISTSet(subOpQuery)

        for (subop in subOpResult) {
            subOperationData.addAll(listOf(SubOperationData(subop.operationNumber, subop.maintenanceOrderSubOperation, subop.operationDescription.toString(), subop.finalConfirmed as Boolean)))
        }
        Log.i("subopdata", subOperationData.toString())


        //Get sparepart data
        val sparePartQuery = DataQuery().withURL("WorkOrderOperDetailSet(OrderNumber='$orderNumber',MaintOrderRoutingNumber='$routingNumber',MaintOrderOperationCounter='$operationCounter')/to_WorkOrderOperComp")
        val sparePartResult = dataService.getZeamIWoComponents(sparePartQuery)
        for(sparePart in sparePartResult) {
            Log.i("sparepart", sparePart.toString())
            sparePartsData.addAll(listOf(SparePartData(sparePart.material.toString(), sparePart.description.toString(), sparePart.plant.toString(), sparePart.stock.toString(), sparePart.requiredQty.toString(), sparePart.withdrawnQty.toString())))
        }

        //Get operation attachment data
        val attachmentQuery = DataQuery().withURL("WorkOrderOperDetailSet(OrderNumber='$orderNumber',MaintOrderRoutingNumber='$routingNumber',MaintOrderOperationCounter='$operationCounter')/to_WorkOrderOperAttach")
        val attachmentResult = dataService.getZeamIWoOperDocuments(attachmentQuery)
        //Log.i("attachmentq", attachmentQuery.toString())
        for(attachment in attachmentResult) {
            attachmentData.addAll(listOf(AttachmentData(attachment.attachDesc.toString() + ".${attachment.attachType}", attachment.createdBy.toString(), attachment.createdDate.toString(), attachment.mediaStream.readLink.toString(), attachment.maintOrderRoutingNumber)))
        }


        Log.i("attachmentres", attachmentResult.toString())
        //Get timelog data
        val timeLogQuery = DataQuery().withURL("WorkOrderOperDetailSet(OrderNumber='$orderNumber',MaintOrderRoutingNumber='$routingNumber',MaintOrderOperationCounter='$operationCounter')/to_WorkOrderOperConf?")
        val timeLogResult = dataService.getZeamIWoOperaConfDet(timeLogQuery)
        for(time in timeLogResult) {
            timeLogData.addAll(listOf(TimeLogData(time.changedBy.toString(), time.actualWork.toString(), time.actualWorkUnit.toString(), time.remainingWork.toString(), time.remainingWorkUnit.toString(), time.finalConfirmation as Boolean, time.cancelled as Boolean, time.confirmText.toString())))
        }
        Log.i("timelogres", timeLogResult.toString())

        val title = findViewById<TextView>(R.id.operation_details_title)
        val btnBack = findViewById<Button>(R.id.btn_back_operation_details)
        val btnToggleInfo = findViewById<TextView>(R.id.btn_toggle_op_info)
        val nextOperationText = findViewById<TextView>(R.id.next_operation_text)
        val btnNextOperation = findViewById<TextView>(R.id.btn_next_operation)
        val operationInfo = findViewById<TextView>(R.id.operation_info)

        operationInfo.isVisible = false
        btnToggleInfo.setTextColor(R.color.light_blue)
        btnNextOperation.setTextColor(R.color.light_blue)
        if(nextOperation == "no") {
            nextOperationText.isVisible = false
            btnNextOperation.isVisible = false
        } else {
            nextOperationText.isVisible = true
            btnNextOperation.isVisible = true
        }
        btnNextOperation.text = nextOperation
        title.text = titleData
        operationInfo.text = titleData

        val viewPager: ViewPager = findViewById(R.id.opViewPager)
        viewPager.adapter = OperationFragmentAdapter(supportFragmentManager)

        val tabLayout = findViewById<View>(R.id.operation_tablayout) as TabLayout // get the reference of TabLayout
        tabLayout.setupWithViewPager(viewPager)

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

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val scroll = findViewById<NestedScrollView>(R.id.operation_details_scroll)
                scroll.smoothScrollTo(0, 0)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        tabLayout.isNestedScrollingEnabled = false
        tabLayout.getTabAt(0)?.setIcon(R.drawable.ic_info_black_24dp)
        tabLayout.getTabAt(1)?.setIcon(R.drawable.ic_policy_check_24dp)
        tabLayout.getTabAt(2)?.setIcon(R.drawable.ic_attach_file_black_24dp)
        tabLayout.getTabAt(3)?.setIcon(R.drawable.ic_timeline_end_node)

        val opTimer = findViewById<TextView>(R.id.operation_timer)

        val startTimeStamp: LocalDateTime = LocalDateTime.now()
        var endTimeStamp: LocalDateTime = startTimeStamp

        // Get current users data
        val login = dataService.loginUserCheckSet
        var isPromark : Boolean? = false
        var username = ""
        for (i in login) {
            username = i.userName
            isPromark = i.isPromark
        }

        val timer = (0..Int.MAX_VALUE)
            .asSequence()
            .asFlow()
            .onEach { delay(1_000) }

        GlobalScope.launch {
            withContext(Dispatchers.Main) {
                while(!isAllChecked) {
                    timer.collect {
                        val hours = it / 3600
                        val minutes = (it % 3600) / 60
                        val seconds = it % 60
                        val timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds)
                        opTimer.text = timeString
                        endTimeStamp = startTimeStamp.plusSeconds(it.toLong())
                    }
                }
            }
        }

        //Receive last checked suboperation from SuboperationItemAdapter
        val mMessageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent) {

                lastChecked = intent.getStringExtra("lastChecked")
                val selectedSubOp = subOpResult.find {it.maintenanceOrderSubOperation == lastChecked}
                selectedSubOp?.finalConfirmed = true

                isAllChecked = intent.getBooleanExtra("isAllChecked", false)
                if(isAllChecked) {
                    val builder = AlertDialog.Builder(this@OperationDetailsActivity)
                    builder.setTitle("Set operation as final?")
                    //builder.setMessage("We have a message")
                    builder.setPositiveButton("Confirm") { _, _ ->
                        dataService.updateTime(
                            isPromark,
                            orderNumber,
                            operationNumber,
                            lastChecked,
                            "test",
//                            "345984",
                            username,
                            workCenter,
                            planningPlant,
                            "",
                            startTimeStamp,
                            endTimeStamp,
                            "START_TIME",
                            "true",
                            "AHA",
                            null,
                            null,
                            null
                        )
                        dataService.updateTime(
                            isPromark,
                            orderNumber,
                            operationNumber,
                            lastChecked,
                            "test",
//                            "345984",
                            username,
                            workCenter,
                            planningPlant,
                            "",
                            startTimeStamp,
                            endTimeStamp,
                            "STOP_TIME",
                            "true",
                            "AHA",
                            null,
                            null,
                            null
                        )
                       dataService.updateTime(
                            isPromark,
                            orderNumber,
                            operationNumber,
                            lastChecked,
                            "test",
//                           "345984",
                            username,
                            workCenter,
                            planningPlant,
                            "",
                            startTimeStamp,
                            endTimeStamp,
                            "CONFIRM_TIME",
                            "true",
                            "AHA",
                            null,
                            null,
                            null
                        )



                        Log.i("returnmessage", MessageReturn().toString())
                        Toast.makeText(applicationContext,
                            "Operation set as final.", Toast.LENGTH_SHORT).show()
                    }
                    builder.setNegativeButton(android.R.string.no) { _, _ ->
                        Toast.makeText(applicationContext,
                            "Cancelled", Toast.LENGTH_SHORT).show()
                    }
                    try {
                        builder.show()
                    }
                    catch (e: Exception)
                    { Log.e("builder_error", e.toString()) }
                }
                else {
                    dataService.updateTime(
                        isPromark,
                        orderNumber,
                        operationNumber,
                        lastChecked,
                        "test",
//                        "345984",
                            username,
                        workCenter,
                        planningPlant,
                        "",
                        startTimeStamp,
                        endTimeStamp,
                        "START_TIME",
                        "true",
                        "AHA",
                        null,
                        null,
                        null
                    )
                    dataService.updateTime(
                        isPromark,
                        orderNumber,
                        operationNumber,
                        lastChecked,
                        "test",
//                        "345984",
                        username,
                        workCenter,
                        planningPlant,
                        "",
                        startTimeStamp,
                        endTimeStamp,
                        "STOP_TIME",
                        "",
                        "AHA",
                        null,
                        null,
                        null
                    )
                    Log.i("returnmessage", MessageReturn().toString())
                }
            }
        }

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
            IntentFilter("lastChecked")
        )

//        val asd = subOpResult[0]
//        asd.finalConfirmed = true
//        dataService.updateEntity(asd)

        btnBack.setOnClickListener {
            finish()
        }

        btnToggleInfo.setOnClickListener {
            operationInfo.isVisible = !operationInfo.isVisible
            if(operationInfo.isVisible) {
                btnToggleInfo.text = getText(R.string.operation_details_less)
            } else {
                btnToggleInfo.text = getText(R.string.operation_details_more)
            }
        }

        btnNextOperation.setOnClickListener {
            val intent = Intent(this, OperationDetailsActivity::class.java)
            if(operationIndex < nextOpResult.size -2) {
                operationIndex += 1
            }

            intent.putExtra("OperationNum", nextOperation)
            intent.putExtra("Title", nextOpResult[operationIndex +1].operationDescription)
            intent.putExtra("OrderNum", nextOpResult[operationIndex +1].orderNumber)
            intent.putExtra("OrderRoutingNum", nextOpResult[operationIndex +1].maintOrderRoutingNumber)
            intent.putExtra("OperationCount", nextOpResult[operationIndex +1].maintOrderOperationCounter)
            intent.putExtra("NextOperation", nextOpResult[operationIndex +1].operationNumber)
            startActivity(intent)
        }

    }
}