package pl.gawor.android.tayckner.repository

import android.util.Log
import pl.gawor.android.tayckner.model.Habit
import pl.gawor.android.tayckner.model.ResponseModel
import pl.gawor.android.tayckner.service.HabitApi
import pl.gawor.android.tayckner.service.RetrofitInstance
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

object HabitRepository {

    private  val TAG = "TAYCKNER"

    suspend fun list(): List<Habit> {
        val result: List<Habit>
        val habitApiClient: HabitApi = RetrofitInstance.retrofit.create(HabitApi::class.java)
        val response: Response<ResponseModel<List<Habit>>> = try {
            habitApiClient.list(JWT_TOKEN)
        } catch (e: IOException) {
            Log.e(TAG, "HabitRepository.list:\t\tIOException: ${e.message}")
            return emptyList()
        } catch (e: HttpException) {
            Log.e(TAG, "HabitRepository.list:\t\tHttpException: ${e.message}")
            return emptyList()
        }
        if (response.isSuccessful && response.body() != null) {
            val res: ResponseModel<List<Habit>> = response.body()!!
            Log.e(TAG, "HabitRepository.list: $res")
            if (res.content != null)
            {
                result =  res.content
                return result
            }
        } else {
            Log.e(TAG, "HabitRepository.list: HTTP status != 200")
            return emptyList()
        }

        return emptyList()
    }
}