package pl.gawor.android.tayckner.service

import pl.gawor.android.tayckner.model.HabitEvent
import pl.gawor.android.tayckner.model.ResponseModel
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.Body
import retrofit2.http.Path
import retrofit2.http.PUT
import retrofit2.http.POST
import retrofit2.http.GET

interface HabitEventApi {
    @GET("habit-events")
    suspend fun list(@Header("Authorization") auth: String): Response<ResponseModel<List<HabitEvent>>>

    @POST("habit-events")
    suspend fun create(@Header("Authorization") auth: String, @Body habitEvent: HabitEvent): Response<ResponseModel<HabitEvent>>

    @PUT("habit-events/{id}")
    fun update(@Header("Authorization") auth: String, @Body habitEvent: HabitEvent, @Path("id") id: Int): Call<ResponseModel<HabitEvent>>

    @GET("habit-events")
    fun listCall(@Header("Authorization") auth: String): Call<ResponseModel<List<HabitEvent>>>
}