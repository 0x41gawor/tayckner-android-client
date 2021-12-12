package pl.gawor.android.tayckner.service

import pl.gawor.android.tayckner.model.ResponseModel
import pl.gawor.android.tayckner.model.UserCredentialsModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApi {

    @POST("login")
    fun login(@Body credentialsModel: UserCredentialsModel): Call<ResponseModel>
}