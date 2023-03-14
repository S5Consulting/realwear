package com.akso.realwear.app.app.orderDetails

import java.io.Serializable

data class WorkOrderDetail(val orderType: String, val orderText: String, val location: String, val jobType: String, val planningPlant: String, val salesOrder: String, val workCenterPlant: String, val mainWorkCenter: String, val employeeResponsible: String, val SOpartner: String): Serializable
