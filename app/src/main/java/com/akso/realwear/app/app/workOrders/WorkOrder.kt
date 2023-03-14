package com.akso.realwear.app.app.workOrders

import java.io.Serializable

data class WorkOrder(var title: String, var priority: String, var startDate: String, var workcenter: String, var orderType: String, var orderId: String, var inspection: String, var percentComplete: String): Serializable
