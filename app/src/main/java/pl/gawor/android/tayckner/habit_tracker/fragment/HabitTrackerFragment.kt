package pl.gawor.android.tayckner.habit_tracker.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import pl.gawor.android.tayckner.R
import pl.gawor.android.tayckner.habit_tracker.adapter.HabitAdapter
import pl.gawor.android.tayckner.habit_tracker.adapter.HabitEventAdapter
import pl.gawor.android.tayckner.databinding.HabitTrackerFragmentMainBinding
import pl.gawor.android.tayckner.databinding.HabitTrackerItemAddHabitEventBinding
import pl.gawor.android.tayckner.habit_tracker.model.Habit
import pl.gawor.android.tayckner.habit_tracker.model.HabitEvent
import pl.gawor.android.tayckner.habit_tracker.repository.HabitEventRepository
import pl.gawor.android.tayckner.habit_tracker.repository.HabitRepository
import pl.gawor.android.tayckner.common.util.SharedPrefManager


class HabitTrackerFragment : Fragment() {

    private  val TAG = "TAYCKNER"

    private lateinit var binding: HabitTrackerFragmentMainBinding

    private lateinit var habitAdapter: HabitAdapter
    private lateinit var habitEventAdapter: HabitEventAdapter

    private val repository = Repository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        repository.refreshHabitsList()
        repository.refreshHabitEventsList()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = HabitTrackerFragmentMainBinding.inflate(layoutInflater)
        setupHabitRecyclerView()
        setupHabitEventRecyclerView()

        binding.imageButtonOptions.setOnClickListener {
            optionsMenu(binding.imageButtonOptions)
        }
        binding.imageButtonDayPlanner.setOnClickListener {
          findNavController().navigate(R.id.action_habitTrackerFragment_to_dayPlannerFragment)
        }
        binding.imageButtonDayTracker.setOnClickListener {
            findNavController().navigate(R.id.action_habitTrackerFragment_to_dayTrackerFragment)
        }
        binding.imageButtonHabitTracker.setOnClickListener {

        }
        binding.imageButtonAdd.setOnClickListener {
            addHabitEvent()
        }
        binding.linearLayoutMyHabitsPanel.setOnClickListener {
            findNavController().navigate(R.id.action_habitTrackerFragment_to_myHabitsFragment)
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        repository.refreshHabitEventsList()
        repository.refreshHabitsList()
    }

    private fun setupHabitRecyclerView() = binding.recyclerViewMyHabits.apply {
        habitAdapter = HabitAdapter()
        adapter = habitAdapter
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun setupHabitEventRecyclerView() = binding.recyclerViewHabitEvents.apply {
        habitEventAdapter = HabitEventAdapter(context)
        adapter = habitEventAdapter
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }


    private fun addHabitEvent() {
        Log.e(TAG, "HabitTrackerFragment.addHabitEvent()")
        val bindingAddHabitEvent = HabitTrackerItemAddHabitEventBinding.inflate(layoutInflater)

        val editTextHabitId = bindingAddHabitEvent.editTextHabit
        val editTextDate = bindingAddHabitEvent.editTextDate
        val editTextComment = bindingAddHabitEvent.editTextComment
        val editTextValue = bindingAddHabitEvent.editTextValue

        val dialogAddHabitEvent = AlertDialog.Builder(context)

        dialogAddHabitEvent.setView(bindingAddHabitEvent.root)

        dialogAddHabitEvent.setPositiveButton("OK") {
                dialog,_->
            repository.sendHabitEventsCreateRequest(editTextHabitId, editTextDate, editTextComment, editTextValue)
            Thread.sleep(500)
            repository.refreshHabitEventsList()
            dialog.dismiss()
        }
        dialogAddHabitEvent.setNegativeButton("Cancel") {
                dialog,_->
            Toast.makeText(context, "Adding canceled", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
        dialogAddHabitEvent.create()
        dialogAddHabitEvent.show()
        Log.e(TAG, "HabitTrackerFragment.addHabitEvent() = void")
    }

    private fun optionsMenu(view: View) {
        val popupMenus = PopupMenu(context, view)
        popupMenus.inflate(R.menu.habit_tracker_menu_top_bar_options)
        popupMenus.setOnMenuItemClickListener{
            when(it.itemId) {
                 R.id.logout -> {
                     SharedPrefManager.logout(this)
                     findNavController().navigate(R.id.action_habitTrackerFragment_to_loginFragment)
                     true}
                else -> {true}
            }
        }
        popupMenus.show()
    }


    inner class Repository{
        private val habitEventRepository = HabitEventRepository()
        private val habitRepository = HabitRepository()

        fun sendHabitEventsCreateRequest(editTextHabitId: EditText, editTextDate: EditText, editTextComment: EditText, editTextValue: EditText) {
            Log.i(TAG, "HabitTrackerFragment.Repository.sendHabitEventsCreateRequest()")
            val habitId = editTextHabitId.text.toString().toLong()
            val date = editTextDate.text.toString()
            val comment = editTextComment.text.toString()
            val value = editTextValue.text.toString().toInt()
            val habit = Habit(habitId,"","", null)
            val habitEvent = HabitEvent(comment, date, habit, 0, value)
            lifecycleScope.launchWhenCreated {
                habitEventRepository.create(habitEvent)
                return@launchWhenCreated
            }
            Log.i(TAG, "HabitTrackerFragment.Repository.sendHabitEventsCreateRequest() = void")

        }

        fun refreshHabitEventsList() {
            Log.i(TAG, "HabitTrackerFragment.Repository.refreshHabitEventsList()")
            lifecycleScope.launchWhenCreated {
                val list :List<HabitEvent> = habitEventRepository.list()
                habitEventAdapter.habitEvents = list
                return@launchWhenCreated
            }
            Log.i(TAG, "HabitTrackerFragment.Repository.refreshHabitEventsList() = void")
        }

        fun refreshHabitsList() {
            Log.i(TAG, "HabitTrackerFragment.Repository.refreshHabitsList()")
            lifecycleScope.launchWhenCreated {
                val list :List<Habit> = habitRepository.list()
                habitAdapter.habits = list
                return@launchWhenCreated
            }
            Log.i(TAG, "HabitTrackerFragment.Repository.refreshHabitsList() = void")
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            HabitTrackerFragment().apply {
            }
    }
}