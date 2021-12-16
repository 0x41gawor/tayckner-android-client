package pl.gawor.android.tayckner

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import pl.gawor.android.tayckner.adapter.HabitAdapter
import pl.gawor.android.tayckner.adapter.HabitEventAdapter
import pl.gawor.android.tayckner.databinding.FragmentHabitTrackerBinding
import pl.gawor.android.tayckner.model.Habit
import pl.gawor.android.tayckner.model.HabitEvent
import pl.gawor.android.tayckner.model.ResponseModel
import pl.gawor.android.tayckner.service.HabitApi
import pl.gawor.android.tayckner.service.HabitEventApi
import pl.gawor.android.tayckner.service.RetrofitInstance
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException


const val JWT_TOKEN = "Bearer " + "eyJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE2Mzk2OTM4MjYsImV4cCI6MTYzOTcwMzM0OCwidXNlcklkIjozLCJ1c2VybmFtZSI6IndhemEifQ.fGz7Xd4rc8udNan8yqn12bweh4Bg8r3GHQn3HAmr8O8"

class HabitTrackerFragment : Fragment() {

    private  val TAG = "TAYCKNER"

    private lateinit var binding: FragmentHabitTrackerBinding

    private lateinit var habitAdapter: HabitAdapter
    private lateinit var habitEventAdapter: HabitEventAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sendHabitsListRequest()
        sendHabitEventsListRequest()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentHabitTrackerBinding.inflate(layoutInflater)
        setupHabitRecyclerView()
        setupHabitEventRecyclerView()

        binding.imageButtonOptions.setOnClickListener {
            Toast.makeText(context, "Options button not implemented yet", Toast.LENGTH_SHORT).show()
        }
        binding.imageButtonDayPlanner.setOnClickListener {
            Toast.makeText(context, "Day-planner button not implemented yet", Toast.LENGTH_SHORT).show()
        }
        binding.imageButtonDayTracker.setOnClickListener {
            Toast.makeText(context, "Day-tracker button not implemented yet", Toast.LENGTH_SHORT).show()
        }
        binding.imageButtonHabitTracker.setOnClickListener {
            Toast.makeText(context, "Habit-tracker button not implemented yet", Toast.LENGTH_SHORT).show()
        }
        binding.imageButtonAdd.setOnClickListener {
            Toast.makeText(context, "Add habit-event button not implemented yet", Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    private fun setupHabitRecyclerView() = binding.recyclerViewMyHabits.apply {
        habitAdapter = HabitAdapter()
        adapter = habitAdapter
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun setupHabitEventRecyclerView() = binding.recyclerViewHabitEvents.apply {
        habitEventAdapter = HabitEventAdapter()
        adapter = habitEventAdapter
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    private fun sendHabitsListRequest() {
        Log.i(TAG, "HabitTrackerFragment.sendHabitsListRequest()")
        lifecycleScope.launchWhenCreated {
            val habitApiClient: HabitApi = RetrofitInstance.retrofit.create(HabitApi::class.java)
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

    private fun sendHabitEventsListRequest() {
        Log.i(TAG, "HabitTrackerFragment.sendHabitEventsListRequest()")
        lifecycleScope.launchWhenCreated {
            val habitEventApiClient: HabitEventApi = RetrofitInstance.retrofit.create(HabitEventApi::class.java)
            val response: Response<ResponseModel<List<HabitEvent>>> = try {
                habitEventApiClient.list(JWT_TOKEN)
            } catch (e: IOException) {
                Log.e(TAG, "HabitTrackerFragment.sendHabitEventsListRequest:\t\tIOException: ${e.message}")
                return@launchWhenCreated
            } catch (e: HttpException) {
                Log.e(TAG, "HabitTrackerFragment.sendHabitEventsListRequest:\t\tHttpException: ${e.message}")
                return@launchWhenCreated
            }
            if (response.isSuccessful && response.body() != null) {
                val res: ResponseModel<List<HabitEvent>> = response.body()!!
                Log.e(TAG, "HabitTrackerFragment.sendHabitEventsListRequest: $res")
                habitEventAdapter.habitEvents = res.content
            } else {
                Log.e(TAG, "HabitTrackerFragment.sendHabitEventsListRequest: HTTP status != 200")
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