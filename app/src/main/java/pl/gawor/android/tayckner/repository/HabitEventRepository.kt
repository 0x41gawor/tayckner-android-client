package pl.gawor.android.tayckner.repository

import android.util.Log
import pl.gawor.android.tayckner.model.HabitEvent
import pl.gawor.android.tayckner.model.ResponseModel
import pl.gawor.android.tayckner.service.HabitEventApi
import pl.gawor.android.tayckner.service.RetrofitInstance
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

object HabitEventRepository {

    private  val TAG = "TAYCKNER"

    suspend fun list(): List<HabitEvent> {
        val result: List<HabitEvent>
        val habitEventApiClient: HabitEventApi = RetrofitInstance.retrofit.create(HabitEventApi::class.java)
        val response: Response<ResponseModel<List<HabitEvent>>> = try {
            habitEventApiClient.list(JWT_TOKEN)
        } catch (e: IOException) {
            Log.e(TAG, "HabitTrackerFragment.sendHabitEventsListRequest:\t\tIOException: ${e.message}")
            return emptyList()
        } catch (e: HttpException) {
            Log.e(TAG, "HabitTrackerFragment.sendHabitEventsListRequest:\t\tHttpException: ${e.message}")
            return emptyList()
        }
        if (response.isSuccessful && response.body() != null) {
            val res: ResponseModel<List<HabitEvent>> = response.body()!!
            Log.e(TAG, "HabitTrackerFragment.sendHabitEventsListRequest: $res")
            if (res.content != null)
            {
                result =  res.content
                return result
            }
        } else {
            Log.e(TAG, "HabitTrackerFragment.sendHabitEventsListRequest: HTTP status != 200")
            return emptyList()
        }

        return emptyList()
    }



}