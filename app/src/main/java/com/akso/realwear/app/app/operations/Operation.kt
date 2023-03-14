package com.akso.realwear.app.app.operations

data class Operation(var title: String, var operationNumber: String, var operationConfirm: String, var operationOrderNumber: String, var operationRoutingNumber: String, var operationCount: String, var nextOperation: String?, var finalConfirmed : Boolean, var workCenter: String, var planningPlant: String)
