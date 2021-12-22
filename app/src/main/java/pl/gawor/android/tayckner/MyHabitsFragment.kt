package pl.gawor.android.tayckner

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import pl.gawor.android.tayckner.adapter.MyHabitAdapter
import pl.gawor.android.tayckner.databinding.FragmentMyHabitsBinding
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
        return binding.root
    }

    private fun setupHabitRecyclerView()  = binding.recyclerViewMyHabits.apply {
        habitAdapter = MyHabitAdapter()
        adapter = habitAdapter
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    inner class Repository{
        fun refreshHabitsList() {
            Log.i(TAG, "MyHabitsFragment.Repository.refreshHabitsList() = void")
            lifecycleScope.launchWhenCreated {
                val list :List<Habit> = HabitRepository.list()
                habitAdapter.habits = list
                return@launchWhenCreated
            }
            Log.i(TAG, "MyHabitsFragment.Repository.refreshHabitsList() = void")
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