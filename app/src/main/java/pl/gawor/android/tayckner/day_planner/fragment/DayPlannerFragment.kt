package pl.gawor.android.tayckner.day_planner.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import pl.gawor.android.tayckner.common.util.Util
import pl.gawor.android.tayckner.databinding.DayPlannerDialogAddScheduleBinding
import pl.gawor.android.tayckner.databinding.DayPlannerFragmentMainBinding
import pl.gawor.android.tayckner.day_planner.adapter.ScheduleAdapter
import pl.gawor.android.tayckner.day_planner.model.Schedule
import pl.gawor.android.tayckner.day_planner.repository.ScheduleRepository
import java.util.*

class DayPlannerFragment : Fragment() {

    private  val TAG = "TAYCKNER"

    private lateinit var binding: DayPlannerFragmentMainBinding

    private lateinit var scheduleAdapter: ScheduleAdapter

    private val repository = Repository()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        repository.refreshSchedulesList()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DayPlannerFragmentMainBinding.inflate(layoutInflater)
        setupScheduleRecyclerView()
        setupDate()

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
            addSchedule()
        }

        return binding.root
    }



    private fun setupScheduleRecyclerView() = binding.recyclerView.apply {
        scheduleAdapter = ScheduleAdapter(context)
        adapter = scheduleAdapter
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

    private fun addSchedule() {
        Log.e(TAG, "DayPlannerFragment.addSchedule()")

        val bindingAddSchedule = DayPlannerDialogAddScheduleBinding.inflate(layoutInflater)

        val editTextName = bindingAddSchedule.editTextName
        val editTextStart = bindingAddSchedule.editTextStart
        val editTextEnd = bindingAddSchedule.editTextEnd
        val editTextDuration = bindingAddSchedule.editTextDuration

        val dialogAddSchedule = AlertDialog.Builder(context)

        dialogAddSchedule.setView(bindingAddSchedule.root)

        dialogAddSchedule.setPositiveButton("Add") {
                dialog,_->
                repository.sendSchedulesCreateRequest(editTextName, editTextStart, editTextEnd, editTextDuration)
                Thread.sleep(500)
                repository.refreshSchedulesList()
                dialog.dismiss()
        }
        dialogAddSchedule.setNegativeButton("Cancel") {
                dialog,_->
                Toast.makeText(context, "Adding canceled", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
        }
        dialogAddSchedule.create()
        dialogAddSchedule.show()
        Log.e(TAG, "DayPlannerFragment.addSchedule() = void")
    }

    inner class Repository{
        private val scheduleRepository = ScheduleRepository()

        fun refreshSchedulesList() {
            Log.i(TAG, "DayPlannerFragment.Repository.refreshSchedulesList()")
            lifecycleScope.launchWhenCreated {
                val list :List<Schedule> = scheduleRepository.list()
                scheduleAdapter.schedules = list
                return@launchWhenCreated
            }
            Log.i(TAG, "DayPlannerFragment.Repository.refreshSchedulesList() = void")
        }

        fun sendSchedulesCreateRequest(editTextName: EditText, editTextStart: EditText, editTextEnd: EditText, editTextDuration: EditText) {
            Log.i(TAG, "DayPlannerFragment.Repository.sendSchedulesCreateRequest()")
            val name = editTextName.text.toString()
            val duration = editTextDuration.text.toString().toInt()

            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH) + 1
            val day = c.get(Calendar.DAY_OF_MONTH)

            val date = "$year-${if (month < 10) "0$month" else month}-${if (day < 10) "0$day" else day}"

            var start = editTextStart.text.toString()
            var end = editTextEnd.text.toString()

            if (start == "-") {
                start = "00:00"
            }
            if (end == "-") {
                end = "00:00"
            }

            start = "${date}T${start}:00"
            end = "${date}T${end}:00"

            val schedule = Schedule(duration, end, 0, name, start, null)

            lifecycleScope.launchWhenCreated {
                scheduleRepository.create(schedule)
                return@launchWhenCreated
            }

        }
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DayPlannerFragment().apply {

            }
    }
}