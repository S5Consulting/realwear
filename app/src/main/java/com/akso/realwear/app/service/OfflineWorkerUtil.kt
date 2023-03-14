package com.akso.realwear.service

import android.content.Context
import android.util.Base64
import androidx.work.*
import com.akso.realwear.app.app.SAPWizardApplication
import com.sap.cloud.android.odata.zfiori_eam_app_srv_entities.ZFIORI_EAM_APP_SRV_Entities

import com.sap.cloud.mobile.flowv2.core.FlowContextRegistry
import com.sap.cloud.mobile.flowv2.securestore.UserSecureStoreDelegate
import com.sap.cloud.mobile.foundation.common.ClientProvider
import com.sap.cloud.mobile.foundation.common.SettingsProvider
import com.sap.cloud.mobile.foundation.model.AppConfig
import com.sap.cloud.mobile.odata.core.AndroidSystem
import com.sap.cloud.mobile.odata.core.LoggerFactory
import com.sap.cloud.mobile.odata.offline.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.net.URL
import java.security.SecureRandom
import java.util.*

object OfflineWorkerUtil {
    var offlineODataProvider: OfflineODataProvider? = null
        private set

    /** OData service for interacting with local OData Provider */
    lateinit var zFIORI_EAM_APP_SRV_Entities: ZFIORI_EAM_APP_SRV_Entities
        private set
    private val logger = LoggerFactory.getLogger(OfflineWorkerUtil::class.java.toString())
    private val progressListeners = mutableSetOf<OfflineProgressListener>()

    var userSwitchFlag = false

    var openRequest: OneTimeWorkRequest? = null
        private set

    @JvmStatic
    fun resetOpenRequest() {
        openRequest = null
    }

    var syncRequest: OneTimeWorkRequest? = null
        private set

    @JvmStatic
    fun resetSyncRequest() {
        syncRequest = null
    }

    const val OFFLINE_OPEN_WORKER_UNIQUE_NAME = "offline_init_sync_worker"
    const val OFFLINE_SYNC_WORKER_UNIQUE_NAME = "offline_sync_worker"

    const val OUTPUT_ERROR_KEY = "output.error"
    const val OUTPUT_ERROR_DETAIL = "output.error.detail"

    /** Name of the offline data file on the application file space */
    private const val OFFLINE_DATASTORE = "OfflineDataStore"
    const val OFFLINE_DATASTORE_ENCRYPTION_KEY =
        "Offline_DataStore_EncryptionKey"

    /** Header name for application version */
    private const val APP_VERSION_HEADER = "X-APP-VERSION"

    /** Connection ID of Mobile Application*/
    private const val CONNECTION_ID_ZFIORI_EAM_APP_SRV_ENTITIES = "Test"

    /** The preference to say whether offline is initialized. */
    const val PREF_OFFLINE_INITIALIZED = "pref.offline.db.initialized"

    /** The preference to say whether app is in foreground service. */
    const val PREF_FOREGROUND_SERVICE = "pref.foreground.service"

    /** The preference to say whether app just deleted registration. */
    const val PREF_DELETE_REGISTRATION = "pref.delete.registration"

    @JvmStatic
    fun addProgressListener(listener: OfflineProgressListener) =
        progressListeners.add(listener)

    @JvmStatic
    fun removeProgressListener(listener: OfflineProgressListener) =
        progressListeners.remove(listener)

    @JvmStatic
    fun resetOfflineODataProvider() {
        offlineODataProvider = null
    }

    private val delegate = object : OfflineODataProviderDelegate {
        override fun updateOpenProgress(
            p0: OfflineODataProvider,
            p1: OfflineODataProviderOperationProgress
        ) = notifyListeners(p0, p1)

        override fun updateDownloadProgress(
            p0: OfflineODataProvider,
            p1: OfflineODataProviderDownloadProgress
        ) = notifyListeners(p0, p1)

        override fun updateUploadProgress(
            p0: OfflineODataProvider,
            p1: OfflineODataProviderOperationProgress
        ) = notifyListeners(p0, p1)

        override fun updateFailedRequest(p0: OfflineODataProvider, p1: OfflineODataFailedRequest) =
            Unit

        override fun updateSendStoreProgress(
            p0: OfflineODataProvider,
            p1: OfflineODataProviderOperationProgress
        ) = notifyListeners(p0, p1)

        private fun notifyListeners(
            p0: OfflineODataProvider,
            p1: OfflineODataProviderOperationProgress
        ) {
            logger.debug("Progress " + p1.currentStepNumber + " out of " + p1.totalNumberOfSteps)
            MainScope().launch {
                progressListeners.forEach {
                    it.onOfflineProgress(p0, p1)
                }
            }
        }
    }

