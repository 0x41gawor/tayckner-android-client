package pl.gawor.android.tayckner.habit_tracker.service

import pl.gawor.android.tayckner.habit_tracker.model.Habit
import pl.gawor.android.tayckner.habit_tracker.model.ResponseModel
import retrofit2.Response
import retrofit2.http.*

interface HabitApi {

    @GET("habits")
    suspend fun list(@Header("Authorization") auth: String): Response<ResponseModel<List<Habit>>>

    @POST("habits")
    suspend fun create(@Header("Authorization") auth: String, @Body habit: Habit): Response<ResponseModel<Habit>>

    @PUT("habits/{id}")
    suspend fun update(@Header("Authorization") auth: String, @Body habit: Habit, @Path("id") id: Int): Response<ResponseModel<Habit>>

    @DELETE("habits/{id}")
    suspend fun delete(@Header("Authorization") auth: String, @Path("id") id: Int): Response<ResponseModel<Any>>
}