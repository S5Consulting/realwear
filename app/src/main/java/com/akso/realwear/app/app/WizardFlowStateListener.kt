package com.akso.realwear.app.app

import android.content.Intent
import com.sap.cloud.mobile.flowv2.core.FlowContextRegistry.flowContext
import com.sap.cloud.mobile.flowv2.ext.FlowStateListener
import com.sap.cloud.mobile.foundation.model.AppConfig

class WizardFlowStateListener(private val application: SAPWizardApplication) :
    FlowStateListener() {

    override fun onAppConfigRetrieved(appConfig: AppConfig) {
    }

    override fun onApplicationReset() {
        this.application.resetApplication()
    }

    override fun onApplicationLocked() {
        super.onApplicationLocked()
        application.isApplicationUnlocked = true
    }

    override fun onFlowFinished(flowName: String?) {
        var userSwitchFlag = flowContext.getPreviousUserId()?.let {
            flowContext.getCurrentUserId() != it
        } ?: false

        flowName?.let{
            application.isApplicationUnlocked = true
        }

        if (userSwitchFlag) {
            Intent(application, MainBusinessActivity::class.java).also {
                it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                application.startActivity(it)
            }
        }
    }



}
