package pl.gawor.android.tayckner.service

import pl.gawor.android.tayckner.model.ResponseModel
import pl.gawor.android.tayckner.model.CredentialsModel
import pl.gawor.android.tayckner.model.UserModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApi {

    @POST("login")
    fun login(@Body credentialsModel: CredentialsModel): Call<ResponseModel>

    @POST("register")
    fun register(@Body user: UserModel): Call<ResponseModel>
}