package pl.gawor.android.tayckner.day_tracker.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import pl.gawor.android.tayckner.databinding.DayTrackerItemActivityBinding
import pl.gawor.android.tayckner.day_tracker.model.Activity

class ActivityAdapter(val context: Context) : RecyclerView.Adapter<ActivityAdapter.ActivityViewHolder>() {
    inner class ActivityViewHolder(val binding: DayTrackerItemActivityBinding) : RecyclerView.ViewHolder(binding.root)

    private  val TAG = "TAYCKNER"

    private val diffCallback = object : DiffUtil.ItemCallback<Activity>() {
        override fun areItemsTheSame(oldItem: Activity, newItem: Activity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Activity, newItem: Activity): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    var activities: List<Activity>
        get() = differ.currentList
        set(value) {
            differ.submitList(value)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityViewHolder {
        val binding = DayTrackerItemActivityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ActivityViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ActivityViewHolder, position: Int) {
        holder.binding.apply {
            val activity = activities[position]
            textViewName.text = activity.name
            textViewStart.text = activity.startTime.substring(11, 16)
            textViewEnd.text = activity.endTime.substring(11, 16)
            textViewCategory.text = activity.category.name
            linearLayout.background.current.setTint(Color.parseColor(activity.category.color))
        }
    }

    override fun getItemCount(): Int {
        return activities.size
    }

}