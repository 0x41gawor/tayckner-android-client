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
            Log.e(TAG, "CategoryRepository.list:\t\tIOException: ${e.message}")
            return emptyList()
        } catch (e: HttpException) {
            Log.e(TAG, "CategoryRepository.list:\t\tHttpException: ${e.message}")
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

    suspend fun create(category: Category) : Category? {
        Log.i(TAG, "CategoryRepository.create()")
        var result: Category? = null
        val categoryApiClient: CategoryApi = RetrofitInstance.retrofit.create(CategoryApi::class.java)
        val response: Response<ResponseModel<Category>> = try {
            categoryApiClient.create(JWT_TOKEN, category)
        }catch (e: IOException) {
            Log.e(TAG, "CategoryRepository.create:\t\tIOException: ${e.message}")
            return null
        } catch (e: HttpException) {
            Log.e(TAG, "CategoryRepository.create:\t\tHttpException: ${e.message}")
            return null
        }
        if (response.isSuccessful && response.body() != null) {
            val res: ResponseModel<Category> = response.body()!!
            result = res.content
            Log.e(TAG, "CategoryRepository.create: $res")
        } else {
            Log.e(TAG, "CategoryRepository.create: HTTP status != 200")
        }
        Log.i(TAG, "CategoryRepository.create() = $result")

        return result
    }

    suspend fun update(category: Category, id: Int) : Category? {
        Log.i(TAG, "CategoryRepository.update()")
        var result: Category? = null
        val categoryApiClient: CategoryApi = RetrofitInstance.retrofit.create(CategoryApi::class.java)
        val response: Response<ResponseModel<Category>> = try {
            categoryApiClient.update(JWT_TOKEN, category, id)
        }catch (e: IOException) {
            Log.e(TAG, "CategoryRepository.update:\t\tIOException: ${e.message}")
            return null
        } catch (e: HttpException) {
            Log.e(TAG, "CategoryRepository.update:\t\tHttpException: ${e.message}")
            return null
        }
        if (response.isSuccessful && response.body() != null) {
            val res: ResponseModel<Category> = response.body()!!
            result = res.content
            Log.e(TAG, "CategoryRepository.update: $res")
        } else {
            Log.e(TAG, "CategoryRepository.update: HTTP status != 200")
        }
        Log.i(TAG, "CategoryRepository.update() = $result")

        return result
    }

    suspend fun delete(id: Int) {
        Log.i(TAG, "CategoryRepository.delete()")
        val categoryApiClient: CategoryApi = RetrofitInstance.retrofit.create(CategoryApi::class.java)
        val response: Response<ResponseModel<Any>> = try {
            categoryApiClient.delete(JWT_TOKEN, id)
        }catch (e: IOException) {
            Log.e(TAG, "CategoryRepository.delete:\t\tIOException: ${e.message}")
            return
        } catch (e: HttpException) {
            Log.e(TAG, "CategoryRepository.delete:\t\tHttpException: ${e.message}")
            return
        }
        if (response.isSuccessful && response.body() != null) {
            val res: ResponseModel<Any> = response.body()!!
            Log.e(TAG, "CategoryRepository.delete: $res")
        } else {
            Log.e(TAG, "CategoryRepository.delete: HTTP status != 200")
        }
        Log.i(TAG, "CategoryRepository.delete() = void")
    }
}