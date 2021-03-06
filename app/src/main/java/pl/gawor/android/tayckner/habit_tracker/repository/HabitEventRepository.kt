package pl.gawor.android.tayckner.habit_tracker.repository

import android.util.Log
import pl.gawor.android.tayckner.habit_tracker.model.HabitEvent
import pl.gawor.android.tayckner.common.model.ResponseModel
import pl.gawor.android.tayckner.repository.JWT_TOKEN
import pl.gawor.android.tayckner.habit_tracker.service.HabitEventApi
import pl.gawor.android.tayckner.common.service.RetrofitInstance
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class HabitEventRepository {

    private  val TAG = "TAYCKNER"

    suspend fun list(): List<HabitEvent> {
        val result: List<HabitEvent>
        val habitEventApiClient: HabitEventApi = RetrofitInstance.retrofit.create(HabitEventApi::class.java)
        val response: Response<ResponseModel<List<HabitEvent>>> = try {
            habitEventApiClient.list(JWT_TOKEN)
        } catch (e: IOException) {
            Log.e(TAG, "HabitEventRepository.list:\t\tIOException: ${e.message}")
            return emptyList()
        } catch (e: HttpException) {
            Log.e(TAG, "HabitEventRepository.list:\t\tHttpException: ${e.message}")
            return emptyList()
        }
        if (response.isSuccessful && response.body() != null) {
            val res: ResponseModel<List<HabitEvent>> = response.body()!!
            Log.e(TAG, "HabitEventRepository.list: $res")
            if (res.content != null)
            {
                result =  res.content
                return result
            }
        } else {
            Log.e(TAG, "HabitEventRepository.list: HTTP status != 200")
            return emptyList()
        }

        return emptyList()
    }

    suspend fun create(habitEvent: HabitEvent) : HabitEvent? {
        Log.i(TAG, "HabitEventRepository.create()")
        var result: HabitEvent? = null
        val habitEventApiClient: HabitEventApi = RetrofitInstance.retrofit.create(HabitEventApi::class.java)
        val response: Response<ResponseModel<HabitEvent>> = try {
            habitEventApiClient.create(JWT_TOKEN, habitEvent)
        }catch (e: IOException) {
            Log.e(TAG, "HabitEventRepository.create:\t\tIOException: ${e.message}")
            return null
        } catch (e: HttpException) {
            Log.e(TAG, "HabitEventRepository.create:\t\tHttpException: ${e.message}")
            return null
        }
        if (response.isSuccessful && response.body() != null) {
            val res: ResponseModel<HabitEvent> = response.body()!!
            result = res.content
            Log.e(TAG, "HabitEventRepository.create: $res")
        } else {
            Log.e(TAG, "HabitEventRepository.create: HTTP status != 200")
        }
        Log.i(TAG, "HabitEventRepository.create() = $result")

        return result
    }

    suspend fun update(habitEvent: HabitEvent, id: Int) : HabitEvent? {
        Log.i(TAG, "HabitEventRepository.update()")
        var result: HabitEvent? = null
        val habitEventApiClient: HabitEventApi = RetrofitInstance.retrofit.create(HabitEventApi::class.java)
        val response: Response<ResponseModel<HabitEvent>> = try {
            habitEventApiClient.update(JWT_TOKEN, habitEvent, id)
        }catch (e: IOException) {
            Log.e(TAG, "HabitEventRepository.update:\t\tIOException: ${e.message}")
            return null
        } catch (e: HttpException) {
            Log.e(TAG, "HabitEventRepository.update:\t\tHttpException: ${e.message}")
            return null
        }
        if (response.isSuccessful && response.body() != null) {
            val res: ResponseModel<HabitEvent> = response.body()!!
            result = res.content
            Log.e(TAG, "HabitEventRepository.update: $res")
        } else {
            Log.e(TAG, "HabitEventRepository.update: HTTP status != 200")
        }
        Log.i(TAG, "HabitEventRepository.update() = $result")

        return result
    }

    suspend fun delete(id: Int) {
        Log.i(TAG, "HabitEventRepository.delete()")
        val habitEventApiClient: HabitEventApi = RetrofitInstance.retrofit.create(HabitEventApi::class.java)
        val response: Response<ResponseModel<Any>> = try {
            habitEventApiClient.delete(JWT_TOKEN, id)
        }catch (e: IOException) {
            Log.e(TAG, "HabitEventRepository.delete:\t\tIOException: ${e.message}")
            return
        } catch (e: HttpException) {
            Log.e(TAG, "HabitEventRepository.delete:\t\tHttpException: ${e.message}")
            return
        }
        if (response.isSuccessful && response.body() != null) {
            val res: ResponseModel<Any> = response.body()!!
            Log.e(TAG, "HabitEventRepository.delete: $res")
        } else {
            Log.e(TAG, "HabitEventRepository.delete: HTTP status != 200")
        }
        Log.i(TAG, "HabitEventRepository.delete() = void")
    }
}