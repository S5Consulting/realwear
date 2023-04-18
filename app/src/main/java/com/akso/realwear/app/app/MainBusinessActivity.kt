package com.akso.realwear.app.app

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.akso.realwear.R
import com.akso.realwear.app.app.workOrders.WorkOrderListActivity


class MainBusinessActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_business)
    }


    private fun startEntitySetListActivity() {
        startActivity(Intent(this, WorkOrderListActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        })
    }

    override fun onResume() {
        super.onResume()
        startEntitySetListActivity()
    }

    companion object {
    }
}
