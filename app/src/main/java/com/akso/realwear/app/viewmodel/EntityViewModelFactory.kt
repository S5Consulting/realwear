package com.akso.realwear.app.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import android.os.Parcelable
import com.akso.realwear.app.viewmodel.addmaterial.AddMaterialViewModel
import com.akso.realwear.app.viewmodel.checkappsaccess.CheckAppsAccessViewModel
import com.akso.realwear.app.viewmodel.cstsobjactivestatuscodetexttype.CStsObjActiveStatusCodeTextTypeViewModel
import com.akso.realwear.app.viewmodel.equipmentlist.EquipmentListViewModel
import com.akso.realwear.app.viewmodel.icustomervhtype.ICustomerVHTypeViewModel
import com.akso.realwear.app.viewmodel.ifunctionallocationstdvhtype.IFunctionalLocationStdVHTypeViewModel
import com.akso.realwear.app.viewmodel.imaintactytypestdvhtype.IMaintActyTypeStdVHTypeViewModel
import com.akso.realwear.app.viewmodel.imaintenancerevisionstdvhtype.IMaintenanceRevisionStdVHTypeViewModel
import com.akso.realwear.app.viewmodel.imaintplnrgrpstdvhtype.IMaintPlnrGrpStdVHTypeViewModel
import com.akso.realwear.app.viewmodel.imaterialstdvhtype.IMaterialStdVHTypeViewModel
import com.akso.realwear.app.viewmodel.iperswrkagrmtsrchhelptype.IPersWrkAgrmtSrchHelpTypeViewModel
import com.akso.realwear.app.viewmodel.iplantstdvhtype.IPlantStdVHTypeViewModel
import com.akso.realwear.app.viewmodel.isalesorderitemstdvhtype.ISalesOrderItemStdVHTypeViewModel
import com.akso.realwear.app.viewmodel.isalesorderstdvhtype.ISalesOrderStdVHTypeViewModel
import com.akso.realwear.app.viewmodel.istoragelocationtype.IStorageLocationTypeViewModel
import com.akso.realwear.app.viewmodel.iwrkctrbysemantickeystdvhtype.IWrkCtrBySemanticKeyStdVHTypeViewModel
import com.akso.realwear.app.viewmodel.locations.LocationsViewModel
import com.akso.realwear.app.viewmodel.loginusercheck.LoginUserCheckViewModel
import com.akso.realwear.app.viewmodel.longtextget.LongTextGetViewModel
import com.akso.realwear.app.viewmodel.messagereturn.MessageReturnViewModel
import com.akso.realwear.app.viewmodel.usercontact.UserContactViewModel
import com.akso.realwear.app.viewmodel.workorderdetail.WorkOrderDetailViewModel
import com.akso.realwear.app.viewmodel.workorderlist.WorkOrderListViewModel
import com.akso.realwear.app.viewmodel.workorderoperdetail.WorkOrderOperDetailViewModel
import com.akso.realwear.app.viewmodel.workordsuboperdoc.WorkOrdSubOperDocViewModel
import com.akso.realwear.app.viewmodel.wosuboperalist.WOSubOperaListViewModel
import com.akso.realwear.app.viewmodel.wotilecounter.WOTileCounterViewModel
import com.akso.realwear.app.viewmodel.zeamcaddmaterialparameters.ZEAMCADDMATERIALParametersViewModel
import com.akso.realwear.app.viewmodel.zeamcstarttimetype.ZEAMCSTARTTIMETypeViewModel
import com.akso.realwear.app.viewmodel.zeamcwooperdocumentsparameters.ZEAMCWOOPERDOCUMENTSParametersViewModel
import com.akso.realwear.app.viewmodel.zeamcworkorderlistparameters.ZEAMCWORKORDERLISTParametersViewModel
import com.akso.realwear.app.viewmodel.zeamcwosuboperationslistparameters.ZEAMCWOSUBOPERATIONSLISTParametersViewModel
import com.akso.realwear.app.viewmodel.zeamigetallassignuserstype.ZEAMIGETALLASSIGNUSERSTypeViewModel
import com.akso.realwear.app.viewmodel.zeamigetassignusersfrmwctype.ZEAMIGETASSIGNUSERSFRMWCTypeViewModel
import com.akso.realwear.app.viewmodel.zeamisubordertype.ZEAMISUBORDERTypeViewModel
import com.akso.realwear.app.viewmodel.zeamivariancereasontype.ZEAMIVarianceReasonTypeViewModel
import com.akso.realwear.app.viewmodel.zeamiwoattachmentshowtype.ZEAMIWOATTACHMENTSHOWTypeViewModel
import com.akso.realwear.app.viewmodel.zeamiwocomponentstype.ZEAMIWOCOMPONENTSTypeViewModel
import com.akso.realwear.app.viewmodel.zeamiwoobjectlisttype.ZEAMIWOOBJECTLISTTypeViewModel
import com.akso.realwear.app.viewmodel.zeamiwooperaconfdettype.ZEAMIWOOPERACONFDETTypeViewModel
import com.akso.realwear.app.viewmodel.zeamiwooperationslisttype.ZEAMIWOOPERATIONSLISTTypeViewModel
import com.akso.realwear.app.viewmodel.zeamiwooperdocumentstype.ZEAMIWOOPERDOCUMENTSTypeViewModel


