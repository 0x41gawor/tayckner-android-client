package pl.gawor.android.tayckner.day_tracker.service

import pl.gawor.android.tayckner.common.model.ResponseModel
import pl.gawor.android.tayckner.day_tracker.model.Activity
import retrofit2.Response
import retrofit2.http.*

interface ActivityApi {
    @GET("activities")
    suspend fun list(@Header("Authorization") auth: String): Response<ResponseModel<List<Activity>>>

    @POST("activities")
    suspend fun create(@Header("Authorization") auth: String, @Body activity: Activity): Response<ResponseModel<Activity>>

    @PUT("activities/{id}")
    suspend fun update(@Header("Authorization") auth: String, @Body activity: Activity, @Path("id") id: Int): Response<ResponseModel<Activity>>

    @DELETE("activities/{id}")
    suspend fun delete(@Header("Authorization") auth: String, @Path("id") id: Int): Response<ResponseModel<Any>>
}