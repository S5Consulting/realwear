package com.akso.realwear.app.app.operations

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.akso.realwear.R
import com.akso.realwear.app.app.orderDetails.WorkOrderDetailsActivity

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [OperationsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class OperationsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var ACTIVITY: WorkOrderDetailsActivity
    private var operationsList = ArrayList<Operation>()

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

        val data = ACTIVITY.operationData
        for(operation in data) {
            operationsList.add(operation)
        }

        Log.i("operdata", data.toString())
    }


    //TODO(define operations data here)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_operations, container, false)
        val recyclerView : RecyclerView = view.findViewById(R.id.operations_list)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = OperationItemAdapter(operationsList)

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment OperationsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            OperationsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}