package com.akso.realwear.app.repository

import com.akso.realwear.app.repository.Repository
import com.sap.cloud.android.odata.zfiori_eam_app_srv_entities.ZFIORI_EAM_APP_SRV_EntitiesMetadata.EntitySets
import com.sap.cloud.android.odata.zfiori_eam_app_srv_entities.ZEAMCADDMATERIALParameters
import com.sap.cloud.android.odata.zfiori_eam_app_srv_entities.CStsObjActiveStatusCodeTextType
import com.sap.cloud.android.odata.zfiori_eam_app_srv_entities.CheckAppsAccess
import com.sap.cloud.android.odata.zfiori_eam_app_srv_entities.EquipmentList
import com.sap.cloud.android.odata.zfiori_eam_app_srv_entities.ICustomerVHType
import com.sap.cloud.android.odata.zfiori_eam_app_srv_entities.IFunctionalLocationStdVHType
import com.sap.cloud.android.odata.zfiori_eam_app_srv_entities.IMaintActyTypeStdVHType
import com.sap.cloud.android.odata.zfiori_eam_app_srv_entities.IMaintPlnrGrpStdVHType
import com.sap.cloud.android.odata.zfiori_eam_app_srv_entities.IMaintenanceRevisionStdVHType
import com.sap.cloud.android.odata.zfiori_eam_app_srv_entities.IMaterialStdVHType
import com.sap.cloud.android.odata.zfiori_eam_app_srv_entities.IPersWrkAgrmtSrchHelpType
import com.sap.cloud.android.odata.zfiori_eam_app_srv_entities.IPlantStdVHType
import com.sap.cloud.android.odata.zfiori_eam_app_srv_entities.ISalesOrderItemStdVHType
import com.sap.cloud.android.odata.zfiori_eam_app_srv_entities.ISalesOrderStdVHType
import com.sap.cloud.android.odata.zfiori_eam_app_srv_entities.IStorageLocationType
import com.sap.cloud.android.odata.zfiori_eam_app_srv_entities.IWrkCtrBySemanticKeyStdVHType
import com.sap.cloud.android.odata.zfiori_eam_app_srv_entities.Locations
import com.sap.cloud.android.odata.zfiori_eam_app_srv_entities.LoginUserCheck
import com.sap.cloud.android.odata.zfiori_eam_app_srv_entities.LongTextGet
import com.sap.cloud.android.odata.zfiori_eam_app_srv_entities.MessageReturn
import com.sap.cloud.android.odata.zfiori_eam_app_srv_entities.UserContact
import com.sap.cloud.android.odata.zfiori_eam_app_srv_entities.ZEAMCWOSUBOPERATIONSLISTParameters
import com.sap.cloud.android.odata.zfiori_eam_app_srv_entities.WOTileCounter
import com.sap.cloud.android.odata.zfiori_eam_app_srv_entities.ZEAMCWOOPERDOCUMENTSParameters
import com.sap.cloud.android.odata.zfiori_eam_app_srv_entities.WorkOrderDetail
import com.sap.cloud.android.odata.zfiori_eam_app_srv_entities.ZEAMCWORKORDERLISTParameters
import com.sap.cloud.android.odata.zfiori_eam_app_srv_entities.WorkOrderOperDetail
import com.sap.cloud.android.odata.zfiori_eam_app_srv_entities.AddMaterial
import com.sap.cloud.android.odata.zfiori_eam_app_srv_entities.WorkOrdSubOperDoc
import com.sap.cloud.android.odata.zfiori_eam_app_srv_entities.WorkOrderList
import com.sap.cloud.android.odata.zfiori_eam_app_srv_entities.WOSubOperaList
import com.sap.cloud.android.odata.zfiori_eam_app_srv_entities.ZEAMIVarianceReasonType
import com.sap.cloud.android.odata.zfiori_eam_app_srv_entities.ZEAMCSTARTTIMEType
import com.sap.cloud.android.odata.zfiori_eam_app_srv_entities.ZEAMIGETALLASSIGNUSERSType
import com.sap.cloud.android.odata.zfiori_eam_app_srv_entities.ZEAMIGETASSIGNUSERSFRMWCType
import com.sap.cloud.android.odata.zfiori_eam_app_srv_entities.ZEAMISUBORDERType
import com.sap.cloud.android.odata.zfiori_eam_app_srv_entities.ZEAMIWOATTACHMENTSHOWType
import com.sap.cloud.android.odata.zfiori_eam_app_srv_entities.ZEAMIWOCOMPONENTSType
import com.sap.cloud.android.odata.zfiori_eam_app_srv_entities.ZEAMIWOOBJECTLISTType
import com.sap.cloud.android.odata.zfiori_eam_app_srv_entities.ZEAMIWOOPERDOCUMENTSType
import com.sap.cloud.android.odata.zfiori_eam_app_srv_entities.ZEAMIWOOPERACONFDETType
import com.sap.cloud.android.odata.zfiori_eam_app_srv_entities.ZEAMIWOOPERATIONSLISTType

