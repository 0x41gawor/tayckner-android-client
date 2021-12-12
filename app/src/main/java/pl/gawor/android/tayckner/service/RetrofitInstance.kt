package pl.gawor.android.tayckner.service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/api/users/")
//            .baseUrl("http://192.168.1.149:8080/api/users/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}