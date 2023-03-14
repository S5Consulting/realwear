package com.akso.realwear.app.app.spareParts

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

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SparePartsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SparePartsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var ACTIVITY: OperationDetailsActivity
    var sparePartsList = ArrayList<SparePartData>()

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
        val data = ACTIVITY.sparePartsData
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_spare_parts, container, false)
        val tableLayout : TableLayout = view.findViewById(R.id.spare_parts_table)
        val ctx = view.context
        val i = 1
        for (part in data) {
            val tr = TableRow(ctx)
            tr.setPadding(0,8,0,8)
            tr.background = ContextCompat.getDrawable(ctx, R.drawable.border_bottom)
            val materialTV = TextView(ctx)
            val plantTV = TextView(ctx)
            val stockTV = TextView(ctx)
            val quantitiesTV = TextView(ctx)
            //TODO
            materialTV.text = part.material + "\n" + part.description
            plantTV.text = part.plant
            stockTV.text = part.stock + " EA"
            quantitiesTV.text = part.required_quantity + " " + getString(R.string.subop_required_quantity) + "\n" + part.withdrawn_quantity + " " + getString(R.string.subop_withdrawn_quantity)

            tr.addView(materialTV)
            tr.addView(plantTV)
            tr.addView(stockTV)
            tr.addView(quantitiesTV)
            tableLayout.addView(tr, i)
            i+1
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
         * @return A new instance of fragment SparePartsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SparePartsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}