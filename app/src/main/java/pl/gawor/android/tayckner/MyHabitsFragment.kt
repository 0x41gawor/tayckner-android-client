package pl.gawor.android.tayckner

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import pl.gawor.android.tayckner.adapter.HabitAdapter
import pl.gawor.android.tayckner.databinding.FragmentHabitTrackerBinding
import pl.gawor.android.tayckner.databinding.FragmentMyHabitsBinding

class MyHabitsFragment : Fragment() {

    private lateinit var binding: FragmentMyHabitsBinding

    private lateinit var habitAdapter: HabitAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMyHabitsBinding.inflate(layoutInflater)
        setupHabitRecyclerView()
        return binding.root
    }

    private fun setupHabitRecyclerView()  = binding.recyclerViewMyHabits.apply {
        habitAdapter = HabitAdapter()
        adapter = habitAdapter
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
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