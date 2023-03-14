package com.akso.realwear.app.app.subOperations

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.akso.realwear.R
import com.akso.realwear.app.app.operationDetails.OperationDetailsActivity
import kotlinx.android.synthetic.main.fragment_suboperations.*
import java.util.Observer
import java.util.logging.Filter

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SuboperationsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */


class SuboperationsFragment : Fragment(), ISubOp {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var ACTIVITY: OperationDetailsActivity
    var suboperationsList = ArrayList<SubOperationData>()
    var isAllChecked : Boolean = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        ACTIVITY = context as OperationDetailsActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        val data = ACTIVITY.subOperationData

        for(subOp in data) {
            suboperationsList.addAll(listOf(SubOperationData(subOp.operation, subOp.title, subOp.description, subOp.isCompleted)))
            isAllChecked = data.all { subOp.isCompleted }
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_suboperations, container, false)
        val recyclerView : RecyclerView = view.findViewById(R.id.suboperations_list)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = SuboperationItemAdapter(suboperationsList)

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SuboperationsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SuboperationsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun passData(position: Int, id: String) {
        val bundle = Bundle()
        bundle.putInt("input_pos", position)
        bundle.putString("input_id", id)

        val transaction = this.parentFragmentManager.beginTransaction()

    }
}