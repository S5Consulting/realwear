package com.akso.realwear.app.app.timeLog

data class TimeLogData(var createdBy: String, var actualWork :String, var actualWorkUnit: String, var remainingWork : String, var remainingWorkUnit: String, var finalConfirm : Boolean, var cancelled : Boolean, var comment: String)
