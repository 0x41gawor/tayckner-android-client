package pl.gawor.android.tayckner.service

import pl.gawor.android.tayckner.model.HabitEvent
import pl.gawor.android.tayckner.model.ResponseModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface HabitEventApi {
    @GET("habit-events")
    suspend fun list(@Header("Authorization") auth: String): Response<ResponseModel<List<HabitEvent>>>
}