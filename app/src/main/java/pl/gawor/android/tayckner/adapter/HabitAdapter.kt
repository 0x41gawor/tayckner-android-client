package pl.gawor.android.tayckner.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import pl.gawor.android.tayckner.databinding.HabitItemBinding
import pl.gawor.android.tayckner.model.HabitModel

class HabitAdapter : RecyclerView.Adapter<HabitAdapter.HabitViewHolder>() {

    inner class HabitViewHolder(val binding: HabitItemBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffCallback = object : DiffUtil.ItemCallback<HabitModel>() {
        override fun areItemsTheSame(oldItem: HabitModel, newItem: HabitModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: HabitModel, newItem: HabitModel): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)
    var habits: List<HabitModel>
        get() = differ.currentList
        set(value) {differ.submitList(value)}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitViewHolder {
        return HabitViewHolder(HabitItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: HabitViewHolder, position: Int) {
        holder.binding.apply {
            val habit = habits[position]
            textViewHabit.text = habit.name
            frameLayout.background.current.setTint(Color.parseColor(habit.color))
        }
    }

    override fun getItemCount(): Int {
        return habits.size
    }
}