/**
 * Custom factory class, which can create view models for entity subsets, which are
 * reached from a parent entity through a navigation property.
 *
 * @param application parent application
 * @param navigationPropertyName name of the navigation link
 * @param entityData parent entity
 */
class EntityViewModelFactory (
        val application: Application, // name of the navigation property
        val navigationPropertyName: String, // parent entity
        val entityData: Parcelable) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass.simpleName) {
			"ZEAMCADDMATERIALParametersViewModel" -> ZEAMCADDMATERIALParametersViewModel(application, navigationPropertyName, entityData) as T
            			"CStsObjActiveStatusCodeTextTypeViewModel" -> CStsObjActiveStatusCodeTextTypeViewModel(application, navigationPropertyName, entityData) as T
            			"CheckAppsAccessViewModel" -> CheckAppsAccessViewModel(application, navigationPropertyName, entityData) as T
            			"EquipmentListViewModel" -> EquipmentListViewModel(application, navigationPropertyName, entityData) as T
            			"ICustomerVHTypeViewModel" -> ICustomerVHTypeViewModel(application, navigationPropertyName, entityData) as T
            			"IFunctionalLocationStdVHTypeViewModel" -> IFunctionalLocationStdVHTypeViewModel(application, navigationPropertyName, entityData) as T
            			"IMaintActyTypeStdVHTypeViewModel" -> IMaintActyTypeStdVHTypeViewModel(application, navigationPropertyName, entityData) as T
            			"IMaintPlnrGrpStdVHTypeViewModel" -> IMaintPlnrGrpStdVHTypeViewModel(application, navigationPropertyName, entityData) as T
            			"IMaintenanceRevisionStdVHTypeViewModel" -> IMaintenanceRevisionStdVHTypeViewModel(application, navigationPropertyName, entityData) as T
            			"IMaterialStdVHTypeViewModel" -> IMaterialStdVHTypeViewModel(application, navigationPropertyName, entityData) as T
            			"IPersWrkAgrmtSrchHelpTypeViewModel" -> IPersWrkAgrmtSrchHelpTypeViewModel(application, navigationPropertyName, entityData) as T
            			"IPlantStdVHTypeViewModel" -> IPlantStdVHTypeViewModel(application, navigationPropertyName, entityData) as T
            			"ISalesOrderItemStdVHTypeViewModel" -> ISalesOrderItemStdVHTypeViewModel(application, navigationPropertyName, entityData) as T
            			"ISalesOrderStdVHTypeViewModel" -> ISalesOrderStdVHTypeViewModel(application, navigationPropertyName, entityData) as T
            			"IStorageLocationTypeViewModel" -> IStorageLocationTypeViewModel(application, navigationPropertyName, entityData) as T
            			"IWrkCtrBySemanticKeyStdVHTypeViewModel" -> IWrkCtrBySemanticKeyStdVHTypeViewModel(application, navigationPropertyName, entityData) as T
            			"LocationsViewModel" -> LocationsViewModel(application, navigationPropertyName, entityData) as T
            			"LoginUserCheckViewModel" -> LoginUserCheckViewModel(application, navigationPropertyName, entityData) as T
            			"LongTextGetViewModel" -> LongTextGetViewModel(application, navigationPropertyName, entityData) as T
            			"MessageReturnViewModel" -> MessageReturnViewModel(application, navigationPropertyName, entityData) as T
            			"UserContactViewModel" -> UserContactViewModel(application, navigationPropertyName, entityData) as T
            			"ZEAMCWOSUBOPERATIONSLISTParametersViewModel" -> ZEAMCWOSUBOPERATIONSLISTParametersViewModel(application, navigationPropertyName, entityData) as T
            			"WOTileCounterViewModel" -> WOTileCounterViewModel(application, navigationPropertyName, entityData) as T
            			"ZEAMCWOOPERDOCUMENTSParametersViewModel" -> ZEAMCWOOPERDOCUMENTSParametersViewModel(application, navigationPropertyName, entityData) as T
            			"WorkOrderDetailViewModel" -> WorkOrderDetailViewModel(application, navigationPropertyName, entityData) as T
            			"ZEAMCWORKORDERLISTParametersViewModel" -> ZEAMCWORKORDERLISTParametersViewModel(application, navigationPropertyName, entityData) as T
            			"WorkOrderOperDetailViewModel" -> WorkOrderOperDetailViewModel(application, navigationPropertyName, entityData) as T
            			"AddMaterialViewModel" -> AddMaterialViewModel(application, navigationPropertyName, entityData) as T
            			"WorkOrdSubOperDocViewModel" -> WorkOrdSubOperDocViewModel(application, navigationPropertyName, entityData) as T
            			"WorkOrderListViewModel" -> WorkOrderListViewModel(application, navigationPropertyName, entityData) as T
            			"WOSubOperaListViewModel" -> WOSubOperaListViewModel(application, navigationPropertyName, entityData) as T
            			"ZEAMIVarianceReasonTypeViewModel" -> ZEAMIVarianceReasonTypeViewModel(application, navigationPropertyName, entityData) as T
            			"ZEAMCSTARTTIMETypeViewModel" -> ZEAMCSTARTTIMETypeViewModel(application, navigationPropertyName, entityData) as T
            			"ZEAMIGETALLASSIGNUSERSTypeViewModel" -> ZEAMIGETALLASSIGNUSERSTypeViewModel(application, navigationPropertyName, entityData) as T
            			"ZEAMIGETASSIGNUSERSFRMWCTypeViewModel" -> ZEAMIGETASSIGNUSERSFRMWCTypeViewModel(application, navigationPropertyName, entityData) as T
            			"ZEAMISUBORDERTypeViewModel" -> ZEAMISUBORDERTypeViewModel(application, navigationPropertyName, entityData) as T
            			"ZEAMIWOATTACHMENTSHOWTypeViewModel" -> ZEAMIWOATTACHMENTSHOWTypeViewModel(application, navigationPropertyName, entityData) as T
            			"ZEAMIWOCOMPONENTSTypeViewModel" -> ZEAMIWOCOMPONENTSTypeViewModel(application, navigationPropertyName, entityData) as T
            			"ZEAMIWOOBJECTLISTTypeViewModel" -> ZEAMIWOOBJECTLISTTypeViewModel(application, navigationPropertyName, entityData) as T
            			"ZEAMIWOOPERDOCUMENTSTypeViewModel" -> ZEAMIWOOPERDOCUMENTSTypeViewModel(application, navigationPropertyName, entityData) as T
            			"ZEAMIWOOPERACONFDETTypeViewModel" -> ZEAMIWOOPERACONFDETTypeViewModel(application, navigationPropertyName, entityData) as T
             else -> ZEAMIWOOPERATIONSLISTTypeViewModel(application, navigationPropertyName, entityData) as T
        }
    }
}
