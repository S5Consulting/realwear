package com.akso.realwear.app.app.callAPI

data class TimelogTest(
    val OrderNumber: String,
    val MaintOrderRoutingNumber: String,
    val MaintOrderOperationCounter: String,
    val OperationNumber: String,
    val MaintenanceItemObjectList: String,
    val OperationDescription: String,
    val ExecutingWorkCenter: String,
    val PlannedPersons: Int,
    val PlannedWork: String,
    val PlannedWorkUnit: String,
    val Duration: String,
    val DurationUnit: String,
    val ActualWork: String,
    val StandardTextKey: String,
    val Confirmation : String,
    val FinalConfirmed: Boolean,
    val PlanningPlant : String,
    val OperationControlKey: String,
    val ActivityType: String,
    val ReturnErrorMessage: String
)
