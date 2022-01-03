package pl.gawor.android.tayckner.day_planner.repository

import android.util.Log
import pl.gawor.android.tayckner.common.model.ResponseModel
import pl.gawor.android.tayckner.common.service.RetrofitInstance
import pl.gawor.android.tayckner.day_planner.model.Schedule
import pl.gawor.android.tayckner.day_planner.service.ScheduleApi
import pl.gawor.android.tayckner.repository.JWT_TOKEN
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class ScheduleRepository {

    private  val TAG = "TAYCKNER"

    suspend fun list(): List<Schedule> {
        val result: List<Schedule>
        val scheduleApiClient: ScheduleApi = RetrofitInstance.retrofit.create(ScheduleApi::class.java)
        val response: Response<ResponseModel<List<Schedule>>> = try {
            scheduleApiClient.list(JWT_TOKEN)
        } catch (e: IOException) {
            Log.e(TAG, "ScheduleRepository.list:\t\tIOException: ${e.message}")
            return emptyList()
        } catch (e: HttpException) {
            Log.e(TAG, "ScheduleRepository.list:\t\tHttpException: ${e.message}")
            return emptyList()
        }
        if (response.isSuccessful && response.body() != null) {
            val res: ResponseModel<List<Schedule>> = response.body()!!
            Log.e(TAG, "ScheduleRepository.list: $res")
            if (res.content != null)
            {
                result =  res.content
                return result
            }
        } else {
            Log.e(TAG, "ScheduleRepository.list: HTTP status != 200")
            return emptyList()
        }

        return emptyList()
    }

    suspend fun create(schedule: Schedule) : Schedule? {
        Log.i(TAG, "ScheduleRepository.create()")
        var result: Schedule? = null
        val scheduleApiClient: ScheduleApi = RetrofitInstance.retrofit.create(ScheduleApi::class.java)
        val response: Response<ResponseModel<Schedule>> = try {
            scheduleApiClient.create(JWT_TOKEN, schedule)
        }catch (e: IOException) {
            Log.e(TAG, "ScheduleRepository.create:\t\tIOException: ${e.message}")
            return null
        } catch (e: HttpException) {
            Log.e(TAG, "ScheduleRepository.create:\t\tHttpException: ${e.message}")
            return null
        }
        if (response.isSuccessful && response.body() != null) {
            val res: ResponseModel<Schedule> = response.body()!!
            result = res.content
            Log.e(TAG, "ScheduleRepository.create: $res")
        } else {
            Log.e(TAG, "ScheduleRepository.create: HTTP status != 200")
        }
        Log.i(TAG, "ScheduleRepository.create() = $result")

        return result
    }

    suspend fun update(schedule: Schedule, id: Int) : Schedule? {
        Log.i(TAG, "ScheduleRepository.update()")
        var result: Schedule? = null
        val scheduleApiClient: ScheduleApi = RetrofitInstance.retrofit.create(ScheduleApi::class.java)
        val response: Response<ResponseModel<Schedule>> = try {
            scheduleApiClient.update(JWT_TOKEN, schedule, id)
        }catch (e: IOException) {
            Log.e(TAG, "ScheduleRepository.update:\t\tIOException: ${e.message}")
            return null
        } catch (e: HttpException) {
            Log.e(TAG, "ScheduleRepository.update:\t\tHttpException: ${e.message}")
            return null
        }
        if (response.isSuccessful && response.body() != null) {
            val res: ResponseModel<Schedule> = response.body()!!
            result = res.content
            Log.e(TAG, "ScheduleRepository.update: $res")
        } else {
            Log.e(TAG, "ScheduleRepository.update: HTTP status != 200")
        }
        Log.i(TAG, "ScheduleRepository.update() = $result")

        return result
    }

    suspend fun delete(id: Int) {
        Log.i(TAG, "ScheduleRepository.delete()")
        val scheduleApiClient: ScheduleApi = RetrofitInstance.retrofit.create(ScheduleApi::class.java)
        val response: Response<ResponseModel<Any>> = try {
            scheduleApiClient.delete(JWT_TOKEN, id)
        }catch (e: IOException) {
            Log.e(TAG, "ScheduleRepository.delete:\t\tIOException: ${e.message}")
            return
        } catch (e: HttpException) {
            Log.e(TAG, "ScheduleRepository.delete:\t\tHttpException: ${e.message}")
            return
        }
        if (response.isSuccessful && response.body() != null) {
            val res: ResponseModel<Any> = response.body()!!
            Log.e(TAG, "ScheduleRepository.delete: $res")
        } else {
            Log.e(TAG, "ScheduleRepository.delete: HTTP status != 200")
        }
        Log.i(TAG, "ScheduleRepository.delete() = void")
    }
}