    /*
     * Create OfflineODataProvider
     * This is a blocking call, no data will be transferred until open, download, upload
     */
    @JvmStatic
    fun initializeOffline(
        context: Context,
        appConfig: AppConfig,
        runtimeMultipleUserMode: Boolean
    ) {
        if (offlineODataProvider != null) return
        if (FlowContextRegistry.flowContext.getCurrentUserId().isNullOrEmpty())
            error("Current user not ready yet.")

        AndroidSystem.setContext(context as SAPWizardApplication)
        val serviceUrl = appConfig.serviceUrl
        try {
            val url = URL(serviceUrl + CONNECTION_ID_ZFIORI_EAM_APP_SRV_ENTITIES)
            val offlineODataParameters = OfflineODataParameters().apply {
                isEnableRepeatableRequests = true
                storeName = OFFLINE_DATASTORE
                currentUser = FlowContextRegistry.flowContext.getCurrentUserId()
                isForceUploadOnUserSwitch = runtimeMultipleUserMode
                val encryptionKey = if (runtimeMultipleUserMode) {
                    UserSecureStoreDelegate.getInstance().getOfflineEncryptionKey()
                } else { //If is single user mode, create and save a key into user secure store for accessing offline DB
                    if (UserSecureStoreDelegate.getInstance().getData<String>(OFFLINE_DATASTORE_ENCRYPTION_KEY) == null) {
                        val bytes = ByteArray(32)
                        val random = SecureRandom()
                        random.nextBytes(bytes)
                        val key = Base64.encodeToString(bytes, Base64.NO_WRAP)
                        UserSecureStoreDelegate.getInstance().saveData(OFFLINE_DATASTORE_ENCRYPTION_KEY, key)
                        Arrays.fill(bytes, 0.toByte())
                        key
                    } else {
                        UserSecureStoreDelegate.getInstance().getData<String>(OFFLINE_DATASTORE_ENCRYPTION_KEY)
                    }
                }
                storeEncryptionKey = encryptionKey
            }.also {
                // Set the default application version
                val customHeaders = it.customHeaders
                customHeaders[APP_VERSION_HEADER] = SettingsProvider.get().applicationVersion
                // In case of offlineODataParameters.customHeaders returning a new object if customHeaders from offlineODataParameters is null, set again as below
                it.setCustomHeaders(customHeaders)
            }

            offlineODataProvider = OfflineODataProvider(
                url,
                offlineODataParameters,
                ClientProvider.get(),
                delegate
            ).apply {
                val addMaterialSetQuery = OfflineODataDefiningQuery("AddMaterialSet", "AddMaterialSet", false)
                addDefiningQuery(addMaterialSetQuery)
                val cStsObjActiveStatusCodeTextQuery = OfflineODataDefiningQuery("C_StsObjActiveStatusCodeText", "C_StsObjActiveStatusCodeText", false)
                addDefiningQuery(cStsObjActiveStatusCodeTextQuery)
                val checkAppsAccessSetQuery = OfflineODataDefiningQuery("CheckAppsAccessSet", "CheckAppsAccessSet", false)
                addDefiningQuery(checkAppsAccessSetQuery)
                val equipmentListSetQuery = OfflineODataDefiningQuery("EquipmentListSet", "EquipmentListSet", false)
                addDefiningQuery(equipmentListSetQuery)
                val iCustomerVHQuery = OfflineODataDefiningQuery("I_Customer_VH", "I_Customer_VH", false)
                addDefiningQuery(iCustomerVHQuery)
                val iFunctionalLocationStdVHQuery = OfflineODataDefiningQuery("I_FunctionalLocationStdVH", "I_FunctionalLocationStdVH", false)
                addDefiningQuery(iFunctionalLocationStdVHQuery)
                val iMaintActyTypeStdVHQuery = OfflineODataDefiningQuery("I_MaintActyTypeStdVH", "I_MaintActyTypeStdVH", false)
                addDefiningQuery(iMaintActyTypeStdVHQuery)
                val iMaintPlnrGrpStdVHQuery = OfflineODataDefiningQuery("I_MaintPlnrGrpStdVH", "I_MaintPlnrGrpStdVH", false)
                addDefiningQuery(iMaintPlnrGrpStdVHQuery)
                val iMaintenanceRevisionStdVHQuery = OfflineODataDefiningQuery("I_MaintenanceRevisionStdVH", "I_MaintenanceRevisionStdVH", false)
                addDefiningQuery(iMaintenanceRevisionStdVHQuery)
                val iMaterialStdVHQuery = OfflineODataDefiningQuery("I_MaterialStdVH", "I_MaterialStdVH", false)
                addDefiningQuery(iMaterialStdVHQuery)
                val iPersWrkAgrmtSrchHelpQuery = OfflineODataDefiningQuery("I_PersWrkAgrmtSrchHelp", "I_PersWrkAgrmtSrchHelp", false)
                addDefiningQuery(iPersWrkAgrmtSrchHelpQuery)
                val iPlantStdVHQuery = OfflineODataDefiningQuery("I_PlantStdVH", "I_PlantStdVH", false)
                addDefiningQuery(iPlantStdVHQuery)
                val iSalesOrderItemStdVHQuery = OfflineODataDefiningQuery("I_SalesOrderItemStdVH", "I_SalesOrderItemStdVH", false)
                addDefiningQuery(iSalesOrderItemStdVHQuery)
                val iSalesOrderStdVHQuery = OfflineODataDefiningQuery("I_SalesOrderStdVH", "I_SalesOrderStdVH", false)
                addDefiningQuery(iSalesOrderStdVHQuery)
                val iStorageLocationQuery = OfflineODataDefiningQuery("I_StorageLocation", "I_StorageLocation", false)
                addDefiningQuery(iStorageLocationQuery)
                val iWrkCtrBySemanticKeyStdVHQuery = OfflineODataDefiningQuery("I_WrkCtrBySemanticKeyStdVH", "I_WrkCtrBySemanticKeyStdVH", false)
                addDefiningQuery(iWrkCtrBySemanticKeyStdVHQuery)
                val locationsSetQuery = OfflineODataDefiningQuery("LocationsSet", "LocationsSet", false)
                addDefiningQuery(locationsSetQuery)
                val loginUserCheckSetQuery = OfflineODataDefiningQuery("LoginUserCheckSet", "LoginUserCheckSet", false)
                addDefiningQuery(loginUserCheckSetQuery)
                val longTextGetSetQuery = OfflineODataDefiningQuery("LongTextGetSet", "LongTextGetSet", false)
                addDefiningQuery(longTextGetSetQuery)
                val messageReturnSetQuery = OfflineODataDefiningQuery("MessageReturnSet", "MessageReturnSet", false)
                addDefiningQuery(messageReturnSetQuery)
                val messagesReturnSetQuery = OfflineODataDefiningQuery("MessagesReturnSet", "MessagesReturnSet", false)
                addDefiningQuery(messagesReturnSetQuery)
                val userContactSetQuery = OfflineODataDefiningQuery("UserContactSet", "UserContactSet", false)
                addDefiningQuery(userContactSetQuery)
                val wOSubOperaListSetQuery = OfflineODataDefiningQuery("WOSubOperaListSet", "WOSubOperaListSet", false)
                addDefiningQuery(wOSubOperaListSetQuery)
                val wOTileCounterSetQuery = OfflineODataDefiningQuery("WOTileCounterSet", "WOTileCounterSet", false)
                addDefiningQuery(wOTileCounterSetQuery)
                val workOrdSubOperDocSetQuery = OfflineODataDefiningQuery("WorkOrdSubOperDocSet", "WorkOrdSubOperDocSet", false)
                addDefiningQuery(workOrdSubOperDocSetQuery)
                val workOrderDetailSetQuery = OfflineODataDefiningQuery("WorkOrderDetailSet", "WorkOrderDetailSet", false)
                addDefiningQuery(workOrderDetailSetQuery)
                val workOrderListSetQuery = OfflineODataDefiningQuery("WorkOrderListSet", "WorkOrderListSet", false)
                addDefiningQuery(workOrderListSetQuery)
                val workOrderOperDetailSetQuery = OfflineODataDefiningQuery("WorkOrderOperDetailSet", "WorkOrderOperDetailSet", false)
                addDefiningQuery(workOrderOperDetailSetQuery)
                val zEAMCADDMATERIALSetQuery = OfflineODataDefiningQuery("ZEAM_C_ADD_MATERIALSet", "ZEAM_C_ADD_MATERIALSet", false)
                addDefiningQuery(zEAMCADDMATERIALSetQuery)
                val zEAMCWOOPERDOCUMENTSSetQuery = OfflineODataDefiningQuery("ZEAM_C_WO_OPER_DOCUMENTSSet", "ZEAM_C_WO_OPER_DOCUMENTSSet", false)
                addDefiningQuery(zEAMCWOOPERDOCUMENTSSetQuery)
                val zEAMCWORKORDERLISTSetQuery = OfflineODataDefiningQuery("ZEAM_C_WORK_ORDER_LISTSet", "ZEAM_C_WORK_ORDER_LISTSet", false)
                addDefiningQuery(zEAMCWORKORDERLISTSetQuery)
                val zEAMCWOSUBOPERATIONSLISTSetQuery = OfflineODataDefiningQuery("ZEAM_C_WO_SUB_OPERATIONS_LISTSet", "ZEAM_C_WO_SUB_OPERATIONS_LISTSet", false)
                addDefiningQuery(zEAMCWOSUBOPERATIONSLISTSetQuery)
                val zEAMIVarianceReasonQuery = OfflineODataDefiningQuery("ZEAM_I_VarianceReason", "ZEAM_I_VarianceReason", false)
                addDefiningQuery(zEAMIVarianceReasonQuery)
                val zeamCStartTimeQuery = OfflineODataDefiningQuery("ZEAM_C_START_TIME", "ZEAM_C_START_TIME", false)
                addDefiningQuery(zeamCStartTimeQuery)
                val zeamIGetAllAssignUsersQuery = OfflineODataDefiningQuery("ZEAM_I_GET_ALL_ASSIGN_USERS", "ZEAM_I_GET_ALL_ASSIGN_USERS", false)
                addDefiningQuery(zeamIGetAllAssignUsersQuery)
                val zeamIGetAssignUsersFrmWcQuery = OfflineODataDefiningQuery("ZEAM_I_GET_ASSIGN_USERS_FRM_WC", "ZEAM_I_GET_ASSIGN_USERS_FRM_WC", false)
                addDefiningQuery(zeamIGetAssignUsersFrmWcQuery)
                val zeamISubOrderQuery = OfflineODataDefiningQuery("ZEAM_I_SUB_ORDER", "ZEAM_I_SUB_ORDER", false)
                addDefiningQuery(zeamISubOrderQuery)
                val zeamIWoAttachmentShowQuery = OfflineODataDefiningQuery("ZEAM_I_WO_ATTACHMENT_SHOW", "ZEAM_I_WO_ATTACHMENT_SHOW", true)
                addDefiningQuery(zeamIWoAttachmentShowQuery)
                val zeamIWoComponentsQuery = OfflineODataDefiningQuery("ZEAM_I_WO_COMPONENTS", "ZEAM_I_WO_COMPONENTS", false)
                addDefiningQuery(zeamIWoComponentsQuery)
                val zeamIWoObjectListQuery = OfflineODataDefiningQuery("ZEAM_I_WO_OBJECT_LIST", "ZEAM_I_WO_OBJECT_LIST", false)
                addDefiningQuery(zeamIWoObjectListQuery)
                val zeamIWoOperDocumentsQuery = OfflineODataDefiningQuery("ZEAM_I_WO_OPER_DOCUMENTS", "ZEAM_I_WO_OPER_DOCUMENTS", true)
                addDefiningQuery(zeamIWoOperDocumentsQuery)
                val zeamIWoOperaConfDetQuery = OfflineODataDefiningQuery("ZEAM_I_WO_OPERA_CONF_DET", "ZEAM_I_WO_OPERA_CONF_DET", false)
                addDefiningQuery(zeamIWoOperaConfDetQuery)
                val zeamIWoOperationsListQuery = OfflineODataDefiningQuery("ZEAM_I_WO_OPERATIONS_LIST", "ZEAM_I_WO_OPERATIONS_LIST", false)
                addDefiningQuery(zeamIWoOperationsListQuery)
                zFIORI_EAM_APP_SRV_Entities = ZFIORI_EAM_APP_SRV_Entities(this)
            }
        } catch (e: Exception) {
            logger.error("Exception encountered setting up offline store: " + e.message)
        }
    }

