package com.akso.realwear.app.app.callAPI

import android.database.Observable
import com.akso.realwear.app.app.timeLog.TimeLogData
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*


interface ApiInterface {

    //@GET("WorkOrderOperDetailSet(OrderNumber='4627105',MaintOrderRoutingNumber='1001278026',MaintOrderOperationCounter='00000002')/to_WorkOrderOperConf")
    fun ISendReq(@Body requestModel: RequestModel) :
        Call<TimeLogData>

    @GET("WorkOrderOperDetailSet(OrderNumber='4627105',MaintOrderRoutingNumber='1001278026',MaintOrderOperationCounter='00000002')/to_WorkOrderOperConf")
    fun IGetTimeLog() : Call<TimelogTest>

    @Headers("slug: test080220232455025957364563105.jpg;4636657")
    @POST("ZEAM_I_WO_ATTACHMENT_SHOW")
    fun IUploadImage(@Body body: Map<String, Any>): Call<ResponseBody>

}