import com.sap.cloud.mobile.odata.EntitySet
import com.sap.cloud.mobile.odata.EntityValue
import com.sap.cloud.mobile.odata.Property
import com.akso.realwear.service.OfflineWorkerUtil

import java.util.WeakHashMap

/*
 * Repository factory to construct repository for an entity set
 */
class RepositoryFactory
/**
 * Construct a RepositoryFactory instance. There should only be one repository factory and used
 * throughout the life of the application to avoid caching entities multiple times.
 */
{
    private val repositories: WeakHashMap<String, Repository<out EntityValue>> = WeakHashMap()

    /**
     * Construct or return an existing repository for the specified entity set
     * @param entitySet - entity set for which the repository is to be returned
     * @param orderByProperty - if specified, collection will be sorted ascending with this property
     * @return a repository for the entity set
     */
    fun getRepository(entitySet: EntitySet, orderByProperty: Property?): Repository<out EntityValue> {
        val zFIORI_EAM_APP_SRV_Entities = OfflineWorkerUtil.zFIORI_EAM_APP_SRV_Entities
        val key = entitySet.localName
        var repository: Repository<out EntityValue>? = repositories[key]
        if (repository == null) {
            repository = when (key) {
                EntitySets.addMaterialSet.localName -> Repository<ZEAMCADDMATERIALParameters>(zFIORI_EAM_APP_SRV_Entities, EntitySets.addMaterialSet, orderByProperty)
                EntitySets.cStsObjActiveStatusCodeText.localName -> Repository<CStsObjActiveStatusCodeTextType>(zFIORI_EAM_APP_SRV_Entities, EntitySets.cStsObjActiveStatusCodeText, orderByProperty)
                EntitySets.checkAppsAccessSet.localName -> Repository<CheckAppsAccess>(zFIORI_EAM_APP_SRV_Entities, EntitySets.checkAppsAccessSet, orderByProperty)
                EntitySets.equipmentListSet.localName -> Repository<EquipmentList>(zFIORI_EAM_APP_SRV_Entities, EntitySets.equipmentListSet, orderByProperty)
                EntitySets.iCustomerVH.localName -> Repository<ICustomerVHType>(zFIORI_EAM_APP_SRV_Entities, EntitySets.iCustomerVH, orderByProperty)
                EntitySets.iFunctionalLocationStdVH.localName -> Repository<IFunctionalLocationStdVHType>(zFIORI_EAM_APP_SRV_Entities, EntitySets.iFunctionalLocationStdVH, orderByProperty)
                EntitySets.iMaintActyTypeStdVH.localName -> Repository<IMaintActyTypeStdVHType>(zFIORI_EAM_APP_SRV_Entities, EntitySets.iMaintActyTypeStdVH, orderByProperty)
                EntitySets.iMaintPlnrGrpStdVH.localName -> Repository<IMaintPlnrGrpStdVHType>(zFIORI_EAM_APP_SRV_Entities, EntitySets.iMaintPlnrGrpStdVH, orderByProperty)
                EntitySets.iMaintenanceRevisionStdVH.localName -> Repository<IMaintenanceRevisionStdVHType>(zFIORI_EAM_APP_SRV_Entities, EntitySets.iMaintenanceRevisionStdVH, orderByProperty)
                EntitySets.iMaterialStdVH.localName -> Repository<IMaterialStdVHType>(zFIORI_EAM_APP_SRV_Entities, EntitySets.iMaterialStdVH, orderByProperty)
                EntitySets.iPersWrkAgrmtSrchHelp.localName -> Repository<IPersWrkAgrmtSrchHelpType>(zFIORI_EAM_APP_SRV_Entities, EntitySets.iPersWrkAgrmtSrchHelp, orderByProperty)
                EntitySets.iPlantStdVH.localName -> Repository<IPlantStdVHType>(zFIORI_EAM_APP_SRV_Entities, EntitySets.iPlantStdVH, orderByProperty)
                EntitySets.iSalesOrderItemStdVH.localName -> Repository<ISalesOrderItemStdVHType>(zFIORI_EAM_APP_SRV_Entities, EntitySets.iSalesOrderItemStdVH, orderByProperty)
                EntitySets.iSalesOrderStdVH.localName -> Repository<ISalesOrderStdVHType>(zFIORI_EAM_APP_SRV_Entities, EntitySets.iSalesOrderStdVH, orderByProperty)
                EntitySets.iStorageLocation.localName -> Repository<IStorageLocationType>(zFIORI_EAM_APP_SRV_Entities, EntitySets.iStorageLocation, orderByProperty)
                EntitySets.iWrkCtrBySemanticKeyStdVH.localName -> Repository<IWrkCtrBySemanticKeyStdVHType>(zFIORI_EAM_APP_SRV_Entities, EntitySets.iWrkCtrBySemanticKeyStdVH, orderByProperty)
                EntitySets.locationsSet.localName -> Repository<Locations>(zFIORI_EAM_APP_SRV_Entities, EntitySets.locationsSet, orderByProperty)
                EntitySets.loginUserCheckSet.localName -> Repository<LoginUserCheck>(zFIORI_EAM_APP_SRV_Entities, EntitySets.loginUserCheckSet, orderByProperty)
                EntitySets.longTextGetSet.localName -> Repository<LongTextGet>(zFIORI_EAM_APP_SRV_Entities, EntitySets.longTextGetSet, orderByProperty)
                EntitySets.messageReturnSet.localName -> Repository<MessageReturn>(zFIORI_EAM_APP_SRV_Entities, EntitySets.messageReturnSet, orderByProperty)
                EntitySets.messagesReturnSet.localName -> Repository<MessageReturn>(zFIORI_EAM_APP_SRV_Entities, EntitySets.messagesReturnSet, orderByProperty)
                EntitySets.userContactSet.localName -> Repository<UserContact>(zFIORI_EAM_APP_SRV_Entities, EntitySets.userContactSet, orderByProperty)
                EntitySets.woSubOperaListSet.localName -> Repository<ZEAMCWOSUBOPERATIONSLISTParameters>(zFIORI_EAM_APP_SRV_Entities, EntitySets.woSubOperaListSet, orderByProperty)
                EntitySets.woTileCounterSet.localName -> Repository<WOTileCounter>(zFIORI_EAM_APP_SRV_Entities, EntitySets.woTileCounterSet, orderByProperty)
                EntitySets.workOrdSubOperDocSet.localName -> Repository<ZEAMCWOOPERDOCUMENTSParameters>(zFIORI_EAM_APP_SRV_Entities, EntitySets.workOrdSubOperDocSet, orderByProperty)
                EntitySets.workOrderDetailSet.localName -> Repository<WorkOrderDetail>(zFIORI_EAM_APP_SRV_Entities, EntitySets.workOrderDetailSet, orderByProperty)
                EntitySets.workOrderListSet.localName -> Repository<ZEAMCWORKORDERLISTParameters>(zFIORI_EAM_APP_SRV_Entities, EntitySets.workOrderListSet, orderByProperty)
                EntitySets.workOrderOperDetailSet.localName -> Repository<WorkOrderOperDetail>(zFIORI_EAM_APP_SRV_Entities, EntitySets.workOrderOperDetailSet, orderByProperty)
                EntitySets.zeamCADDMATERIALSet.localName -> Repository<AddMaterial>(zFIORI_EAM_APP_SRV_Entities, EntitySets.zeamCADDMATERIALSet, orderByProperty)
                EntitySets.zeamCWOOPERDOCUMENTSSet.localName -> Repository<WorkOrdSubOperDoc>(zFIORI_EAM_APP_SRV_Entities, EntitySets.zeamCWOOPERDOCUMENTSSet, orderByProperty)
                EntitySets.zeamCWORKORDERLISTSet.localName -> Repository<WorkOrderList>(zFIORI_EAM_APP_SRV_Entities, EntitySets.zeamCWORKORDERLISTSet, orderByProperty)
                EntitySets.zeamCWOSUBOPERATIONSLISTSet.localName -> Repository<WOSubOperaList>(zFIORI_EAM_APP_SRV_Entities, EntitySets.zeamCWOSUBOPERATIONSLISTSet, orderByProperty)
                EntitySets.zeamIVarianceReason.localName -> Repository<ZEAMIVarianceReasonType>(zFIORI_EAM_APP_SRV_Entities, EntitySets.zeamIVarianceReason, orderByProperty)
                EntitySets.zeamCStartTime.localName -> Repository<ZEAMCSTARTTIMEType>(zFIORI_EAM_APP_SRV_Entities, EntitySets.zeamCStartTime, orderByProperty)
                EntitySets.zeamIGetAllAssignUsers.localName -> Repository<ZEAMIGETALLASSIGNUSERSType>(zFIORI_EAM_APP_SRV_Entities, EntitySets.zeamIGetAllAssignUsers, orderByProperty)
                EntitySets.zeamIGetAssignUsersFrmWc.localName -> Repository<ZEAMIGETASSIGNUSERSFRMWCType>(zFIORI_EAM_APP_SRV_Entities, EntitySets.zeamIGetAssignUsersFrmWc, orderByProperty)
                EntitySets.zeamISubOrder.localName -> Repository<ZEAMISUBORDERType>(zFIORI_EAM_APP_SRV_Entities, EntitySets.zeamISubOrder, orderByProperty)
                EntitySets.zeamIWoAttachmentShow.localName -> Repository<ZEAMIWOATTACHMENTSHOWType>(zFIORI_EAM_APP_SRV_Entities, EntitySets.zeamIWoAttachmentShow, orderByProperty)
                EntitySets.zeamIWoComponents.localName -> Repository<ZEAMIWOCOMPONENTSType>(zFIORI_EAM_APP_SRV_Entities, EntitySets.zeamIWoComponents, orderByProperty)
                EntitySets.zeamIWoObjectList.localName -> Repository<ZEAMIWOOBJECTLISTType>(zFIORI_EAM_APP_SRV_Entities, EntitySets.zeamIWoObjectList, orderByProperty)
                EntitySets.zeamIWoOperDocuments.localName -> Repository<ZEAMIWOOPERDOCUMENTSType>(zFIORI_EAM_APP_SRV_Entities, EntitySets.zeamIWoOperDocuments, orderByProperty)
                EntitySets.zeamIWoOperaConfDet.localName -> Repository<ZEAMIWOOPERACONFDETType>(zFIORI_EAM_APP_SRV_Entities, EntitySets.zeamIWoOperaConfDet, orderByProperty)
                EntitySets.zeamIWoOperationsList.localName -> Repository<ZEAMIWOOPERATIONSLISTType>(zFIORI_EAM_APP_SRV_Entities, EntitySets.zeamIWoOperationsList, orderByProperty)
                else -> throw AssertionError("Fatal error, entity set[$key] missing in generated code")
            }
            repositories[key] = repository
        }
        return repository
    }

    /**
     * Get rid of all cached repositories
     */
    fun reset() {
        repositories.clear()
    }
}
