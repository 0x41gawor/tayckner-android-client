package pl.gawor.android.tayckner.service

import pl.gawor.android.tayckner.model.HabitEvent
import pl.gawor.android.tayckner.model.ResponseModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface HabitEventApi {
    @GET("habit-events")
    suspend fun list(@Header("Authorization") auth: String): Response<ResponseModel<List<HabitEvent>>>

    @POST("habit-events")
    suspend fun create(@Header("Authorization") auth: String, @Body habitEvent: HabitEvent): Response<ResponseModel<HabitEvent>>
}