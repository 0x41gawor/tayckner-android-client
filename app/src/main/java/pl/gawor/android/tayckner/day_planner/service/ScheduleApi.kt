package pl.gawor.android.tayckner.day_planner.service

import pl.gawor.android.tayckner.common.model.ResponseModel
import pl.gawor.android.tayckner.day_planner.model.Schedule
import retrofit2.Response
import retrofit2.http.*

interface ScheduleApi {
    @GET("schedules")
    suspend fun list(@Header("Authorization") auth: String): Response<ResponseModel<List<Schedule>>>

    @POST("schedules")
    suspend fun create(@Header("Authorization") auth: String, @Body schedule: Schedule): Response<ResponseModel<Schedule>>

    @PUT("schedules/{id}")
    suspend fun update(@Header("Authorization") auth: String, @Body schedule: Schedule, @Path("id") id: Int): Response<ResponseModel<Schedule>>

    @DELETE("schedules/{id}")
    suspend fun delete(@Header("Authorization") auth: String, @Path("id") id: Int): Response<ResponseModel<Any>>
}