    /*
     * Close and remove offline data store
     */
    @JvmStatic
    fun resetOffline(context: Context) {
        AndroidSystem.setContext(context)
        offlineODataProvider?.also {
            try {
                it.close()
            } catch (e: OfflineODataException) {
                logger.error("Unable to reset Offline Data Store. Encountered exception: " + e.message)
            } finally {
                offlineODataProvider = null
            }
        }
        OfflineODataProvider.clear(OFFLINE_DATASTORE)
        progressListeners.clear()
    }

    @JvmStatic
    fun open(context: Context) {
        if (FlowContextRegistry.flowContext.getCurrentUserId() == null) {
            error("Current user not ready yet.")
        }

        if (!userSwitchFlag && openRequest != null) {
            return
        }

        val constraints = Constraints.Builder()
            .setRequiresStorageNotLow(true)
            .build()

        openRequest = OneTimeWorkRequestBuilder<OfflineOpenWorker>()
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            OFFLINE_OPEN_WORKER_UNIQUE_NAME,
            ExistingWorkPolicy.KEEP,
            openRequest!!
        )
    }

    @JvmStatic
    fun sync(context: Context) {
        syncRequest?.let {
            return
        }

        val constraints = Constraints.Builder()
            .setRequiresStorageNotLow(true)
            .build()

        syncRequest = OneTimeWorkRequestBuilder<OfflineSyncWorker>()
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            OFFLINE_SYNC_WORKER_UNIQUE_NAME,
            ExistingWorkPolicy.KEEP,
            syncRequest!!
        )
    }
}


