package pl.gawor.android.tayckner.habit_tracker.repository

import android.util.Log
import pl.gawor.android.tayckner.habit_tracker.model.Habit
import pl.gawor.android.tayckner.common.model.ResponseModel
import pl.gawor.android.tayckner.repository.JWT_TOKEN
import pl.gawor.android.tayckner.habit_tracker.service.HabitApi
import pl.gawor.android.tayckner.common.service.RetrofitInstance
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class HabitRepository {

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

    suspend fun create(habit: Habit) : Habit? {
        Log.i(TAG, "HabitRepository.create()")
        var result: Habit? = null
        val habitApiClient: HabitApi = RetrofitInstance.retrofit.create(HabitApi::class.java)
        val response: Response<ResponseModel<Habit>> = try {
            habitApiClient.create(JWT_TOKEN, habit)
        }catch (e: IOException) {
            Log.e(TAG, "HabitRepository.create:\t\tIOException: ${e.message}")
            return null
        } catch (e: HttpException) {
            Log.e(TAG, "HabitRepository.create:\t\tHttpException: ${e.message}")
            return null
        }
        if (response.isSuccessful && response.body() != null) {
            val res: ResponseModel<Habit> = response.body()!!
            result = res.content
            Log.e(TAG, "HabitRepository.create: $res")
        } else {
            Log.e(TAG, "HabitRepository.create: HTTP status != 200")
        }
        Log.i(TAG, "HabitRepository.create() = $result")

        return result
    }

    suspend fun update(habit: Habit, id: Int) : Habit? {
        Log.i(TAG, "HabitRepository.update()")
        var result: Habit? = null
        val habitApiClient: HabitApi = RetrofitInstance.retrofit.create(HabitApi::class.java)
        val response: Response<ResponseModel<Habit>> = try {
            habitApiClient.update(JWT_TOKEN, habit, id)
        }catch (e: IOException) {
            Log.e(TAG, "HabitRepository.update:\t\tIOException: ${e.message}")
            return null
        } catch (e: HttpException) {
            Log.e(TAG, "HabitRepository.update:\t\tHttpException: ${e.message}")
            return null
        }
        if (response.isSuccessful && response.body() != null) {
            val res: ResponseModel<Habit> = response.body()!!
            result = res.content
            Log.e(TAG, "HabitRepository.update: $res")
        } else {
            Log.e(TAG, "HabitRepository.update: HTTP status != 200")
        }
        Log.i(TAG, "HabitRepository.update() = $result")

        return result
    }

    suspend fun delete(id: Int) {
        Log.i(TAG, "HabitRepository.delete()")
        val habitApiClient: HabitApi = RetrofitInstance.retrofit.create(HabitApi::class.java)
        val response: Response<ResponseModel<Any>> = try {
            habitApiClient.delete(JWT_TOKEN, id)
        }catch (e: IOException) {
            Log.e(TAG, "HabitRepository.delete:\t\tIOException: ${e.message}")
            return
        } catch (e: HttpException) {
            Log.e(TAG, "HabitRepository.delete:\t\tHttpException: ${e.message}")
            return
        }
        if (response.isSuccessful && response.body() != null) {
            val res: ResponseModel<Any> = response.body()!!
            Log.e(TAG, "HabitRepository.delete: $res")
        } else {
            Log.e(TAG, "HabitRepository.delete: HTTP status != 200")
        }
        Log.i(TAG, "HabitRepository.delete() = void")
    }
}