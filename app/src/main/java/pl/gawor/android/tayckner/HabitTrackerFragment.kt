package pl.gawor.android.tayckner

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import pl.gawor.android.tayckner.adapter.HabitAdapter
import pl.gawor.android.tayckner.databinding.FragmentHabitTrackerBinding
import pl.gawor.android.tayckner.model.Habit
import pl.gawor.android.tayckner.model.ResponseModel
import pl.gawor.android.tayckner.service.HabitApi
import pl.gawor.android.tayckner.service.RetrofitInstance
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException


const val JWT_TOKEN = "Bearer " + "eyJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE2Mzk1MjExNTYsImV4cCI6MTYzOTUzMDY3OCwidXNlcklkIjozLCJ1c2VybmFtZSI6IndhemEifQ.5iVafroBtowtdV95VBZNj8bxvXjAq0552wAg_0XEyCc"

class HabitTrackerFragment : Fragment() {

    private  val TAG = "TAYCKNER"

    private lateinit var binding: FragmentHabitTrackerBinding

    private lateinit var habitAdapter: HabitAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sendHabitsListRequest()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentHabitTrackerBinding.inflate(layoutInflater)
        setupHabitRecyclerView()
        return binding.root
    }

    private fun setupHabitRecyclerView() = binding.recyclerViewMyHabits.apply {
        habitAdapter = HabitAdapter()
        adapter = habitAdapter
        layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL, false)
    }

    private fun sendHabitsListRequest() {
        Log.i(TAG, "HabitTrackerFragment.sendHabitsListRequest()")
        lifecycleScope.launchWhenCreated {
            val habitApiClient: HabitApi = RetrofitInstance.retrofitHabits.create(HabitApi::class.java)
            val response: Response<ResponseModel<List<Habit>>> = try {
                habitApiClient.list(JWT_TOKEN)
            } catch (e: IOException) {
                Log.e(TAG, "HabitTrackerFragment.sendHabitsListRequest:\t\tIOException: ${e.message}")
                return@launchWhenCreated
            } catch (e: HttpException) {
                Log.e(TAG, "HabitTrackerFragment.sendHabitsListRequest:\t\tHttpException: ${e.message}")
                return@launchWhenCreated
            }
            if (response.isSuccessful && response.body() != null) {
                val res: ResponseModel<List<Habit>> = response.body()!!
                Log.e(TAG, "HabitTrackerFragment.sendHabitsListRequest: $res")
                habitAdapter.habits = res.content
            } else {
                Log.e(TAG, "HabitTrackerFragment.sendHabitsListRequest: HTTP status != 200")
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            HabitTrackerFragment().apply {
            }
    }
}