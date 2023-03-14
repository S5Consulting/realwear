package com.akso.realwear.app.app.operationDetails

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.akso.realwear.app.app.operationAttachments.OperationAttachmentsFragment
import com.akso.realwear.app.app.spareParts.SparePartsFragment
import com.akso.realwear.app.app.subOperations.SuboperationsFragment
import com.akso.realwear.app.app.timeLog.TimeLogFragment

class OperationFragmentAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getCount(): Int {
        return 4;
    }

    override fun getItem(position: Int): Fragment {
        when(position) {
            0 -> {
                return SuboperationsFragment()
            }
            1 -> {
                return SparePartsFragment()
            }
            2 -> {
                return OperationAttachmentsFragment()
            }
            3 -> {
                return TimeLogFragment()
            }
            else -> {
                return SuboperationsFragment()
            }
        }
    }

//TODO use strings for these
    override fun getPageTitle(position: Int): CharSequence? {
        when(position) {
            0 -> {
                return "Suboperations"
            }
            1 -> {
                return "Spare parts"
            }
            2 -> {
                return "Attachments"
            }
            3 -> {
                return "Time log"
            }
        }
        return super.getPageTitle(position)
    }

}