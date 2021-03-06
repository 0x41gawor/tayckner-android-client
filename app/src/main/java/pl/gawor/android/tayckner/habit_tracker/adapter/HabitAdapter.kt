package pl.gawor.android.tayckner.habit_tracker.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import pl.gawor.android.tayckner.databinding.HabitTrackerItemHabitBinding
import pl.gawor.android.tayckner.habit_tracker.model.Habit

class HabitAdapter : RecyclerView.Adapter<HabitAdapter.HabitViewHolder>() {

    inner class HabitViewHolder(val binding: HabitTrackerItemHabitBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffCallback = object : DiffUtil.ItemCallback<Habit>() {
        override fun areItemsTheSame(oldItem: Habit, newItem: Habit): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Habit, newItem: Habit): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)
    var habits: List<Habit>
        get() = differ.currentList
        set(value) {differ.submitList(value)}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitViewHolder {
        return HabitViewHolder(HabitTrackerItemHabitBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: HabitViewHolder, position: Int) {
        holder.binding.apply {
            val habit = habits[position]
            textViewHabit.text = habit.name
            materialCardView.background.current.setTint(Color.parseColor(habit.color))
        }
    }

    override fun getItemCount(): Int {
        return habits.size
    }
}