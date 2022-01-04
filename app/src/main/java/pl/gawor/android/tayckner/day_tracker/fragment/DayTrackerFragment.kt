package pl.gawor.android.tayckner.day_tracker.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import pl.gawor.android.tayckner.R
import pl.gawor.android.tayckner.common.util.Util
import pl.gawor.android.tayckner.databinding.DayPlannerFragmentMainBinding
import pl.gawor.android.tayckner.day_planner.model.Schedule
import pl.gawor.android.tayckner.day_planner.repository.ScheduleRepository
import pl.gawor.android.tayckner.day_tracker.adapter.ActivityAdapter
import pl.gawor.android.tayckner.day_tracker.model.Activity
import pl.gawor.android.tayckner.day_tracker.repository.ActivityRepository
import java.util.*


class DayTrackerFragment : Fragment() {

    private  val TAG = "TAYCKNER"

    private lateinit var binding: DayPlannerFragmentMainBinding

    private lateinit var activityAdapter: ActivityAdapter

    private val repository = Repository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        repository.refreshActivitiesList()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DayPlannerFragmentMainBinding.inflate(layoutInflater)
        setupActivitiesRecyclerView()
        setupDate()
        return binding.root
    }

    private fun setupActivitiesRecyclerView() = binding.recyclerView.apply {
        activityAdapter = ActivityAdapter(context)
        adapter = activityAdapter
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    private fun setupDate() {
        val textViewDate = binding.textViewDate
        val c = Calendar.getInstance()

        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        textViewDate.text = "$day ${Util.convertMonth(month)} $year"
    }

    inner class Repository {
        private val activityRepository = ActivityRepository()

        fun refreshActivitiesList() {
            Log.i(TAG, "DayPlannerFragment.Repository.refreshSchedulesList()")
            lifecycleScope.launchWhenCreated {
                val list: List<Activity> = activityRepository.list()
                activityAdapter.activities = list
                return@launchWhenCreated
            }
            Log.i(TAG, "DayPlannerFragment.Repository.refreshSchedulesList() = void")
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            DayTrackerFragment().apply {
            }
    }
}