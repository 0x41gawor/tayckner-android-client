package pl.gawor.android.tayckner.day_tracker.repository

import android.util.Log
import pl.gawor.android.tayckner.common.model.ResponseModel
import pl.gawor.android.tayckner.common.service.RetrofitInstance
import pl.gawor.android.tayckner.day_tracker.model.Category
import pl.gawor.android.tayckner.day_tracker.service.CategoryApi
import pl.gawor.android.tayckner.repository.JWT_TOKEN
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class CategoryRepository {
    private  val TAG = "TAYCKNER"

    suspend fun list(): List<Category> {
        val result: List<Category>
        val categoryApiClient: CategoryApi = RetrofitInstance.retrofit.create(CategoryApi::class.java)
        val response: Response<ResponseModel<List<Category>>> = try {
            categoryApiClient.list(JWT_TOKEN)
        } catch (e: IOException) {
            Log.e(TAG, "ActivityRepository.list:\t\tIOException: ${e.message}")
            return emptyList()
        } catch (e: HttpException) {
            Log.e(TAG, "ActivityRepository.list:\t\tHttpException: ${e.message}")
            return emptyList()
        }
        if (response.isSuccessful && response.body() != null) {
            val res: ResponseModel<List<Category>> = response.body()!!
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

    suspend fun create(schedule: Category) : Category? {
        Log.i(TAG, "ActivityRepository.create()")
        var result: Category? = null
        val categoryApiClient: CategoryApi = RetrofitInstance.retrofit.create(CategoryApi::class.java)
        val response: Response<ResponseModel<Category>> = try {
            categoryApiClient.create(JWT_TOKEN, schedule)
        }catch (e: IOException) {
            Log.e(TAG, "ActivityRepository.create:\t\tIOException: ${e.message}")
            return null
        } catch (e: HttpException) {
            Log.e(TAG, "ActivityRepository.create:\t\tHttpException: ${e.message}")
            return null
        }
        if (response.isSuccessful && response.body() != null) {
            val res: ResponseModel<Category> = response.body()!!
            result = res.content
            Log.e(TAG, "ActivityRepository.create: $res")
        } else {
            Log.e(TAG, "ActivityRepository.create: HTTP status != 200")
        }
        Log.i(TAG, "ActivityRepository.create() = $result")

        return result
    }

    suspend fun update(schedule: Category, id: Int) : Category? {
        Log.i(TAG, "ActivityRepository.update()")
        var result: Category? = null
        val categoryApiClient: CategoryApi = RetrofitInstance.retrofit.create(CategoryApi::class.java)
        val response: Response<ResponseModel<Category>> = try {
            categoryApiClient.update(JWT_TOKEN, schedule, id)
        }catch (e: IOException) {
            Log.e(TAG, "ActivityRepository.update:\t\tIOException: ${e.message}")
            return null
        } catch (e: HttpException) {
            Log.e(TAG, "ActivityRepository.update:\t\tHttpException: ${e.message}")
            return null
        }
        if (response.isSuccessful && response.body() != null) {
            val res: ResponseModel<Category> = response.body()!!
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
        val categoryApiClient: CategoryApi = RetrofitInstance.retrofit.create(CategoryApi::class.java)
        val response: Response<ResponseModel<Any>> = try {
            categoryApiClient.delete(JWT_TOKEN, id)
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