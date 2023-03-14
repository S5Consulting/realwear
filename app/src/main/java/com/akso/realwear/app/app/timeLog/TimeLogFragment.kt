package com.akso.realwear.app.app.timeLog

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.akso.realwear.R
import com.akso.realwear.app.app.operationDetails.OperationDetailsActivity
import com.akso.realwear.app.app.orderDetails.WorkOrderDetailsActivity

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TimeLogFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TimeLogFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var ACTIVITY: OperationDetailsActivity

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

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val data = ACTIVITY.timeLogData
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_time_log, container, false)
        val table : TableLayout = view.findViewById(R.id.time_log_table)
        val ctx = view.context
        val i = 1
        for (time in data) {
            val tr = TableRow(ctx)
            tr.setPadding(0,8,0,8)
            tr.background = ContextCompat.getDrawable(ctx, R.drawable.border_bottom)
            val createdBy = TextView(ctx)
            val actualWork = TextView(ctx)
            val remainingWork = TextView(ctx)
            val finalConfirm = TextView(ctx)
            val cancel = TextView(ctx)
            val comment = TextView(ctx)

            createdBy.text = time.createdBy
            actualWork.text = time.actualWork + time.actualWorkUnit
            remainingWork.text = time.remainingWork + time.remainingWorkUnit
            if(time.finalConfirm) {
                finalConfirm.text = "\u2713"
            }
            if(time.cancelled) {
                cancel.text = "\u2713"
            }
            comment.text = time.comment

            tr.addView(createdBy)
            tr.addView(actualWork)
            tr.addView(remainingWork)
            tr.addView(finalConfirm)
            tr.addView(cancel)
            tr.addView(comment)
            i + 1
            table.addView(tr, i)
        }

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TimeLogFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TimeLogFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}