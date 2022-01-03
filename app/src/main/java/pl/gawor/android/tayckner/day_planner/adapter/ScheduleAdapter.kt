package pl.gawor.android.tayckner.day_planner.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import pl.gawor.android.tayckner.databinding.DayPlannerItemScheduleBinding
import pl.gawor.android.tayckner.day_planner.model.Schedule

class ScheduleAdapter(val context: Context) : RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder>() {
    inner class ScheduleViewHolder(val binding: DayPlannerItemScheduleBinding) : RecyclerView.ViewHolder(binding.root)

    private  val TAG = "TAYCKNER"

    private val diffCallback = object : DiffUtil.ItemCallback<Schedule>() {
        override fun areItemsTheSame(oldItem: Schedule, newItem: Schedule): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Schedule, newItem: Schedule): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    var schedules: List<Schedule>
        get() = differ.currentList
        set(value) {
            differ.submitList(value)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        val binding = DayPlannerItemScheduleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ScheduleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        holder.binding.apply {
            val schedule = schedules[position]
            textViewName.text = schedule.name
            textViewStart.text = schedule.startTime
            textViewEnd.text = schedule.endTime
            textViewDuration.text = schedule.duration.toString()
        }
    }

    override fun getItemCount(): Int {
        return schedules.size
    }


}