abstract class OfflineProgressListener() {
    enum class WorkerType{
        OPEN, SYNC
    }

    private var previousStep = 0
    val totalStepsForTwoProgresses = 40

    fun onOfflineProgress(
        provider: OfflineODataProvider,
        progress: OfflineODataProviderOperationProgress
    ) {
        if (progress.currentStepNumber > previousStep) {
            previousStep = progress.currentStepNumber
            if (workerType == WorkerType.OPEN && !OfflineWorkerUtil.userSwitchFlag) {
                updateProgress(progress.currentStepNumber, progress.totalNumberOfSteps)
            } else {
                /*
                 * The half of totalStepsForTwoProgresses is for first progress, the other half is for second progress.
                 * To make two progresses as one progress, the current step number needs to be calculated.
                 * For example, totalStepsForTwoProgresses is 40, then first progress will proceed from step 0 to step 20, and the second one will proceed from step 20 to step 40.
                 * So getStartPoint will be 0 for the first progress and 20 for the second progress.
                 * If first progress completes by 20% (i.e. getCurrentStepNumber / getTotalNumberOfSteps = 20%), the overall progress will be 4/40.
                 * If second progress completes by 20%, the overall progress will be 24/40.
                 */
                val currentStepNumber = totalStepsForTwoProgresses / 2 * progress.currentStepNumber / progress.totalNumberOfSteps + getStartPoint()
                updateProgress(currentStepNumber, totalStepsForTwoProgresses)
            }
        }
        if (progress.currentStepNumber == progress.totalNumberOfSteps) {
            previousStep = 0
        }
    }

    abstract fun updateProgress(currentStep: Int, totalSteps: Int)
    abstract fun getStartPoint(): Int
    abstract val workerType: WorkerType
}
