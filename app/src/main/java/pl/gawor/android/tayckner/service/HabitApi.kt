package pl.gawor.android.tayckner.service

import pl.gawor.android.tayckner.model.Habit
import pl.gawor.android.tayckner.model.ResponseModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface HabitApi {

    @GET("habits")
    suspend fun list(@Header("Authorization") auth: String): Response<ResponseModel<List<Habit>>>

    @POST("habits")
    suspend fun create(@Header("Authorization") auth: String, @Body habit: Habit): Response<ResponseModel<Habit>>
}