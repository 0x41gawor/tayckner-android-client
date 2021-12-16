package pl.gawor.android.tayckner.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import pl.gawor.android.tayckner.databinding.HabitEventItemBinding
import pl.gawor.android.tayckner.model.HabitEvent

class HabitEventAdapter : RecyclerView.Adapter<HabitEventAdapter.HabitEventViewHolder>() {
    inner class HabitEventViewHolder(val binding: HabitEventItemBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffCallback = object : DiffUtil.ItemCallback<HabitEvent>() {
        override fun areItemsTheSame(oldItem: HabitEvent, newItem: HabitEvent): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: HabitEvent, newItem: HabitEvent): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    var habitEvents: List<HabitEvent>
        get() = differ.currentList
        set(value) {
            differ.submitList(value)
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitEventViewHolder {
        return HabitEventViewHolder(
            HabitEventItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: HabitEventViewHolder, position: Int) {
        holder.binding.apply {
            val habitEvent = habitEvents[position]
            textViewHabitName.text = habitEvent.habit.name
            //TODO split date and year
            textViewDate.text = habitEvent.date
            textViewYear.text = habitEvent.date
            textViewComment.text = habitEvent.comment
        }
    }

    override fun getItemCount(): Int {
        return habitEvents.size
    }


}