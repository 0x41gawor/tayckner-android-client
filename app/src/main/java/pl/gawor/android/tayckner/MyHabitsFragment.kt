package pl.gawor.android.tayckner

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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import pl.gawor.android.tayckner.adapter.MyHabitAdapter
import pl.gawor.android.tayckner.databinding.FragmentMyHabitsBinding
import pl.gawor.android.tayckner.databinding.ItemAddHabitBinding
import pl.gawor.android.tayckner.model.Habit
import pl.gawor.android.tayckner.repository.HabitRepository

class MyHabitsFragment : Fragment() {

    private  val TAG = "TAYCKNER"

    private lateinit var binding: FragmentMyHabitsBinding

    private lateinit var habitAdapter: MyHabitAdapter

    private val repository = Repository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        repository.refreshHabitsList()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMyHabitsBinding.inflate(layoutInflater)
        setupHabitRecyclerView()

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
            addHabit()
        }
        binding.textViewMyHabits.setOnClickListener{
            findNavController().navigate(R.id.action_myHabitsFragment_to_habitTrackerFragment)
        }
        return binding.root
    }

    private fun setupHabitRecyclerView()  = binding.recyclerViewMyHabits.apply {
        habitAdapter = MyHabitAdapter(context)
        adapter = habitAdapter
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    private fun addHabit() {
        Log.e(TAG, "HabitTrackerFragment.addHabit()")
        val bindingAddHabit = ItemAddHabitBinding.inflate(layoutInflater)

        val editTextName = bindingAddHabit.editTextName
        val editTextColor = bindingAddHabit.editTextColor

        val dialogAddHabitEvent = AlertDialog.Builder(context)

        dialogAddHabitEvent.setView(bindingAddHabit.root)

        dialogAddHabitEvent.setPositiveButton("OK") {
                dialog,_->
            repository.sendHabitsCreateRequest(editTextName, editTextColor)
            Thread.sleep(500)
            repository.refreshHabitsList()
            dialog.dismiss()
        }
        dialogAddHabitEvent.setNegativeButton("Cancel") {
                dialog,_->
            Toast.makeText(context, "Adding canceled", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
        dialogAddHabitEvent.create()
        dialogAddHabitEvent.show()
        Log.e(TAG, "HabitTrackerFragment.addHabit() = void")
    }

    inner class Repository{
        private val habitRepository = HabitRepository()

        fun refreshHabitsList() {
            Log.i(TAG, "MyHabitsFragment.Repository.refreshHabitsList() = void")
            lifecycleScope.launchWhenCreated {
                val list :List<Habit> = habitRepository.list()
                habitAdapter.habits = list
                return@launchWhenCreated
            }
            Log.i(TAG, "MyHabitsFragment.Repository.refreshHabitsList() = void")
        }

        fun sendHabitsCreateRequest(editTextName: EditText,editTextColor: EditText) {
            val name = editTextName.text.toString()
            val color = editTextColor.text.toString()
            val habit = Habit(0,name,color, null)
            lifecycleScope.launchWhenCreated {
                habitRepository.create(habit)
                return@launchWhenCreated
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            MyHabitsFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}