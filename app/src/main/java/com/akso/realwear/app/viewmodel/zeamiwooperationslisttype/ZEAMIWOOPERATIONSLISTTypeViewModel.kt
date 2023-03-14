package com.akso.realwear.app.viewmodel.zeamiwooperationslisttype

import android.app.Application
import android.os.Parcelable

import com.akso.realwear.app.viewmodel.EntityViewModel
import com.sap.cloud.android.odata.zfiori_eam_app_srv_entities.ZEAMIWOOPERATIONSLISTType
import com.sap.cloud.android.odata.zfiori_eam_app_srv_entities.ZFIORI_EAM_APP_SRV_EntitiesMetadata.EntitySets

/*
 * Represents View model for ZEAMIWOOPERATIONSLISTType
 *
 * Having an entity view model for each <T> allows the ViewModelProvider to cache and return the view model of that
 * type. This is because the ViewModelStore of ViewModelProvider cannot not be able to tell the difference between
 * EntityViewModel<type1> and EntityViewModel<type2>.
 */
class ZEAMIWOOPERATIONSLISTTypeViewModel(application: Application): EntityViewModel<ZEAMIWOOPERATIONSLISTType>(application, EntitySets.zeamIWoOperationsList, ZEAMIWOOPERATIONSLISTType.maintOrderRoutingNumber) {
    /**
     * Constructor for a specific view model with navigation data.
     * @param [navigationPropertyName] - name of the navigation property
     * @param [entityData] - parent entity (starting point of the navigation)
     */
    constructor(application: Application, navigationPropertyName: String, entityData: Parcelable): this(application) {
        EntityViewModel<ZEAMIWOOPERATIONSLISTType>(application, EntitySets.zeamIWoOperationsList, ZEAMIWOOPERATIONSLISTType.maintOrderRoutingNumber, navigationPropertyName, entityData)
    }
}
