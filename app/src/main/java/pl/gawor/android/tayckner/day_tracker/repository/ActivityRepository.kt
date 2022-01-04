package pl.gawor.android.tayckner.day_tracker.repository

import android.util.Log
import pl.gawor.android.tayckner.common.model.ResponseModel
import pl.gawor.android.tayckner.common.service.RetrofitInstance
import pl.gawor.android.tayckner.day_tracker.service.ActivityApi
import pl.gawor.android.tayckner.day_tracker.model.Activity
import pl.gawor.android.tayckner.repository.JWT_TOKEN
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class ActivityRepository {
    private  val TAG = "TAYCKNER"

    suspend fun list(): List<Activity> {
        val result: List<Activity>
        val activityApiClient: ActivityApi = RetrofitInstance.retrofit.create(ActivityApi::class.java)
        val response: Response<ResponseModel<List<Activity>>> = try {
            activityApiClient.list(JWT_TOKEN)
        } catch (e: IOException) {
            Log.e(TAG, "ActivityRepository.list:\t\tIOException: ${e.message}")
            return emptyList()
        } catch (e: HttpException) {
            Log.e(TAG, "ActivityRepository.list:\t\tHttpException: ${e.message}")
            return emptyList()
        }
        if (response.isSuccessful && response.body() != null) {
            val res: ResponseModel<List<Activity>> = response.body()!!
            Log.e(TAG, "ActivityRepository.list: $res")
            if (res.content != null)
            {
                result =  res.content
                return result
            }
        } else {
            Log.e(TAG, "ActivityRepository.list: HTTP status != 200")
            return emptyList()
        }

        return emptyList()
    }

    suspend fun create(schedule: Activity) : Activity? {
        Log.i(TAG, "ActivityRepository.create()")
        var result: Activity? = null
        val activityApiClient: ActivityApi = RetrofitInstance.retrofit.create(ActivityApi::class.java)
        val response: Response<ResponseModel<Activity>> = try {
            activityApiClient.create(JWT_TOKEN, schedule)
        }catch (e: IOException) {
            Log.e(TAG, "ActivityRepository.create:\t\tIOException: ${e.message}")
            return null
        } catch (e: HttpException) {
            Log.e(TAG, "ActivityRepository.create:\t\tHttpException: ${e.message}")
            return null
        }
        if (response.isSuccessful && response.body() != null) {
            val res: ResponseModel<Activity> = response.body()!!
            result = res.content
            Log.e(TAG, "ActivityRepository.create: $res")
        } else {
            Log.e(TAG, "ActivityRepository.create: HTTP status != 200")
        }
        Log.i(TAG, "ActivityRepository.create() = $result")

        return result
    }

    suspend fun update(schedule: Activity, id: Int) : Activity? {
        Log.i(TAG, "ActivityRepository.update()")
        var result: Activity? = null
        val activityApiClient: ActivityApi = RetrofitInstance.retrofit.create(ActivityApi::class.java)
        val response: Response<ResponseModel<Activity>> = try {
            activityApiClient.update(JWT_TOKEN, schedule, id)
        }catch (e: IOException) {
            Log.e(TAG, "ActivityRepository.update:\t\tIOException: ${e.message}")
            return null
        } catch (e: HttpException) {
            Log.e(TAG, "ActivityRepository.update:\t\tHttpException: ${e.message}")
            return null
        }
        if (response.isSuccessful && response.body() != null) {
            val res: ResponseModel<Activity> = response.body()!!
            result = res.content
            Log.e(TAG, "ActivityRepository.update: $res")
        } else {
            Log.e(TAG, "ActivityRepository.update: HTTP status != 200")
        }
        Log.i(TAG, "ActivityRepository.update() = $result")

        return result
    }

    suspend fun delete(id: Int) {
        Log.i(TAG, "ActivityRepository.delete()")
        val activityApiClient: ActivityApi = RetrofitInstance.retrofit.create(ActivityApi::class.java)
        val response: Response<ResponseModel<Any>> = try {
            activityApiClient.delete(JWT_TOKEN, id)
        }catch (e: IOException) {
            Log.e(TAG, "ActivityRepository.delete:\t\tIOException: ${e.message}")
            return
        } catch (e: HttpException) {
            Log.e(TAG, "ActivityRepository.delete:\t\tHttpException: ${e.message}")
            return
        }
        if (response.isSuccessful && response.body() != null) {
            val res: ResponseModel<Any> = response.body()!!
            Log.e(TAG, "ActivityRepository.delete: $res")
        } else {
            Log.e(TAG, "ActivityRepository.delete: HTTP status != 200")
        }
        Log.i(TAG, "ActivityRepository.delete() = void")
    }
}