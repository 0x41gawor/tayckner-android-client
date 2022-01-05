package pl.gawor.android.tayckner.day_tracker.service

import pl.gawor.android.tayckner.common.model.ResponseModel
import pl.gawor.android.tayckner.day_tracker.model.Category
import retrofit2.Response
import retrofit2.http.*

interface CategoryApi {
    @GET("categories")
    suspend fun list(@Header("Authorization") auth: String): Response<ResponseModel<List<Category>>>

    @POST("categories")
    suspend fun create(@Header("Authorization") auth: String, @Body category: Category): Response<ResponseModel<Category>>

    @PUT("categories/{id}")
    suspend fun update(@Header("Authorization") auth: String, @Body category: Category, @Path("id") id: Int): Response<ResponseModel<Category>>

    @DELETE("categories/{id}")
    suspend fun delete(@Header("Authorization") auth: String, @Path("id") id: Int): Response<ResponseModel<Any>>
}