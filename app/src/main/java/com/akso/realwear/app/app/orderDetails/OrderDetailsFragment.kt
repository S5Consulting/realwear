package com.akso.realwear.app.app.orderDetails

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.akso.realwear.R
import kotlinx.android.synthetic.main.work_order_details.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [OrderDetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class OrderDetailsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var ACTIVITY: WorkOrderDetailsActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        ACTIVITY = context as WorkOrderDetailsActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_order_details, container, false)
        // Inflate the layout for this fragment
        val data = ACTIVITY.orderDetailsData

        val woType = view.findViewById<TextView>(R.id.order_detail_wo_type)
        val woText = view.findViewById<TextView>(R.id.order_detail_text)
        val woLocation = view.findViewById<TextView>(R.id.order_detail_location)
        val woJobType = view.findViewById<TextView>(R.id.order_detail_job_type)
        val woPlanningPlant = view.findViewById<TextView>(R.id.order_detail_planning_plant)
        val woSalesOrder = view.findViewById<TextView>(R.id.order_detail_sales_order)
        val woWorkCenterPlant = view.findViewById<TextView>(R.id.order_detail_workcenter_plant)
        val woMainWorkCenter = view.findViewById<TextView>(R.id.order_detail_main_work_center)
        val woEmployeeResponsible = view.findViewById<TextView>(R.id.order_detail_employee_responsible)
        val woSoPartner = view.findViewById<TextView>(R.id.order_detail_so_partner)

        woType.text = data.maintenanceOrderType + ", " + data.maintenanceOrder
        woText.text = data.maintenanceOrderDesc
        woLocation.text = data.functionalLocation + "(" + data.functionalLocationName + ")"
        woJobType.text = data.maintenanceActivityType + "(" + data.maintenanceActivityTypeName + ")"
        woPlanningPlant.text = data.maintenancePlanningPlant + ", " + data.maintenancePlanningPlantName
        woSalesOrder.text = data.salesOrder + " - " + data.salesOrderItem
        woWorkCenterPlant.text = data.mainWorkCenterPlant + "(" + data.mainWorkCenterPlantName + ")"
        woMainWorkCenter.text = data.mainWorkCenter + " - " + data.mainWorkCenterText
        woEmployeeResponsible.text = data.personResponsibleName
        woSoPartner.text = data.soPartner

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment OrderDetailsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(data: WorkOrderDetail): OrderDetailsFragment =
            OrderDetailsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}